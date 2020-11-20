package api.controller

import scala.concurrent.Future
import wiro.annotation._
import _root_.command.Move
import model.FinalResult
import game.Game
import scala.concurrent.ExecutionContext.Implicits.global

@path("rps")
trait GameApi {

  @command
  def play(userMove: Move): Future[Either[Throwable, FinalResult]]
}

class GameApiImpl() extends GameApi {

  override def play(userMove: Move): Future[Either[Throwable, FinalResult]] =
    Future(Right(Game.play(userMove)))

}
