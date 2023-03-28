# Use the official Java image as the base image
From azul/zulu-openjdk-centos:17-jre-latest

COPY ./${JAR_FILE} marketdataservice.jar

# Run the jar file when the container starts
ENTRYPOINT ["java", "-jar", "marketdataservice.jar"]
