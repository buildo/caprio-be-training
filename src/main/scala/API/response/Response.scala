package api.response
import command.Move
import model.GameResult

final case class FinalResultResponse(userMove: Move, computerMove: Move, result: GameResult)
