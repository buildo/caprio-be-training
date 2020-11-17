sealed abstract case class Error(errorMessage: String) {
  def getFormattedErrorMsg(): String = s"--> [${this.errorMessage}] !!!"
}

object InvalidInput extends Error("Invalid Input")
