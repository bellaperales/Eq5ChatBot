#!/bin/bash

export DOCKER_REGISTRY="mx-queretaro-1.ocir.io/axvfutv1sy8e/eq5chatbot/wu5ru"
export TODO_PDB_NAME="eq5chatbotwu5ru"
export OCI_REGION="mx-queretaro-1"
export UI_USERNAME="TODOUSER"


SCRIPT_DIR=$(pwd)
if [ -z "$TODO_PDB_NAME" ]; then
    echo "TODO_PDB_NAME not set. Will get it with state_get"
  export TODO_PDB_NAME=$(state_get MTDR_DB_NAME)
fi
if [ -z "$TODO_PDB_NAME" ]; then
    echo "Error: TODO_PDB_NAME env variable needs to be set!"
    exit 1
fi
if [ -z "$OCI_REGION" ]; then
    echo "OCI_REGION not set. Will get it with state_get"
    export OCI_REGION=$(state_get REGION)
fi
if [ -z "$OCI_REGION" ]; then
    echo "Error: OCI_REGION env variable needs to be set!"
    exit 1
fi

if [ -z "$UI_USERNAME" ]; then
    echo "UI_USERNAME not set. Will get it with state_get"
  export UI_USERNAME=$(state_get UI_USERNAME)
fi

if [ -z "$UI_USERNAME" ]; then
    echo "Error: UI_USERNAME env variable needs to be set!"
    exit 1
fi

echo "Creating springboot deplyoment and service"
export CURRENTTIME=$( date '+%F_%H:%M:%S' )
echo CURRENTTIME is $CURRENTTIME  ...this will be appended to generated deployment yaml
cp src/main/resources/todolistapp-springboot.yaml todolistapp-springboot-$CURRENTTIME.yaml

echo "DOCKER_REGISTRY: $DOCKER_REGISTRY"
echo "TODO_PDB_NAME: $TODO_PDB_NAME"
echo "OCI_REGION: $OCI_REGION"
echo "UI_USERNAME: $UI_USERNAME"
echo "CURRENTTIME: $CURRENTTIME"

sed -i "s|%DOCKER_REGISTRY%|${DOCKER_REGISTRY}|g" todolistapp-springboot-$CURRENTTIME.yaml

sed -e "s|%DOCKER_REGISTRY%|${DOCKER_REGISTRY}|g" todolistapp-springboot-${CURRENTTIME}.yaml > /tmp/todolistapp-springboot-${CURRENTTIME}.yaml
mv -- /tmp/todolistapp-springboot-$CURRENTTIME.yaml todolistapp-springboot-$CURRENTTIME.yaml
sed -e "s|%TODO_PDB_NAME%|${TODO_PDB_NAME}|g" todolistapp-springboot-${CURRENTTIME}.yaml > /tmp/todolistapp-springboot-${CURRENTTIME}.yaml
mv -- /tmp/todolistapp-springboot-$CURRENTTIME.yaml todolistapp-springboot-$CURRENTTIME.yaml
sed -e "s|%OCI_REGION%|${OCI_REGION}|g" todolistapp-springboot-${CURRENTTIME}.yaml > /tmp/todolistapp-springboot-$CURRENTTIME.yaml
mv -- /tmp/todolistapp-springboot-$CURRENTTIME.yaml todolistapp-springboot-$CURRENTTIME.yaml
sed -e "s|%UI_USERNAME%|${UI_USERNAME}|g" todolistapp-springboot-${CURRENTTIME}.yaml > /tmp/todolistapp-springboot-$CURRENTTIME.yaml
mv -- /tmp/todolistapp-springboot-$CURRENTTIME.yaml todolistapp-springboot-$CURRENTTIME.yaml

chmod +x /workspace/Eq5ChatBot/oci-react-samples-springboot-bot/MtdrSpring/utils/kube_token_cache.sh

if [ -z "$1" ]; then
    kubectl apply -f $SCRIPT_DIR/todolistapp-springboot-$CURRENTTIME.yaml -n mtdrworkshop
else
    kubectl apply -f <(istioctl kube-inject -f $SCRIPT_DIR/todolistapp-springboot-$CURRENTTIME.yaml) -n mtdrworkshop
fi
