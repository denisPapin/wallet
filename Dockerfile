FROM openjdk:21
ARG jarFile=target/walletDemoProject-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${jarFile} wallet.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "wallet.jar"]
