#!/bin/bash

export IMAGE_NAME=todolistapp-springboot
export IMAGE_VERSION=0.1

export JAVA_HOME="/usr/lib64/graalvm/graalvm22-ee-java17"


# Set your OCI user OCID
USER_OCID="ocid1.user.oc1..aaaaaaaaxaiyxoz42fplddsx2m54huwuigvpbe6zjstsii6n7iquxiuggpuq"  # Replace with your actual user OCID

# Function to retrieve existing auth token
get_existing_token() {
  oci iam auth-token list --user-id "$USER_OCID" --query 'data[?contains("description", `docker-login`)].token' --raw-output
}

# Use the existing auth token
TOKEN=$(get_existing_token)
if [ -z "$TOKEN" ]; then
  echo "Error: No existing auth token found. Exiting."
  exit 1
fi
echo "Using existing auth token."
echo "Auth Token: $TOKEN"

# Get your OCI username
USERNAME=$(oci iam user get --user-id "$USER_OCID" --query "data.name" --raw-output)
if [ $? -ne 0 ]; then
  echo "Error retrieving OCI username. Exiting."
  exit 1
fi
echo "OCI Username: $USERNAME"

# Get the namespace
NAMESPACE=$(oci os ns get --query "data" --raw-output)
if [ $? -ne 0 ]; then
  echo "Error retrieving namespace. Exiting."
  exit 1
fi
echo "Namespace: $NAMESPACE"

# Docker login
echo "$TOKEN" | docker login -u "$NAMESPACE/$USERNAME" --password-stdin "mx-queretaro-1.ocir.io"
if [ $? -ne 0 ]; then
  echo "Error logging in to Docker. Exiting."
  exit 1
fi
echo "Docker login successful."

#echo "Logging in to Docker registry"
#docker login -u "axvfutv1sy8e/a00815371@tec.mx" --password-stdin "mx-queretaro-1.ocir.io"


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
