FROM eclipse-temurin:17-jre-alpine
COPY /target/browser.jar /browser.jar
ENTRYPOINT ["java","-jar","/browser.jar"]