import Validator._
import org.scalatest._

class ValidatorSpec extends FlatSpec with Matchers {

  private val positiveIntError = "Should be positive."

  private def lessThanError = (n: Int) => s"Should be less than $n."

  private val nonEmptyError = "Should not be empty."

  "Positive Int Validator" should "return valid result" in {
    positiveInt.validate(42) shouldEqual Right(42)
  }

  "Positive Int Validator" should "return error message" in {
    positiveInt.validate(-42) shouldEqual Left(positiveIntError)
    positiveInt.validate(0) shouldEqual Left(positiveIntError)
  }

  "Less Than Int Validator" should "return valid result" in {
    lessThan(69).validate(42) shouldEqual Right(42)
  }

  "Less Than Int Validator" should "return error message" in {
    lessThan(13).validate(42) shouldEqual Left(lessThanError(13))
    lessThan(42).validate(42) shouldEqual Left(lessThanError(42))
  }

  "Positive Int AND Less Than Validator" should "return valid result" in {
    positiveInt.and(lessThan(69)).validate(42) shouldEqual Right(42)
  }

  "Positive Int AND Less Than Validator" should "return error message" in {
    positiveInt.and(lessThan(69)).validate(-42) shouldEqual Left(positiveIntError)
    positiveInt.and(lessThan(69)).validate(142) shouldEqual Left(lessThanError(69))
    positiveInt.and(lessThan(-69)).validate(-42) shouldEqual Left(positiveIntError + lessThanError(-69))
  }

  "Positive Int OR Less Than Validator" should "return valid result" in {
    positiveInt.or(lessThan(69)).validate(42) shouldEqual Right(42)
    positiveInt.or(lessThan(69)).validate(142) shouldEqual Right(142)
    positiveInt.or(lessThan(-69)).validate(-142) shouldEqual Right(-142)
  }

  "Positive Int OR Less Than Validator" should "return error message" in {
    positiveInt.or(lessThan(-69)).validate(-42) shouldEqual Left(positiveIntError + lessThanError(-69))
  }

  "Non Empty Validator" should "return valid result" in {
    nonEmpty.validate("test") shouldEqual Right("test")
  }

  "Non Empty Validator" should "return error message" in {
    nonEmpty.validate("") shouldEqual Left(nonEmptyError)
  }

  "Person Validator" should "return valid result" in {
    isPersonValid.validate(Person("John", 42)) shouldEqual Right(Person("John", 42))
  }

  "Person Validator" should "return error message" in {
    isPersonValid.validate(Person("John", -42)) shouldEqual Left(positiveIntError)
    isPersonValid.validate(Person("John", 142)) shouldEqual Left(lessThanError(100))
    isPersonValid.validate(Person("", 42)) shouldEqual Left(nonEmptyError)
    isPersonValid.validate(Person("", -42)) shouldEqual Left(nonEmptyError + positiveIntError)
    isPersonValid.validate(Person("", 142)) shouldEqual Left(nonEmptyError + lessThanError(100))
  }
}
