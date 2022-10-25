docker-compose down
mvn build -Dmaven.test.skip=true
docker-compose build
docker-compose up