import org.scalatest.OneInstancePerTest
import org.scalatest.funspec.AnyFunSpec
import org.scalamock.scalatest.MockFactory
import org.scalacheck.Gen
import io.buildo.enumero.CaseEnumSerialization
import service.{CpuMoveStrategy, GameEngine, GameService, GameServiceImpl}
import persistance.GameRepository
import command.Move
import command.Move._
import model.GameResult._
import model.FinalResult
import persistance.GameStatusEntity
import model.GameResult

class GameServiceTest extends AnyFunSpec with MockFactory with OneInstancePerTest {

  var mockgameRepository: GameRepository = mock[GameRepository]
  var mockCpuMoveStrategy: CpuMoveStrategy = mock[CpuMoveStrategy]
  var mockGameEngine: GameEngine = mock[GameEngine]

  val gameResultsGen: Gen[GameStatusEntity] = for {
    userMove <- Gen.oneOf(CaseEnumSerialization.apply[Move].values)
    cpuMove <- Gen.oneOf(CaseEnumSerialization.apply[Move].values)
    result <- Gen.oneOf(CaseEnumSerialization.apply[GameResult].values)
  } yield GameStatusEntity(userMove, cpuMove, result)

  val genGameResultsList = (num: Int) => Gen.containerOfN[List, GameStatusEntity](num, gameResultsGen)

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
      val previousGameResult = genGameResultsList(10).sample.get

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
}
