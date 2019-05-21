package rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import domain.User
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat: RootJsonFormat[User] = jsonFormat4(User)
  implicit val newUserFormat: RootJsonFormat[NewUser] = jsonFormat3(NewUser)
}
