package service

import command.Move
import command.Move._
import model.GameResult
import model.GameResult._

trait GameEngine {
  def evaluateWinner(userPlay: Move, cpuPlay: Move): GameResult
}

class GameEngineImpl extends GameEngine {
  def evaluateWinner(userPlay: Move, cpuPlay: Move): GameResult =
    (userPlay, cpuPlay) match {
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) => Win
      case (x, y) if x == y => Draw
      case _ => Lose
    }
}
