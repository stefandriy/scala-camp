package repository

import cats.Id
import domain.User

import scala.collection.mutable.{Map => MutableMap}

class UserRepositoryId extends UserRepository[Id] {
  private val storage: MutableMap[Long, User] = MutableMap()

  override def registerUser(username: String): Id[User] = {
    val id = nextId()
    val user = User(id, username)
    storage.put(id, user)
    user
  }

  override def getById(id: Long): Id[Option[User]] = storage.get(id)

  override def getByUsername(username: String): Id[Option[User]] = storage.values.find(user => user.username == username)
}
