package game

import scala.util.Random
import command.Command
import command.Command._
import command.Move
import command.Move._
import cats.data._
import io.buildo.enumero._
import model.GameResult
import model.GameResult._
import model.FinalResult

object Game {

  val moveSerialization = CaseEnumSerialization.apply[Move]

  def provideCPUMove(): Move = Random.shuffle(moveSerialization.values).head

  private def evaluateWinner(userPlay: Move, cpuPlay: Move): GameResult =
    (userPlay, cpuPlay) match {
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => Win
      case (x, y) if x == y => Draw
      case _ => Lose
    }

  def play(userMove: Move): FinalResult = {
    val cpuMove = provideCPUMove()
    val matchResult = evaluateWinner(userMove, cpuMove)

    new FinalResult(
      userMove,
      cpuMove,
      matchResult
    )
  }

}
