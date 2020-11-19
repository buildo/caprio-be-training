import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn
import scala.concurrent.ExecutionContext

object Main extends App {

  implicit val system = ActorSystem(Behaviors.empty, "rps-game")
  implicit val executionContext = system.executionContext

  val route =
    path("start") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Start to Play Rock - Paper - Scissor</h1>"))
      }
    }

  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
