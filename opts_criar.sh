#!/bin/bash

if [ "$1" = "rm" ]; then
    echo "Removendo imagem..."
    docker stop myapp3
    docker rm myapp3
    docker image rm daniel/myapp3
    docker image prune
    docker images
    exit 1
fi

cd springBootDocker

mvn clean && mvn package

cd ..

ls

docker build -f Dockerfile.comShell -t daniel/myapp3 .

docker images


if [ "$1" = "run" ]; then
    echo "Rodando imagem..."
    docker run --name myapp3 -p 8290:8290 daniel/myapp3
fi
