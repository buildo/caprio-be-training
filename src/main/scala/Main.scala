import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.io.StdIn
import api.controller.GameApiController
import api.controller.GameApiControllerImpl

import io.circe.generic.auto._, io.circe.syntax._
import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.ToHttpResponse
import wiro.server.akkaHttp.FailSupport._
import io.buildo.enumero.circe._
import akka.http.scaladsl.model.{ContentType, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.model.MediaTypes
import error.GameNotFoundError
import error.Error
import service.GameService
import service.GameServiceImpl
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import service.{CpuMoveStrategyImpl, GameEngineImpl}
import persistance.GameRepositoryImpl

object Main extends App with RouterDerivationModule {
  val config = Config("localhost", 8080)

  implicit val system = ActorSystem("rps-game")
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  implicit def errorToResponse = new ToHttpResponse[Error] {
    def response(error: Error) = error match {
      case GameNotFoundError =>
        HttpResponse(
          status = StatusCodes.NotFound,
          entity = HttpEntity(ContentType(MediaTypes.`application/json`), error.asJson.noSpaces)
        )
      case _ =>
        HttpResponse(
          status = StatusCodes.InternalServerError,
          entity = HttpEntity(ContentType(MediaTypes.`application/json`), error.asJson.noSpaces)
        )
    }
  }

  val gameService: GameService = new GameServiceImpl(
    new GameRepositoryImpl,
    new CpuMoveStrategyImpl(),
    new GameEngineImpl()
  )
  
  val usersRouter = deriveRouter[GameApiController](new GameApiControllerImpl(gameService))
  val customRoutes = cors() { usersRouter.buildRoute }

  val rpcServer = new HttpRPCServer(
    config = config,
    routers = List(usersRouter),
    customRoute = customRoutes
  )

  println(s"Server online at http://localhost:${config.port}/\nPress RETURN to stop...")
  StdIn.readLine()
  system.terminate()

}