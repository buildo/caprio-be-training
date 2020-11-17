package command

sealed trait Command
object Command{
  case object Exit extends Command
  case object PlayAgain extends Command
}

sealed trait Move
object Move {
  case object Rock extends Move
  case object Paper extends Move
  case object Scissor extends Move

  val moves = Seq(Rock, Paper, Scissor)
}
