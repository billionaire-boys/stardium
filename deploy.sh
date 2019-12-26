#!/bin/sh
APP_PID=`sudo lsof -i :8080 | awk 'NR == 2 {print $2}'`
echo running pid of stardium:
echo $APP_PID
kill $APP_PID

cd /home/ubuntu/stardium
git pull

./gradlew clean build
java -jar -Dserver.port=8080 build/libs/stardium-0.0.1-SNAPSHOT.jar &