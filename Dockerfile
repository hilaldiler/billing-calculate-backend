FROM openjdk:17

COPY billing-0.0.1-SNAPSHOT.jar /sernoffers.jar

ENTRYPOINT ["java","-jar","/sernoffers.jar"]