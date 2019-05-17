package rest

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object UserRoutes {
  val routes: Route =
    path("user") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to user</h1>"))
      }
    } ~
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      }
  //  pathPrefix("user") {
  //      path("") {
  //    get {
  //      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to user</h1>"))
  //        }
  //    }
  //    path("hello") {
  //      get {
  //        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
  //      }
  //    }
  //  }
}