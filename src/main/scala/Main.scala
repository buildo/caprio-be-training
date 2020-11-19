import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn
import scala.concurrent.ExecutionContext
import api.request.PlayRequest
import api.response.FinalResultResponse
import game.Game._
import game.Game

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.buildo.enumero.circe._
import io.circe.generic.auto._, io.circe.syntax._

object Main extends App {
  val port = 8080

  implicit val system = ActorSystem(Behaviors.empty, "rps-game")
  implicit val executionContext = system.executionContext

  val route =
    pathPrefix("rps") {
      path("play") {
        post {
          entity(as[PlayRequest]) { request =>
            {
              val gameOutput = Game.play(request.userMove)
              val apiResponse = FinalResultResponse(gameOutput.userMove, gameOutput.computerMove, gameOutput.gameResult)
              complete(apiResponse.asJson)
            }
          }
        }
      }
    }

  val bindingFuture = Http().newServerAt("localhost", port).bind(route)

  println(s"Server online at http://localhost:${port}/\nPress RETURN to stop...")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
