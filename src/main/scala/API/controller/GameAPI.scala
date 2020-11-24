package api.controller

import scala.concurrent.Future
import wiro.annotation._
import model.FinalResult
import _root_.command.Move
import scala.concurrent.ExecutionContext
import service.GameService
import error.GameNotFoundError
import error.Error

@path("rps")
trait GameApiController {

  @command
  def play(userMove: Move): Future[Either[Error, FinalResult]]

  @query
  def result(): Future[Either[Error, FinalResult]]
}

class GameApiControllerImpl (gameService: GameService)(implicit ec: ExecutionContext) extends GameApiController {

  override def play(userMove: Move): Future[Either[Error, FinalResult]] = Future(Right(gameService.play(userMove)))

  override def result(): Future[Either[Error, FinalResult]] =
    Future {
      gameService
        .getLastGameResult()
        .left
        .map(_ => GameNotFoundError)
    }

}
