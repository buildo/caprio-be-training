package model

import command.Move

case class FinalResult(val userMove: Move, val computerMove: Move, val gameResult: GameResult)