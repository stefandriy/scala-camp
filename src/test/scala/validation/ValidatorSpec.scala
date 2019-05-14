package validation

import org.scalatest._
import validation.Validator._

class ValidatorSpec extends FlatSpec with Matchers {

  private val positiveIntError = "should be positive"

  private def lessThanError = (n: Int) => s"should be less than $n"

  private val nonEmptyError = "should not be empty"

  "Positive Int validation.Validator" should "return valid result" in {
    positiveInt.validate(42) shouldEqual Right(42)
  }

  "Positive Int validation.Validator" should "return error message" in {
    positiveInt.validate(-42) shouldEqual Left(positiveIntError)
    positiveInt.validate(0) shouldEqual Left(positiveIntError)
  }

  "Less Than Int validation.Validator" should "return valid result" in {
    lessThan(69).validate(42) shouldEqual Right(42)
  }

  "Less Than Int validation.Validator" should "return error message" in {
    lessThan(13).validate(42) shouldEqual Left(lessThanError(13))
    lessThan(42).validate(42) shouldEqual Left(lessThanError(42))
  }

  "Positive Int AND Less Than validation.Validator" should "return valid result" in {
    positiveInt.and(lessThan(69)).validate(42) shouldEqual Right(42)
  }

  "Positive Int AND Less Than validation.Validator" should "return error message" in {
    positiveInt.and(lessThan(69)).validate(-42) shouldEqual Left(positiveIntError)
    positiveInt.and(lessThan(69)).validate(142) shouldEqual Left(lessThanError(69))
    positiveInt.and(lessThan(-69)).validate(-42) shouldEqual Left(s"$positiveIntError and ${lessThanError(-69)}")
  }

  "Positive Int OR Less Than validation.Validator" should "return valid result" in {
    positiveInt.or(lessThan(69)).validate(42) shouldEqual Right(42)
    positiveInt.or(lessThan(69)).validate(142) shouldEqual Right(142)
    positiveInt.or(lessThan(-69)).validate(-142) shouldEqual Right(-142)
  }

  "Positive Int OR Less Than validation.Validator" should "return error message" in {
    positiveInt.or(lessThan(-69)).validate(-42) shouldEqual Left(s"$positiveIntError or ${lessThanError(-69)}")
  }

  "Non Empty validation.Validator" should "return valid result" in {
    nonEmpty.validate("test") shouldEqual Right("test")
  }

  "Non Empty validation.Validator" should "return error message" in {
    nonEmpty.validate("") shouldEqual Left(nonEmptyError)
  }

  "validation.Person validation.Validator" should "return valid result" in {
    isPersonValid.validate(Person("John", 42)) shouldEqual Right(Person("John", 42))
  }

  "validation.Person validation.Validator" should "return error message" in {
    isPersonValid.validate(Person("John", -42)) shouldEqual Left(s"age $positiveIntError")
    isPersonValid.validate(Person("John", 142)) shouldEqual Left(s"age ${lessThanError(100)}")
    isPersonValid.validate(Person("", 42)) shouldEqual Left(s"name $nonEmptyError")
    isPersonValid.validate(Person("", -42)) shouldEqual Left(s"name $nonEmptyError and age $positiveIntError")
    isPersonValid.validate(Person("", 142)) shouldEqual Left(s"name $nonEmptyError and age ${lessThanError(100)}")
  }
}
