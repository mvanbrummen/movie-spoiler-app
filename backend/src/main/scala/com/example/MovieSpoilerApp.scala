package com.example

import akka.actor.{ Actor, ActorLogging, ActorSystem, Props }
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import spray.json.DefaultJsonProtocol._

import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }
import scala.util.Random

object MovieSpoilerApp {

  case object GetSpoiler

  case class MovieSpoiler(movieTitle: String, spoiler: String)

  class SpoilerActor extends Actor with ActorLogging {

    val spoilers: List[MovieSpoiler] = List(
      MovieSpoiler("Harry Potter", "Dumbledore dies"),
      MovieSpoiler("Rocky II", "Rocky wins"),
      MovieSpoiler("The Sixth Sense", "Bruce Willis was dead the whole time")
    )

    def receive = {
      case GetSpoiler => sender ! spoilers
      case _ => log.info("Unknown message")
    }
  }

  implicit val movieSpoilerFormat = jsonFormat2(MovieSpoiler)

  def main(args: Array[String]) {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    val movieSpoilers = system.actorOf(Props[SpoilerActor], "movieSpoilers")

    val route =
      get {
        pathEndOrSingleSlash {
          getFromResource("build/index.html")
        } ~ {
          getFromResourceDirectory("build")
        }
      } ~
        path("spoiler") {
          get {
            implicit lazy val timeout = Timeout(5.seconds)

            val spoiler: Future[MovieSpoiler] = (movieSpoilers ? GetSpoiler).mapTo[MovieSpoiler]

            complete(spoiler)
          }
        }

    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)

    println(s"Server online at http://localhost:8080/...")

    Await.result(system.whenTerminated, Duration.Inf)
  }

}