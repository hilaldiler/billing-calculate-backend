FROM openjdk:17

COPY target/billing-0.0.1-SNAPSHOT.jar /sernoffers.jar

CMD ["java","-jar","/sernoffers.jar"]