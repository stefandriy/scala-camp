package schema

import domain.User
import slick.jdbc.H2Profile.api._

class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def username = column[String]("USERNAME")

  def address = column[Option[String]]("ADDRESS")

  def email = column[String]("EMAIL")

  def * = (id, username, address, email).mapTo[User]
}
