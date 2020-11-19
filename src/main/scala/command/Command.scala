package command

import io.buildo.enumero.annotations.indexedEnum
import io.buildo.enumero.annotations.enum

@enum trait Command {
  Exit
  PlayAgain
}

@enum trait Move {
  object Rock 
  object Paper
  object Scissors
}
