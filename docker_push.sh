if [ -z "${AWS_ACCOUNT_ID}" ]
then
      echo "AWS_ACCOUNT_ID is not set"
      exit 1
fi

AWS_REGION="eu-west-2"

./gradlew clean build -x test || exit
docker build --build-arg JAR_FILE=build/libs/*.jar -t rivalchess-vie-player .
aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
AWS_ECR_BASE_URI="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

docker tag rivalchess-vie-player:latest ${AWS_ECR_BASE_URI}/rivalchess-vie-player:latest

docker push ${AWS_ECR_BASE_URI}/rivalchess-vie-player:latest
