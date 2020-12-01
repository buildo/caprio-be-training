package service

import command.Move
import command.Move._
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

class GameServiceImpl(gameRepository: GameRepository, cpuMoveStrategy: CpuMoveStrategy, gameEngine: GameEngine) extends GameService {

  override def play(userMove: Move): FinalResult = {
    val cpuMove = cpuMoveStrategy.provideCPUMove()
    val matchResult = gameEngine.evaluateWinner(userMove, cpuMove)

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
