package game

import scala.util.Random
import command.Command
import command.Command._
import command.Move
import command.Move._
import error.Error
import error.InputParsingError
import error.Printable
import validation.InputValidator
import validation.UserInputData
import cats.data._
import io.buildo.enumero._
import model.GameResult
import model.GameResult._
import model.FinalResult

object Game {

  private val moveSet = CaseEnumSerialization.apply[Move].values
  private val moveConverter = CaseEnumIndex.caseEnumIndex[Move]

  implicit val printError = new Printable[Error] {
    def providePrintableMsg(err: Error): String = s"--> [${err.message}] !!!"
  }

  private def printMessage[T](e: T)(implicit d: Printable[T]): Unit = println(d.providePrintableMsg(e))

  private def provideCPUMove(): Move = Random.shuffle(moveSet).head

  private def parsePlayerMove(value: String): Either[Error, Move] =
    moveConverter
      .caseFromIndex(value)
      .map(move => Right(move))
      .getOrElse(Left(InputParsingError))

  private def handleError(error: Error): Either[NonEmptyList[Error], FinalResult] =
    handleErrors(NonEmptyChain.one(error))

  private def handleErrors(errors: NonEmptyChain[Error]): Either[NonEmptyList[Error], FinalResult] = {
    errors.iterator.foreach(err => printMessage(err))
    Left(errors.toNonEmptyList)
  }

  private def playGameTurn(data: UserInputData): Either[NonEmptyList[Error], FinalResult] =
    parsePlayerMove(data.input)
      .fold(
        handleError,
        handleUserMove
      )

  private def handleUserMove(userMove: Move): Either[NonEmptyList[Error], FinalResult] = {
    val cpuMove = provideCPUMove()
    val matchResult = evaluateWinner(userMove, cpuMove)

    Right(
      new FinalResult(
        userMove,
        cpuMove,
        matchResult
      )
    )
  }

  private def evaluateWinner(userPlay: Move, cpuPlay: Move): GameResult =
    (userPlay, cpuPlay) match {
      case (Rock, Scissor) | (Scissor, Paper) | (Paper, Rock) => Win
      case (x, y) if x == y => Draw
      case _ => Lose
    }

  def play(userInput: String): Either[NonEmptyList[Error], FinalResult] =
    InputValidator
      .validateInput(userInput)
      .fold(
        handleErrors,
        playGameTurn
      )

}
