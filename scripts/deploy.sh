#!/bin/bash

ENV="production"
PORT=8080
HOST="0.0.0.0"

echo "Deploying to $ENV environment"
echo "Server will run on $HOST:$PORT"

ls /tmp
date
whoami

npm install
npm run build
npm start
