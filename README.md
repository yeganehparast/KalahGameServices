**KALAH!**

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

**Game Requirements**

Each of the two players has ** six pits ** in front of him/her. To the right of the six pits, each player has a larger pit, his Kalah or house.
At the start of the game, six stones are put in each pit. The player who begins picks up all the stones in any of their own pits, and sows the 
stones on to the right, one in each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah. If the players 
last stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other player's turn.

When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the
other players' pit) and puts them in his own Kalah.

The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps them and puts them in his/hers
Kalah. The winner of the game is the player who has the most stones in his Kalah.

#### Endpoint design specification
1. Creation of the game should be possible with the command:
curl --header "Content-Type: application/json" \
--request POST \
http://<host>:<port>/games
Response:
HTTP code: 201
Response Body: { "id": "1234", "uri": "http://<host>:<port>/games/1234" }
id: unique identifier of a game
url: link to the game created
2. Make a move:
curl --header "Content-Type: application/json" \
--request PUT \
http://<host>:<port>/games/{gameId}/pits/{pitId}
gameId: unique identifier of a game
pitId: id of the pit selected to make a move. Pits are numbered from 1 to 14 where 7 and 14 are the kalah (or house)
of each player
Response:
HTTP code: 200
Response Body:
{"id":"1234","url":"http://<host>:<port>/games/1234","status":{"1":"4","2":"4","3":"4","4":"4","5":"4","6":"4","7":"0","8":"4","
9":"4","10":"4","11":"4","12":"4","13":"4","14":"0"}}
status: json object key-value, where key is the pitId and value is the number of stones in the pit
