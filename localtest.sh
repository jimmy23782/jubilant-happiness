./gradlew clean build -x test
docker build -t myorg/myservice:local .
docker run --rm -p 8080:8080 --name mysvc myorg/myservice:local
# test in another terminal:
curl -s http://localhost:8080/actuator/health || curl -s http://localhost:8080/health