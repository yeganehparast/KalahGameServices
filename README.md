**KALAh!**

**Design**

This is a SpringBoot application to provide the required endpoints for playing the Kalah! game. In design of application
was tried to adhere Micro Services architectural style. Application has a simulated in-memory data base which can be found
in the `dao` package. It uses a ConcurrentMap. The logic of game has been encapsulated in `logic` package. A layer named 
service is responsible to relate the logic to the controller package. Spring HATEOS and Swagger were used to create links
and exploiting MicroServices design principles respectively. 

**Test**

Unit tests were provided to test logic, service and controller layers of the application which cover different kind of
tests like mocking unit test, integration tests and acceptance test.

**Execution**
`mvn clean install `
`java -jar assignment-1.0.jar`

