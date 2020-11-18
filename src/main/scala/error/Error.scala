package error

sealed trait Error {
  def message: String
}

object NotAllowedInput extends Error {
  def message: String = "Input not allowed : only 1,2,3,Q are allowed"
}

object InvalidLengthInput extends Error {
  def message: String = "Length input wrong : should be 1 char"
}

trait Printable[T] {
  def providePrintableMsg(t: T): String
}
