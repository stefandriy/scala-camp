package service

import domain.User
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import repository.UserRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class UserServiceSpec extends FlatSpec with Matchers with MockFactory {

  "UserServiceFuture" should "save and retrieve User" in {
    val id = 0
    val username = "username"
    val email = "user@email.test"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val userRepository = stub[UserRepository]
    val userService = new UserService(userRepository)
    val user = User(id, username, None, email)
    (userRepository.save _).when(user).returns(Future.successful(Option(user)))
    (userRepository.findById _).when(id).returns(Future.successful(Option(user)))
    (userRepository.findById _).when(wrongId).returns(Future.successful(Option.empty))
    (userRepository.findByUsername _).when(username).returns(Future.successful(Option.empty))
    (userRepository.findByUsername _).when(wrongUsername).returns(Future.successful(Option(user)))

    val savedUser = await(userService.registerUser(username, None, email)).right.get
    savedUser.username shouldEqual username
    savedUser.email shouldEqual email
    await(userService.findById(savedUser.id)) shouldEqual Option(savedUser)
    await(userService.registerUser(wrongUsername, None, email)).left.get shouldEqual
      s"User with username $wrongUsername already exists"
    await(userService.findById(wrongId)) shouldEqual Option.empty
  }

  private def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
