package repository

import domain.User
import schema.UserTable
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

class UserRepository(db: Database, users: TableQuery[UserTable]) {

  def save(user: User): Future[Option[User]] = {
    db.run(users += user)
    findByUsername(user.username)
  }

  def findById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  def findByUsername(username: String): Future[Option[User]] =
    db.run(users.filter(_.username === username).result.headOption)
}