package command

sealed trait Command
object Command {
  final object Exit extends Command
  final object PlayAgain extends Command
}

sealed trait Move
object Move {
  final case object Rock extends Move
  final case object Paper extends Move
  final case object Scissor extends Move

  val moves = Seq(Rock, Paper, Scissor)
}
