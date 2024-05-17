# springBootDocker

Docker de inicialização Spring. Veja o [doc](https://spring.io/guides/topicals/spring-boot-docker).

## Um Dockerfile básico

Arquivo: `Dockerfile`

```
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Criando a imagem:

```
docker build -f Dockerfole.basic --build-arg JAR_FILE=springBootDocker/target/*.jar -t daniel/myapp .
```

### Comando para o docker básico

Usando a shell `basico_criar.sh`, temos:

- Nenhum parâmetro: Cria a imagem
- `rm` remove a imagem
- `run` cria a imagem e roda o conatainer

Exemplo: `./basico_criar.sh rm`

Endereço da aplicação: [http://localhost:8290/](http://localhost:8290/)

### Comando para o docker básico v2

O básico v2 é uma versão que o jar está dentro do Dockerfile, sem para parâmetro.

Usando a shell `v2_basico_criar.sh`, temos:

- Nenhum parâmetro: Cria a imagem
- `rm` remove a imagem
- `run` cria a imagem e roda o conatainer

Exemplo: `./v2_basico_criar.sh rm`

Endereço da aplicação: [http://localhost:8290/](http://localhost:8290/)


## O ponto de entrada

O formato exec do Dockerfile ENTRYPOINTé usado para que não haja shell envolvendo o processo Java. A vantagem é que o processo java responde aos KILLsinais enviados ao contêiner. Na prática, isso significa (por exemplo) que, se você criar docker runsua imagem localmente, poderá interrompê-la com CTRL-C. Se a linha de comando ficar um pouco longa, você pode extraí-la em um script de shell e COPYna imagem antes de executá-la. 

```
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY ./script/run.sh .
EXPOSE 8290
COPY springBootDocker/target/*.jar app.jar
ENTRYPOINT ["run.sh"]
```

Execute `shell_criar.sh run` para criar rodar o container com o exemplo cima.

# Injetar variáveis

```
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY ./script/run.sh .
EXPOSE 8290
COPY springBootDocker/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]
```

Criando a imagem `docker build -f Dockerfile.opts -t daniel/myapp4 .` e executando o container: `docker run -p 8290:8290 -e "JAVA_OPTS=-Ddebug -Xmx128m" daniel/myapp4`

Usar um ENTRYPOINTcom um shell explícito (como faz o exemplo anterior) significa que você pode passar variáveis ​​de ambiente para o comando Java.

#### Passsando para o processo Java, argumentos

```
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY ./script/run.sh .
EXPOSE 8290
COPY springBootDocker/target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
```

Passando parâmetro para o spring boot: `docker run -p 8290:8290 daniel/myapp5 --server.port=8290`

Exemplo se usar script:

```shell
#!/bin/sh
exec java ${JAVA_OPTS} -jar /app.jar ${@}
```

