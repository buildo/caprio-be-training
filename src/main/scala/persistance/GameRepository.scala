package persistance

import scala.collection.concurrent.TrieMap
import command.Move
import model.GameResult
import scala.util.Random
import scala.collection.immutable.ListMap

class GameStatusEntity(val userMove: Move, val cpuMove: Move, val gameResult: GameResult)

trait GameRepository {
  def insertGameResult(userMove: Move, cpuMove: Move, gameResult: GameResult)
  def getAllGameResultsSortedAsc(): List[GameStatusEntity]
}

class GameRepositoryImpl extends GameRepository {

  val gameStatus: TrieMap[Integer, GameStatusEntity] = TrieMap()

  private def generateId(): Integer = {
    val keyList = gameStatus.keySet.toList
    if (keyList.isEmpty) 1 else keyList.max + 1
  }

  override def getAllGameResultsSortedAsc() = ListMap(gameStatus.toSeq.sortWith(_._1 < _._1): _*).values.toList

  override def insertGameResult(userMove: Move, cpuMove: Move, gameResult: GameResult) =
    gameStatus.put(
      generateId(),
      new GameStatusEntity(userMove, cpuMove, gameResult)
    )

}
