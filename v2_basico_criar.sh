#!/bin/bash

if [ "$1" = "rm" ]; then
    echo "Removendo imagem..."
    docker stop myapp2
    docker rm myapp2
    docker image rm daniel/myapp2
    docker image prune
    docker images
    exit 1
fi

cd springBootDocker

mvn clean && mvn package

cd ..

ls

docker build -f Dockerfile.basicv2 -t daniel/myapp2 .

docker images


if [ "$1" = "run" ]; then
    echo "Rodando imagem..."
    docker run --name myapp2 -p 8290:8290 daniel/myapp2
fi
