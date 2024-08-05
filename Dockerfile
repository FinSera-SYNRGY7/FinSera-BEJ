FROM openjdk:17-jdk-alpine

RUN apk update && apk upgrade \
   && apk add --no-cache ttf-dejavu \
   && apk add --no-cache msttcorefonts-installer \
   && update-ms-fonts && fc-cache -f \
   && apt-get install -y tzdata \
   && ln -fs /usr/share/zoneinfo/Asia/Jakarta /etc/localtime \
   && dpkg-reconfigure


WORKDIR /app


COPY target/finsera-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080


ENTRYPOINT ["java","-jar","app.jar"]
