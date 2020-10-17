Rival Chess Queue Receiver
==========================

Reads messages from the Rival queue, plays the requested matches and posts the results to the database.

    ./gradlew clean build
    docker build --build-arg JAR_FILE=build/libs/*.jar -t rivalchess-player .
    docker run -e ACTIVE_MQ_URL=$ACTIVE_MQ_URL -e ACTIVE_MQ_USER=$ACTIVE_MQ_USER -e ACTIVE_MQ_PASSWORD=$ACTIVE_MQ_PASSWORD -ti rivalchess-player
