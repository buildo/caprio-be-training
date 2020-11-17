import scala.util.Random
import command.Command
import command.PlayAgainCommand
import command.ExitCommand
import command.Play._
import command.Play

object Game extends App {

  def provideCPUMove(): PlayCommand = Random.shuffle(Play.moves).head

  def parsePlayerMove(value: String): Either[Error, Command] = value match {
    case "0" => Right(Rock);
    case "1" => Right(Paper);
    case "2" => Right(Scissor);
    case "Q" => Right(ExitCommand)
    case _ => Left(InvalidInput)
  }

  def evaluateWinner(userPlay: PlayCommand, cpuPlay: PlayCommand): String =
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

    val maybePlayAgain = parsePlayerMove(userInput)
      .fold(
        invalidCommad => {
          println(invalidCommad.getFormattedErrorMsg());
          Some(PlayAgainCommand);
        },
        cmd =>
          cmd match {
            case userMove: PlayCommand => {
              val cpuMove = provideCPUMove()
              println(s"[User] ${userMove.toString()} <-> ${cpuMove.toString()} [CPU]")

              val matchResult = evaluateWinner(userMove, cpuMove)
              println(s"Math result  : ${matchResult}\n\n")

              Some(PlayAgainCommand)
            }
            case _ => None
          }
      )

    maybePlayAgain.map(userCmd => play())
  }

  println("Start with playing to Rock Paper Scissor")
  play()
  println("--- EXIT ---")
}
