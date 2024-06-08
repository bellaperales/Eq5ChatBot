#!/bin/bash

export IMAGE_NAME=todolistapp-springboot
export IMAGE_VERSION=0.1


# Set DOCKER_REGISTRY directly
export DOCKER_REGISTRY="mx-queretaro-1.ocir.io/axvfutv1sy8e/eq5chatbot/wu5ru"
echo "DOCKER_REGISTRY (build.sh) set to: $DOCKER_REGISTRY"
export IMAGE=${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_VERSION}

mvn clean package spring-boot:repackage
docker build -f Dockerfile -t $IMAGE .

docker push $IMAGE
if [  $? -eq 0 ]; then
    docker rmi "$IMAGE"
fi
