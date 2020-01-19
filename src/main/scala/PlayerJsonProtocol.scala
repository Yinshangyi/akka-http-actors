import CustomClasses.Player
import spray.json.DefaultJsonProtocol

trait PlayerJsonProtocol extends DefaultJsonProtocol {
  implicit val playerFormat = jsonFormat3(Player)
}
