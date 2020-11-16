package command

sealed trait Command

case object ExitCommand extends Command
case object PlayAgainCommand extends Command

object Play {
  sealed trait PlayCommand extends Command
  case object Rock extends PlayCommand
  case object Paper extends PlayCommand
  case object Scissor extends PlayCommand

  val moves = Seq(Rock, Paper, Scissor)
}
