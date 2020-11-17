import scala.util.Random
import command.Command
import command.Command._
import command.Move
import command.Move._
import error.Error
import error.InvalidInput

object Game extends App {

  def isExitCommandDetect(value: String): Boolean = value == "Q"

  def provideCPUMove(): Move = Random.shuffle(Move.moves).head

  def parsePlayerMove(value: String): Either[Error, Move] = value match {
    case "0" => Right(Rock)
    case "1" => Right(Paper)
    case "2" => Right(Scissor)
    case _ => Left(InvalidInput)
  }

  def handleInvalidInput(error: Error) = {
    println(error.getFormattedErrorMsg())
    PlayAgain
  }

  def handleUserMove(userMove: Either[Error, Move]): Command.PlayAgain.type =
    userMove.fold(
      handleInvalidInput,
      (userMove: Move) => {

        val cpuMove = provideCPUMove()
        println(s"[User] ${userMove.toString()} <-> ${cpuMove.toString()} [CPU]")

        val matchResult = evaluateWinner(userMove, cpuMove)
        println(s"Math result  : ${matchResult}\n\n")

        PlayAgain
      }
    )

  def evaluateWinner(userPlay: Move, cpuPlay: Move): String =
    (userPlay, cpuPlay) match {
      case (Rock, Scissor) | (Scissor, Paper) | (Paper, Rock) => "YOU WIN"
      case (Rock, Paper) | (Scissor, Rock) | (Paper, Scissor) => "YOU LOSE"
      case _ => "Match is DRAW!"
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

    maybePlayAgain.map(userCmd => play())
  }

  println("Start with playing to Rock Paper Scissor")
  play()
  println("--- EXIT ---")
}
