package service

import cats.Monad
import cats.implicits._
import domain.User
import repository.UserRepository

class UserService[F[_]](repository: UserRepository[F])
                       (implicit monad: Monad[F]) {

  def registerUser(username: String): F[Either[String, User]] = {
    repository.getByUsername(username).flatMap({
      case Some(user) => monad.pure(Left(s"User $user already exists."))
      case None => repository.registerUser(username).map(Right(_))
    })
  }

  def getByUsername(username: String): F[Option[User]] = repository.getByUsername(username)

  def getById(id: Long): F[Option[User]] = repository.getById(id)
}
