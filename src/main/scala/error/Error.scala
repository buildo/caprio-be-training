package error

sealed trait Error extends Throwable{
  def message: String
}

object GameNotFoundError extends Error {
  def message: String = "Game result not found"
}

object GenericError extends Error {
  def message: String = "Something was wrong please contact system admin !"
}

object NotAllowedInput extends Error {
  def message: String = "Input not allowed : only 1,2,3,Q are allowed"
}

object InvalidLengthInput extends Error {
  def message: String = "Length input wrong : should be 1 char"
}

object InputParsingError extends Error {
  def message: String = "ERROR parsing input"
}

trait Printable[T] {
  def providePrintableMsg(t: T): String
}
