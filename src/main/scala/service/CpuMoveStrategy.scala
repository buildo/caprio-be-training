package service

import command.Move
import io.buildo.enumero.CaseEnumSerialization
import scala.util.Random

trait CpuMoveStrategy {
  def provideCPUMove(): Move
}

class CpuMoveStrategyImpl extends CpuMoveStrategy {
  def provideCPUMove(): Move = {
    val moveSerialization = CaseEnumSerialization.apply[Move]
    Random.shuffle(moveSerialization.values).head
  }
}
