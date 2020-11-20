package persistance

import scala.collection.concurrent.TrieMap
import command.Move
import model.GameResult
import scala.util.Random

class GameStatusEntity(val userMove: Move, val cpuMove: Move, val gameResult: GameResult)

trait GameRepository {
  def insertGameResult(userMove: Move, cpuMove: Move, gameResult: GameResult)
  def getAllgameResults(): List[GameStatusEntity]
}

class GameRepositoryImpl extends GameRepository {

  val gameStatus: TrieMap[Integer, GameStatusEntity] = TrieMap()

  private def generateId(keys: List[Integer]): Integer = if (keys.isEmpty) 1 else keys.max + 1

  override def getAllgameResults(): List[GameStatusEntity] = gameStatus.values.toList

  override def insertGameResult(userMove: Move, cpuMove: Move, gameResult: GameResult) {
    val keys: List[Integer] = gameStatus.keySet.seq.toList

    gameStatus.put(
      generateId(keys),
      new GameStatusEntity(userMove, cpuMove, gameResult)
    )
  }

}
