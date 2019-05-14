package service

import cats.implicits._
import org.scalatest._
import repository.{UserRepositoryFuture, UserRepositoryId}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class UserServiceSpec extends FlatSpec with Matchers {

  "UserServiceId" should "save and retrieve User" in {
    val username = "username"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val userService = new UserService(new UserRepositoryId)
    val savedUser = userService.registerUser(username).right.get
    savedUser.username shouldEqual username
    userService.getById(savedUser.id) shouldEqual Option(savedUser)
    userService.getByUsername(username) shouldEqual Option(savedUser)
    userService.registerUser(username).left.get shouldEqual s"User $savedUser already exists."
    userService.getById(wrongId) shouldEqual Option.empty
    userService.getByUsername(wrongUsername) shouldEqual Option.empty
  }

  "UserServiceFuture" should "save and retrieve User" in {
    val username = "username"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val userService = new UserService(new UserRepositoryFuture)
    val savedUser = await(userService.registerUser(username)).right.get
    savedUser.username shouldEqual username
    await(userService.getById(savedUser.id)) shouldEqual Option(savedUser)
    await(userService.getByUsername(username)) shouldEqual Option(savedUser)
    await(userService.registerUser(username)).left.get shouldEqual s"User $savedUser already exists."
    await(userService.getById(wrongId)) shouldEqual Option.empty
    await(userService.getByUsername(wrongUsername)) shouldEqual Option.empty
  }

  private def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
