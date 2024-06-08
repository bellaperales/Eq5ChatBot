#!/bin/bash

export IMAGE_NAME=todolistapp-springboot
export IMAGE_VERSION=0.1

export JAVA_HOME="/usr/lib64/graalvm/graalvm22-ee-java17"


# Check if MTDRWORKSHOP_LOCATION is set
if test -z "$MTDRWORKSHOP_LOCATION"; then
  echo "ERROR: this script requires MTDRWORKSHOP_LOCATION to be set"
  exit 1
fi

# Function to check if a state is done
state_done() {
  test -f "$MTDRWORKSHOP_LOCATION/state/$1"
}

# Function to set a state as done
state_set_done() {
  touch "$MTDRWORKSHOP_LOCATION/state/$1"
}

# Function to set a state with a value
state_set() {
  echo "$2" > "$MTDRWORKSHOP_LOCATION/state/$1"
}

# Function to get a state value
state_get() {
  if state_done "$1"; then
    cat "$MTDRWORKSHOP_LOCATION/state/$1"
  fi
}

# Identify Run Type
while ! state_done RUN_TYPE; do
  state_set RUN_TYPE "1"
done

# Get the User OCID
while ! state_done USER_OCID; do
  if test -z "$TEST_USER_OCID"; then
    read -p "Please enter your OCI user's OCID: " USER_OCID
  else
    USER_OCID=$TEST_USER_OCID
  fi
  if test "$(oci iam user get --user-id "$USER_OCID" --query 'data."lifecycle-state"' --raw-output 2>/dev/null)" == 'ACTIVE'; then
    state_set USER_OCID "$USER_OCID"
  else
    echo "That user OCID could not be validated"
    exit 1
  fi
done

while ! state_done USER_NAME; do
  USER_NAME=$(oci iam user get --user-id "$(state_get USER_OCID)" --query "data.name" --raw-output)
  state_set USER_NAME "$USER_NAME"
done

# Get the tenancy OCID
while ! state_done TENANCY_OCID; do
  state_set TENANCY_OCID "$OCI_TENANCY"
done

# Set the region
while ! state_done REGION; do
  state_set REGION "$OCI_REGION"
done

# Get Namespace
while ! state_done NAMESPACE; do
  NAMESPACE=$(oci os ns get --query "data" --raw-output)
  state_set NAMESPACE "$NAMESPACE"
done

# Login to Docker
while ! state_done DOCKER_REGISTRY; do
  if ! TOKEN=$(oci iam auth-token create --user-id "$(state_get USER_OCID)" --description 'mtdr docker login' --query 'data.token' --raw-output 2>/dev/null); then
    echo "Error creating auth token. Exiting."
    exit 1
  fi

  if echo "$TOKEN" | docker login -u "$(state_get NAMESPACE)/$(state_get USER_NAME)" --password-stdin "$(state_get REGION).ocir.io"; then
    echo "Docker login completed"
    state_set DOCKER_REGISTRY "$(state_get REGION).ocir.io/$(state_get NAMESPACE)/$(state_get RUN_NAME)"
    export OCI_CLI_PROFILE=$(state_get REGION)
    break
  else
    echo "Docker login failed. Exiting."
    exit 1
  fi
done

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
