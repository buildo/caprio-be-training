import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec
import org.scalamock.scalatest.MockFactory
import service.GameServiceImpl
import service.GameService
import persistance.GameRepository
import command.Move
import command.Move._
import model.GameResult._
import persistance.GameStatusEntity
import service.CpuMoveStrategy

class GameServiceTest extends AnyFunSpec with MockFactory with BeforeAndAfterEach {

  var mockgameRepository: GameRepository = _
  var mockCpuMoveStrategy: CpuMoveStrategy = _
  var gameService: GameServiceImpl = _

  override def beforeEach() {
    mockgameRepository = mock[GameRepository]
    mockCpuMoveStrategy = mock[CpuMoveStrategy]
    gameService = new GameServiceImpl(mockgameRepository, mockCpuMoveStrategy)
  }

  describe("Test play") {

    it("user lose") {

      (mockCpuMoveStrategy.provideCPUMove _).expects().returns(Paper)
      (mockgameRepository.insertGameResult _).expects(Rock, Paper, Lose).once()

      val actualResult = gameService.play(Rock)

      assert(actualResult.gameResult == Lose)
      assert(actualResult.computerMove == Paper)
      assert(actualResult.userMove == Rock)
    }

    it("user win") {
      (mockCpuMoveStrategy.provideCPUMove _).expects().returns(Scissors)
      (mockgameRepository.insertGameResult _).expects(Rock, Scissors, Win).once()

      val actualResult = gameService.play(Rock)

      assert(actualResult.gameResult == Win)
      assert(actualResult.computerMove == Scissors)
      assert(actualResult.userMove == Rock)
    }

    it("draw case") {
      (mockCpuMoveStrategy.provideCPUMove _).expects().returns(Scissors)
      (mockgameRepository.insertGameResult _).expects(Scissors, Scissors, Draw).once()

      val actualResult = gameService.play(Scissors)

      assert(actualResult.gameResult == Draw)
      assert(actualResult.computerMove == Scissors)
      assert(actualResult.userMove == Scissors)
    }
  }

  describe("Test getLastGameResult") {
    it("should return last insert entry") {
      val previousGameResult = buildDefaultListResult()

      (mockgameRepository.getAllGameResultsSortedAsc _).expects().returns(previousGameResult)

      val actualResult = gameService.getLastGameResult()
      val expectedResult = previousGameResult.last

      assert(actualResult.isRight == true)
      actualResult.map(res => {
        assert(res.computerMove == expectedResult.cpuMove)
        assert(res.userMove == expectedResult.userMove)
        assert(res.gameResult == expectedResult.gameResult)
      })
    }

    it("should return error on empty result") {
      (mockgameRepository.getAllGameResultsSortedAsc _).expects().returns(List.empty)

      val actualResult = gameService.getLastGameResult()

      assert(actualResult.isLeft == true)
    }
  }

  private def cpuMoveWith(requiredMove: Move) = () => requiredMove

  private def buildDefaultListResult(): List[GameStatusEntity] =
    List(
      new GameStatusEntity(Rock, Paper, Lose),
      new GameStatusEntity(Rock, Rock, Draw),
      new GameStatusEntity(Rock, Paper, Lose)
    )
}
