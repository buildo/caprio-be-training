package command

import io.buildo.enumero.annotations.indexedEnum
import io.buildo.enumero.annotations.enum

@enum trait Command {
  Exit
  PlayAgain
}

@indexedEnum trait Move {
  type Index = String
  object Rock { "0" }
  object Paper { "1" }
  object Scissor { "2" }
}
