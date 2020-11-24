import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.io.StdIn
import api.controller.GameApi
import api.controller.GameApiImpl

import io.circe.generic.auto._, io.circe.syntax._
import wiro.Config
import wiro.server.akkaHttp._
import wiro.server.akkaHttp.ToHttpResponse
import wiro.server.akkaHttp.FailSupport._
import io.buildo.enumero.circe._

object Main extends App with RouterDerivationModule {
  val config = Config("localhost", 8080)

  implicit val system = ActorSystem("rps-game")
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  implicit def throwableResponse: ToHttpResponse[Throwable] = null

  val usersRouter = deriveRouter[GameApi](new GameApiImpl())

  val rpcServer = new HttpRPCServer(
    config = config,
    routers = List(usersRouter)
  )

  println(s"Server online at http://localhost:${config.port}/\nPress RETURN to stop...")
  StdIn.readLine()
  system.terminate()

}