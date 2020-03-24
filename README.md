# myRetail Rest API
A project that stands up a Spring Boot Rest API that performs GET, PUT, POST, and DELETE operations.  
The database that gets stood up for the application is a Mongo database in a Docker container.  
Using Spring annotations, a SwaggerUI is stood up that has request and response specifications.  
SwaggerUI makes it easy to interact with APIs while providing the specification at the same time.  
Of course, SoapUI, Postman, or Insomnia can be used, but why bother taking the time?  

![SwaggerUI](https://github.com/code-cave/rest-api-poc/blob/master/docs/img/swagger_ui.png)

---
## Running The Application
For this first way to stand up the application, Java 11 is required!  
To run the application, simply enter these commands from the command line:
```sh
$ mvn clean install
$ docker-compose up
```
Then head over to http://localhost:8444/swagger-ui.html and start playing around!  

If you do not have docker-comnpose, simply go [here](https://docs.docker.com/compose/install/) and install it.  

If your Maven installation is not working properly, included in the repo is the app.jar file.  
To run the application using the provided jar, run this command:
```sh
$ docker-compose -f docker-compose-alt.yml up
```

---
## Application Tests And Code Coverage
The tests are made using Spock. Not sure what that is? Go [here](http://spockframework.org/spock/docs/1.3/spock_primer.html).  
Over 120 tests were written, with 100% code coverage on all packages except for the model package.  
The model package basically just includes Avro classes and POJOs used for creating the request  
and response models seen in SwaggerUI so it is not pertinent that they be tested.  
Coverage was calculated by IntelliJ, obviously in the real world, results would be uploaded to  
a code scanning tool such as SonarQube, but for now we just get a screenshot.
![SwaggerUI](https://github.com/code-cave/rest-api-poc/blob/master/docs/img/code_coverage.png)
