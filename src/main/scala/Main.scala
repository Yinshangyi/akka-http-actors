import CustomClasses._
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App with RandomData with PlayerJsonProtocol with SprayJsonSupport {

  implicit val system = ActorSystem("VideoGameExercice")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher


  val gameMapActor = system.actorOf(Props[GameAreaMap])

  playersList.foreach(player => {
    gameMapActor ! AddPlayer(player)
  })

  /*
     - GET /api/player, returns all the players in the map, as JSON
     - GET /api/player/(nickname), returns the player with the given nickname (as JSON)
     - GET /api/player?nickname=X, does the same
     - GET /api/player/class/(charClass), returns all the players with the given character class
     - POST /api/player with JSON payload, adds the player to the map
     - (Exercise) DELETE /api/player with JSON payload, removes the player from the map
    */

  implicit val timeout = Timeout(2 seconds)
  val ApiRoute = {
      pathPrefix("api" / "player") {
        get {
          /* GET /api/player */
          path("class" / Segment) { characterClass =>
            val playerByClassFuture = (gameMapActor ? GetPlayersByClass(characterClass)).mapTo[List[Player]]
            complete(playerByClassFuture)
          } ~
            /* GET /api/player/(nickname) and  GET /api/player?nickname=X */
            (path(Segment) | parameter("nickname")) { nickname =>
              val playerOptionFuture = (gameMapActor ? GetPlayer(nickname)).mapTo[Option[Player]]
              complete(playerOptionFuture)
            } ~
            /* GET /api/player */
            pathEndOrSingleSlash {
              val playersOptionFuture = (gameMapActor ? GetAllPlayers).mapTo[List[Player]]
              complete(playersOptionFuture)
            }
        } ~
        /* POST /api/player */
        post {
          entity(as[Player]) { player =>
            complete((gameMapActor ? AddPlayer(player)).map(_ => StatusCodes.OK))
          }
        } ~
        /* DELETE /api/player */
        entity(as[Player]){ player =>
          complete((gameMapActor ? RemovePlayer(player)).map(_ => StatusCodes.OK))
        }
      }
    }

    Http().bindAndHandle(ApiRoute, "localhost", 8080)
    println("Server is starting...")

}
