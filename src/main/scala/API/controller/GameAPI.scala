package api.controller

import scala.concurrent.Future
import wiro.annotation._
import model.FinalResult
import game.Game
import _root_.command.Move
import scala.concurrent.ExecutionContext

@path("rps")
trait GameApi {

  @command
  def play(userMove: Move): Future[Either[Throwable, FinalResult]]
}

class GameApiImpl(implicit ec:ExecutionContext) extends GameApi {
  override def play(userMove: Move): Future[Either[Throwable, FinalResult]] =
    Future(Right(Game.play(userMove)))

}
