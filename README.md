# Concurrent Rest API in Scala with Akka
Simple Stateful Rest API with Akka-HTTP using Actors

This project is a simple API developed with akka-http in Scala. It has been inspired from an exercice from a Scala training program.  
The sample data used is stored in the JVM with Akka Actors. 
The API support the following routes:
- GET /api/player, returns all the players in the map, as JSON
- GET /api/player/(nickname), returns the player with the given nickname (as JSON)
- GET /api/player?nickname=X, does the same
- GET /api/player/class/(charClass), returns all the players with the given character class
 - POST /api/player with JSON payload, adds the player to the map
 - DELETE /api/player with JSON payload, removes the player from the map
 
