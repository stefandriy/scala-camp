package repository

import domain.User

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryFuture(implicit ec: ExecutionContext) extends UserRepository[Future] {
  private val storage: MutableMap[Long, User] = MutableMap()

  override def registerUser(username: String): Future[User] = Future {
    val id = nextId()
    val user = User(id, username)
    storage.put(id, user)
    user
  }

  override def getById(id: Long): Future[Option[User]] = Future(storage.get(id))

  override def getByUsername(username: String): Future[Option[User]] =
    Future(storage.values.find(user => user.username == username))
}
