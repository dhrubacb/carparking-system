# carparking-system
This Spring Boot Application is for finding nearest available car parkings lots based on latitude and longitude. It elivates one API for finding this information.
API: http://localhost:8083/api/v1/carparks/nearest?longitude=102.897&page=0&per_page=3000&latitude=1.37326
It populates MySql DB from csv file and calls third party API for finding available lots.
# Build
mvn clean install
# Run unit tests
mvn test
# RUN
docker-compose up
java -jar target/carparking-system-0.0.1-SNAPSHOT.jar
