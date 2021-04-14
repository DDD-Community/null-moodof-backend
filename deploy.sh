#!/usr/bin/env bash

docker stop $(docker container ps -a -q --filter name=moodof-server)

docker rm $(docker container ps -a -q --filter name=moodof-server)

docker rmi $(docker images --filter=reference='moodof:*' -qa)

docker build --tag moodof /home/ubuntu/docker/moodof

docker run -d -p 8080:8080 --name moodof-server moodof
