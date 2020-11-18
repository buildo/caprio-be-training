package validation

import cats.data._
import cats.data.Validated._
import cats.implicits._
import error.NotAllowedInput
import error.InvalidLengthInput
import error.Error

final case class UserInputData(input: String)

sealed trait InputValidator {

  type ValidationResult[A] = ValidatedNec[Error, A]

  def validateOnlyDigitInput(input: String): ValidationResult[String] =
    if (input.matches("^[0-9]+|Q+$")) input.validNec else NotAllowedInput.invalidNec

  def validateInputLenght(input: String): ValidationResult[String] =
    if (input.length() == 1) input.validNec else InvalidLengthInput.invalidNec

  def validateInput(input: String): ValidationResult[UserInputData] =
    (validateOnlyDigitInput(input), validateInputLenght(input))
      .mapN((_, _) => UserInputData(input))
}

object InputValidator extends InputValidator
