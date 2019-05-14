package repository

import org.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class UserRepositoryFutureSpec extends FlatSpec with Matchers {

  "UserRepositoryFuture" should "save and retrieve User" in {
    val username = "username"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val userRepository = new UserRepositoryFuture
    val savedUser = await(userRepository.registerUser(username))
    savedUser.username shouldEqual username
    await(userRepository.getById(savedUser.id)) shouldEqual Option(savedUser)
    await(userRepository.getByUsername(username)) shouldEqual Option(savedUser)
    await(userRepository.getById(wrongId)) shouldEqual Option.empty
    await(userRepository.getByUsername(wrongUsername)) shouldEqual Option.empty
  }

  private def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
