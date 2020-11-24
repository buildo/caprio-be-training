import org.scalatest.OneInstancePerTest
import org.scalatest.funspec.AnyFunSpec
import org.scalamock.scalatest.MockFactory
import service.{CpuMoveStrategy, GameEngine, GameService, GameServiceImpl}
import persistance.GameRepository
import command.Move
import command.Move._
import model.GameResult._
import model.FinalResult
import persistance.GameStatusEntity

class GameServiceTest extends AnyFunSpec with MockFactory with OneInstancePerTest {

  var mockgameRepository: GameRepository = mock[GameRepository]
  var mockCpuMoveStrategy: CpuMoveStrategy = mock[CpuMoveStrategy]
  var mockGameEngine: GameEngine = mock[GameEngine]

  var gameService: GameServiceImpl = new GameServiceImpl(mockgameRepository, mockCpuMoveStrategy, mockGameEngine)

  it("Test play behaviours") {
    (mockCpuMoveStrategy.provideCPUMove _).expects().returns(Paper)
    (mockGameEngine.evaluateWinner _).expects(Rock, Paper).returns(Lose)
    (mockgameRepository.insertGameResult _).expects(Rock, Paper, Lose).once()

    val actualResult = gameService.play(Rock)

    assert(actualResult.userMove == Rock)
    assert(actualResult.computerMove == Paper)
    assert(actualResult.gameResult == Lose)
  }

  describe("Test getLastGameResult") {
    it("should return last insert entry") {
      val previousGameResult = buildDefaultListResult()

      (mockgameRepository.getAllGameResultsSortedAsc _).expects().returns(previousGameResult)

      val actualResult = gameService.getLastGameResult()
      val expectedResult = new FinalResult(
        previousGameResult.last.userMove,
        previousGameResult.last.cpuMove,
        previousGameResult.last.gameResult
      )

      assert(actualResult == Right(expectedResult))
    }

    it("should return error on empty result") {
      (mockgameRepository.getAllGameResultsSortedAsc _).expects().returns(List.empty)

      val actualResult = gameService.getLastGameResult()

      assert(actualResult.isLeft == true)
    }
  }

  private def buildDefaultListResult(): List[GameStatusEntity] =
    List(
      new GameStatusEntity(Rock, Paper, Lose),
      new GameStatusEntity(Rock, Rock, Draw),
      new GameStatusEntity(Rock, Paper, Lose)
    )
}
