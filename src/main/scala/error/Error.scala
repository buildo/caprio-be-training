package error

sealed trait Error {
  def message: String
}

object InvalidInput extends Error {
  override def message: String = "Invalid Input"
}

trait Printable[T] {
  def providePrintableMsg(t: T): String
}
