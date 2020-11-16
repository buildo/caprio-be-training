sealed abstract case class Error(errorMessage: String) {
  def getFormattedErrorMsg(): String = s"--> [${this.errorMessage}] !!!"
}

object InvaliInput extends Error("Invalid Input");
