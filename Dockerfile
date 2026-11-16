FROM eclipse-temurin:25-jre-alpine
RUN apk add --no-cache bash
COPY target/browser.jar /browser.jar
ENTRYPOINT ["java","-jar","/browser.jar"]