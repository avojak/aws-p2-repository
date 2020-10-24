FROM openjdk:8-jre

# Set the working directory
RUN mkdir -p /opt/app/
WORKDIR /opt/app/

# Get updates
RUN apt-get update && apt-get upgrade -y

# Copy the Spring Boot JAR to /opt/app/
COPY aws-p2-repository-webapp/target/aws-p2-repository-webapp-1.0.1.jar .

# Set entrypoint script
COPY docker-entrypoint.sh /usr/local/bin/
RUN ["chmod", "+x", "/usr/local/bin/docker-entrypoint.sh"]
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]