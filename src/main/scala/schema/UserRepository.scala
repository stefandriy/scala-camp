package schema

import domain.User
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

class UserRepository {

  val db = Database.forConfig("scala-camp")

  lazy val users = TableQuery[UserTable]

  def save(user: User): Future[Int] = db.run(users.insertOrUpdate(user))

  def getAll(): Future[Seq[User]] = db.run(users.result)
}