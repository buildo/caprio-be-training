import scala.util.Random
import command.Command
import command.Command._
import command.Move
import command.Move._
import error.Error
import error.InvalidInput
import error.Printable

object Game extends App {

  implicit val printError = new Printable[Error] {
    def providePrintableMsg(err: Error): String = s"--> [${err.message}] !!!"
  }

  def printMessage[T](e: T)(implicit d: Printable[T]): Unit = println(d.providePrintableMsg(e))

  def isExitCommandDetect(value: String): Boolean = value == "Q"

  def provideCPUMove(): Move = Random.shuffle(Move.moves).head

  def parsePlayerMove(value: String): Either[Error, Move] = value match {
    case "0" => Right(Rock)
    case "1" => Right(Paper)
    case "2" => Right(Scissor)
    case _ => Left(InvalidInput)
  }

  def handleInvalidInput(error: Error) = {
    printMessage(error);
    PlayAgain
  }

  def handleUserMove(userMove: Either[Error, Move]): Command.PlayAgain.type =
    userMove.fold(
      handleInvalidInput,
      (userMove: Move) => {

        val cpuMove = provideCPUMove()
        println(s"[User] ${userMove} <-> ${cpuMove} [CPU]")

        val matchResult = evaluateWinner(userMove, cpuMove)
        println(s"Match result  : ${matchResult}\n\n")

        PlayAgain
      }
    )

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

    val maybePlayAgain = Either
      .cond(
        !isExitCommandDetect(userInput),
        parsePlayerMove(userInput),
        Exit
      )
      .map(handleUserMove)

    maybePlayAgain.fold(
      exit => println("--- EXIT ---"),
      _ => play()
    )
  }

  println("Start with playing to Rock Paper Scissor")
  play()
}
