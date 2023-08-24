#Start with a base image containing Java runtime
FROM openjdk:20-jdk-slim as build

#Information around who maintains the image
MAINTAINER fabrix.com

# Add the application's jar to the container
COPY target/hotels-1.0.jar hotels-1.0.jar

#execute the application
ENTRYPOINT ["java","-jar","/hotels-1.0.jar"]