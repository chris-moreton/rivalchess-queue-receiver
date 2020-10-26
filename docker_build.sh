./gradlew clean build -x test || exit
docker build --build-arg JAR_FILE=build/libs/*.jar -t rivalchess-vie-player .

