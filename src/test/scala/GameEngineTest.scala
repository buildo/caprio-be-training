import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec
import org.scalamock.scalatest.MockFactory
import service.{GameEngine, GameEngineImpl}
import command.Move._
import model.GameResult._

class GameEngineTest extends AnyFunSpec with MockFactory {

  val gameEngine: GameEngine = new GameEngineImpl()

  describe("Test GameEngine evaluate winner") {

    it("user should lose") {
      val actualResult = gameEngine.evaluateWinner(Rock, Paper)
      assert(actualResult == Lose)
    }

    it("user should win") {
      val actualResult = gameEngine.evaluateWinner(Rock, Scissors)
      assert(actualResult == Win)
    }

    it("user should draw") {
      val actualResult = gameEngine.evaluateWinner(Scissors, Scissors)
      assert(actualResult == Draw)
    }
  }
}
