package repository

import domain.User
import org.scalatest._
import schema.UserTable
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class UserRepositorySpec extends FlatSpec with Matchers {

  private val db = Database.forConfig("scala-camp")
  private val users = TableQuery[UserTable]
  private val userRepository = new UserRepository(db, users)
  await(db.run(users.schema.dropIfExists))
  await(db.run(users.schema.create))

  "UserRepository" should "save and retrieve User" in {
    val username = "username"
    val email = "email@mail.test"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val savedUser = await(userRepository.save(User(0, username, None, email))).get
    savedUser.username shouldEqual username
    savedUser.email shouldEqual email
    await(userRepository.findById(savedUser.id)) shouldEqual Option(savedUser)
    await(userRepository.findByUsername(username)) shouldEqual Option(savedUser)
    await(userRepository.findById(wrongId)) shouldEqual Option.empty
    await(userRepository.findByUsername(wrongUsername)) shouldEqual Option.empty
  }

  private def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
