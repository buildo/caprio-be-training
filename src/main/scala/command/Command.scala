package command

import io.buildo.enumero.annotations.enum

sealed trait Command
object Command {
  final object Exit extends Command
  final object PlayAgain extends Command
}

@enum trait Move {
  object Rock
  object Paper
  object Scissor
}
