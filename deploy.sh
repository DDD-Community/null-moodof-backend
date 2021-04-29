#!/usr/bin/env bash

APP_NAME=moodof-server
JAR_NAME=$(ls | grep '.jar' | tail -n 1)

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할것 없음."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_NAME 배포"
nohup java -jar $JAR_NAME &
