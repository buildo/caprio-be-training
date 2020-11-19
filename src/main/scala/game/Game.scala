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

object Game {

  val moveSet = CaseEnumSerialization.apply[Move].values
  val moveConverter = CaseEnumIndex.caseEnumIndex[Move]

  implicit val printError = new Printable[Error] {
    def providePrintableMsg(err: Error): String = s"--> [${err.message}] !!!"
  }

  def printMessage[T](e: T)(implicit d: Printable[T]): Unit = println(d.providePrintableMsg(e))

  def detectExitCommand(value: String): Either[String, Exit.type] =
    Either
      .cond(
        value == "Q",
        Exit,
        value
      )
  
  def provideCPUMove(): Move = Random.shuffle(moveSet).head

  def parsePlayerMove(value: String): Either[Error, Move] =
    moveConverter
      .caseFromIndex(value)
      .map(move => Right(move))
      .getOrElse(Left(InputParsingError))

  def handleError(error: Error): Either[Exit.type, PlayAgain.type] =
    handleErrors(NonEmptyChain.one(error))

  def handleErrors(errors: NonEmptyChain[Error]): Either[Exit.type, PlayAgain.type] = {
    errors.iterator.foreach(err => printMessage(err))
    Right(PlayAgain)
  }

  def playGameTurn(data: UserInputData): Either[Exit.type, PlayAgain.type] =
    detectExitCommand(data.input)
      .fold(
        parsePlayerMove(_)
          .fold(
            handleError,
            handleUserMove
          ),
        _ => Left(Exit)
      )

  def handleUserMove(userMove: Move): Either[Exit.type, PlayAgain.type] = {
    val cpuMove = provideCPUMove()
    println(s"[User] ${userMove} <-> ${cpuMove} [CPU]")

    val matchResult = evaluateWinner(userMove, cpuMove)
    println(s"Match result  : ${matchResult}\n\n")

    Right(PlayAgain)
  }

  def evaluateWinner(userPlay: Move, cpuPlay: Move): String =
    (userPlay, cpuPlay) match {
      case (Rock, Scissor) | (Scissor, Paper) | (Paper, Rock) => "YOU WIN"
      case (x, y) if x == y => "Match is DRAW!"
      case _ => "YOU LOSE"
    }

  def play(): Unit = {
    println(
      """      
      ==============================================================
          digit char and choose for your move and press enter: 
              0 - [ ROCK ]
              1 - [ PAPER ] 
              2 - [ SCISSOR ] 
              digit Q to exit
      ==============================================================
      """
    )

    val userInput = scala.io.StdIn.readLine()

    val maybePlayAgain: Either[Exit.type, PlayAgain.type] = InputValidator
      .validateInput(userInput)
      .fold(
        handleErrors,
        playGameTurn
      )

    maybePlayAgain.fold(
      exit => println("--- EXIT ---"),
      _ => play()
    )
  }

  println("Start with playing to Rock Paper Scissor")
  play()
}
