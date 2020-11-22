package service

import scala.util.Random
import command.Command
import command.Command._
import command.Move
import command.Move._
import io.buildo.enumero._
import model.GameResult
import model.GameResult._
import model.FinalResult
import persistance.GameRepository
import persistance.GameRepositoryImpl
import persistance.GameStatusEntity
import error.GameNotFoundError
import error.Error

trait GameService {
  def play(userMove: Move): FinalResult
  def getLastGameResult(): Either[Error, FinalResult]
}

class GameServiceImpl extends GameService {

  val gameRepository: GameRepository = new GameRepositoryImpl()
  val moveSerialization = CaseEnumSerialization.apply[Move]

  private def provideCPUMove(): Move = Random.shuffle(moveSerialization.values).head

  private def evaluateWinner(userPlay: Move, cpuPlay: Move): GameResult =
    (userPlay, cpuPlay) match {
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => Win
      case (x, y) if x == y => Draw
      case _ => Lose
    }

  override def play(userMove: Move): FinalResult = {
    val cpuMove = provideCPUMove()
    val matchResult = evaluateWinner(userMove, cpuMove)

    gameRepository.insertGameResult(userMove, cpuMove, matchResult)

    new FinalResult(
      userMove,
      cpuMove,
      matchResult
    )
  }

  override def getLastGameResult(): Either[Error, FinalResult] = {
    val allGamesResult = gameRepository.getAllGameResultsSortedAsc()

    Either.cond(!allGamesResult.isEmpty, {
      val res = allGamesResult.last
      new FinalResult(res.userMove, res.cpuMove, res.gameResult)
    }, GameNotFoundError)
  }

}
