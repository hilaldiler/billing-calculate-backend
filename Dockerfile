FROM openjdk:17

COPY target/billing-0.0.1-SNAPSHOT.jar /sernoffers.jar

ENTRYPOINT ["java","-jar","/sernoffers.jar"]