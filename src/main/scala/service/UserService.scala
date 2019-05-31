package service

import domain.User
import repository.UserRepository
import util.Retrier

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class UserService(repository: UserRepository)(implicit val ec: ExecutionContext) {

  def registerUser(username: String, address: Option[String], email: String): Future[Either[String, User]] =

    repository.findByUsername(username).flatMap {
      case Some(_) => Future.successful(Left(s"User with username $username already exists"))
      case None => retry(repository.save(User(0, username, address, email)))
        .map {
          case Some(savedUser) => Right(savedUser)
          case None => Left("Something went wrong")
        }
    }

  def findById(id: Long): Future[Option[User]] = repository.findById(id)

  private def retry(future: Future[Option[User]]): Future[Option[User]] =
    Retrier.retry(() => future, (o: Option[User]) => o.isDefined, List(1.second, 2.seconds, 3.seconds))
}
