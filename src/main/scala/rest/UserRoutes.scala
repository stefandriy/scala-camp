package rest

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import service.UserService
import validation.Validator
import validation.Validator.nonEmpty

class UserRoutes(userService: UserService) extends JsonSupport {

  private val userValidator: Validator[NewUser] = user => {
    val validUsername = nonEmpty.validate(user.username)
    if (validUsername.isLeft) Left(s"username ${validUsername.left.get}")
    else Right(user)
  }

  val routes: Route =
    pathPrefix("users") {
      post {
        entity(as[NewUser]) { user =>
          complete(userValidator.validate(user)
            .map(_ => userService.registerUser(user.username, user.address, user.email)))
        }
      } ~
        get {
          parameters("id".as[Long]) { id =>
            complete {
              userService.findById(id)
            }
          }
        }
    }
}