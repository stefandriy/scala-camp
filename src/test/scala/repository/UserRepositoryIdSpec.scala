package repository

import org.scalatest._

class UserRepositoryIdSpec extends FlatSpec with Matchers {

  "UserRepositoryId" should "save and retrieve User" in {
    val username = "username"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val userRepository = new UserRepositoryId
    val savedUser = userRepository.registerUser(username)
    savedUser.username shouldEqual username
    userRepository.getById(savedUser.id) shouldEqual Option(savedUser)
    userRepository.getByUsername(username) shouldEqual Option(savedUser)
    userRepository.getById(wrongId) shouldEqual Option.empty
    userRepository.getByUsername(wrongUsername) shouldEqual Option.empty
  }
}
