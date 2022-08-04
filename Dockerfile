FROM openjdk:8
VOLUME /tmp
EXPOSE 9083
ADD ./target/ms-account-0.0.1-SNAPSHOT.jar ms-account.jar
ENTRYPOINT ["java","-jar","/ms-account.jar"]