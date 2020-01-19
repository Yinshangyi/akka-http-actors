import CustomClasses._
import akka.actor.{Actor, ActorLogging}

class GameAreaMap extends Actor with ActorLogging {

  var players = Map[String, Player]()

  override def receive: Receive = {
    case GetAllPlayers => log.info("Getting all players")
      sender() ! players.values.toList

    case GetPlayer(nickname) =>
      log.info(s"Getting player with nickname $nickname")
      sender() ! players.get(nickname)

    case GetPlayersByClass(characterClass) =>
      log.info(s"Getting all players with the character class $characterClass")
      sender() ! players.values.toList.filter(_.characterClass == characterClass)

    case AddPlayer(player) =>
      log.info(s"Trying to add player $player")
      players = players + (player.nickname -> player)
      sender() ! OperationSuccess

    case RemovePlayer(player) =>
      log.info(s"Trying to remove $player")
      players = players - player.nickname
      sender() ! OperationSuccess
  }

}
