steps for running

git pull

install mysql server and run on port 3306(defualt)

open eclipse and add external jar file named lombok.jar

run backend\src\main\java\com\cloudrand\arcapi\ArcapiApplication.java

if everything is fine server will run on port 8080

potential issues

- wifi off
- sql server not working
- bad api request (only
  post request to http://localhost:8080/api/users/register works with {"username": "john_doe", "email": "john@example.com", "password": "password123"} as payload )
