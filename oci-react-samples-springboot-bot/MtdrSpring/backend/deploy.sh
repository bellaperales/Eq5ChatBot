#!/bin/bash
SCRIPT_DIR=$(pwd)

echo "Current directory: $SCRIPT_DIR"

# Manually set the database name using the name from your screenshot
TODO_PDB_NAME="eq5chatbotwu5ru"  # Set to the actual database name

# Ensure other environment variables are set
if [ -z "$OCI_REGION" ]; then
    echo "OCI_REGION not set. Will get it with state_get"
    export OCI_REGION=$(state_get REGION)
fi
if [ -z "$OCI_REGION" ]; then
    echo "Error: OCI_REGION env variable needs to be set!"
    exit 1
fi

# Set UI_USERNAME to TODOUSER directly
UI_USERNAME="TODOUSER"

# Print values of environment variables for debugging
echo "TODO_PDB_NAME: $TODO_PDB_NAME"
echo "OCI_REGION: $OCI_REGION"
echo "UI_USERNAME: $UI_USERNAME"

echo "Creating springboot deployment and service"
export CURRENTTIME=$(date '+%F_%H:%M:%S')
echo "CURRENTTIME is $CURRENTTIME ...this will be appended to generated deployment yaml"
cp src/main/resources/todolistapp-springboot.yaml todolistapp-springboot-$CURRENTTIME.yaml

# Using # as a delimiter in sed to avoid conflicts with URL paths
sed -i "s#%DOCKER_REGISTRY%#${DOCKER_REGISTRY}#g" todolistapp-springboot-$CURRENTTIME.yaml
sed -i "s#%TODO_PDB_NAME%#${TODO_PDB_NAME}#g" todolistapp-springboot-$CURRENTTIME.yaml
sed -i "s#%OCI_REGION%#${OCI_REGION}#g" todolistapp-springboot-$CURRENTTIME.yaml
sed -i "s#%UI_USERNAME%#${UI_USERNAME}#g" todolistapp-springboot-$CURRENTTIME.yaml

echo "Deployment YAML after substitution:"
cat todolistapp-springboot-$CURRENTTIME.yaml

if [ -z "$1" ]; then
    kubectl apply -f $SCRIPT_DIR/todolistapp-springboot-$CURRENTTIME.yaml -n mtdrworkshop
else
    kubectl apply -f <(istioctl kube-inject -f $SCRIPT_DIR/todolistapp-springboot-$CURRENTTIME.yaml) -n mtdrworkshop
fi