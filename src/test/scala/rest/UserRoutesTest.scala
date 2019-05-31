package rest

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import domain.User
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import repository.UserRepository
import schema.UserTable
import service.UserService
import slick.jdbc.H2Profile.api._
import spray.json._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class UserRoutesTest extends WordSpec with Matchers with BeforeAndAfterEach with ScalatestRouteTest with JsonSupport {

  private implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(10.seconds)

  private val db = Database.forConfig("scala-camp")
  private val users = TableQuery[UserTable]
  private val userRepository = new UserRepository(db, users)
  private val userRoutes = new UserRoutes(new UserService(userRepository)).routes

  private var user1: User = _
  private val user2 = User(2, "Second User", Option.empty, "second@email.com")

  override def beforeEach(): Unit = {
    await(db.run(users.schema.dropIfExists))
    await(db.run(users.schema.create))
    user1 = await(userRepository.save(User(0, "Existing User", Option("DB"), "first@email.com"))).get
  }

  override def afterEach(): Unit = await(db.run(users.schema.dropIfExists))

  "GET" should {
    "return first user" in {
      Get("/users?id=1") ~> userRoutes ~> check {
        responseAs[User] shouldEqual user1
      }
    }
  }

  "POST" should {
    "create second user" in {
      Post("/users").withEntity(`application/json`, user2.toJson.toString) ~> userRoutes ~> check {
        responseAs[User] shouldEqual user2
      }
    }
  }

  "POST" should {
    "return message that user already exists" in {
      Post("/users").withEntity(`application/json`, user2.toJson.toString) ~> userRoutes
      Post("/users").withEntity(`application/json`, user2.toJson.toString) ~> userRoutes ~> check {
        responseAs[String] shouldEqual s"User with username ${user2.username} already exists"
      }
    }
  }

  "POST" should {
    "return message about incorrect empty username" in {
      val userWithoutUsername = User(0, "", None, "test")
      Post("/users").withEntity(`application/json`, userWithoutUsername.toJson.toString) ~> userRoutes ~> check {
        responseAs[String] shouldEqual "username should not be empty"
      }
    }
  }

  private def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
