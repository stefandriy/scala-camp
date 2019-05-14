package repository

import domain.User

trait UserRepository[F[_]] extends Repository {
  def registerUser(username: String): F[User]

  def getById(id: Long): F[Option[User]]

  def getByUsername(username: String): F[Option[User]]
}
