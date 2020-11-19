package model

import io.buildo.enumero.annotations.enum
import command.Move

@enum trait GameResult {
  Win
  Lose
  Draw
}
