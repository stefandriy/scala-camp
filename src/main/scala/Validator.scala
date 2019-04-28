
/**
  * Implement validator typeclass that should validate arbitrary value [T].
  *
  * @tparam T the type of the value to be validated.
  */
trait Validator[T] {
  /**
    * Validates the value.
    *
    * @param value value to be validated.
    * @return Right(value) in case the value is valid, Left(message) on invalid value
    */
  def validate(value: T): Either[String, T]

  /**
    * And combinator.
    *
    * @param other validator to be combined with 'and' with this validator.
    * @return the Right(value) only in case this validator and <code>other</code> validator returns valid value,
    *         otherwise Left with error messages from the validator that failed.
    */
  def and(other: Validator[T]): Validator[T] = (value: T) => {
    val first = this.validate(value)
    val second = other.validate(value)

    if (first.isLeft && second.isLeft) Left(first.left.get + " and " + second.left.get)
    else if (first.isLeft) first
    else if (second.isLeft) second
    else Right(value)
  }

  /**
    * Or combinator.
    *
    * @param other validator to be combined with 'or' with this validator.
    * @return the Right(value) only in case either this validator or <code>other</code> validator returns valid value,
    *         otherwise Left with error messages from both validators.
    */
  def or(other: Validator[T]): Validator[T] = (value: T) => {
    val first = this.validate(value)
    val second = other.validate(value)

    if (first.isLeft && second.isLeft) Left(first.left.get + " or " + second.left.get)
    else Right(value)
  }
}

object Validator {
  val positiveInt: Validator[Int] = (t: Int) => Either.cond(t > 0, t, "should be positive")

  def lessThan(n: Int): Validator[Int] = (t: Int) => Either.cond(t < n, t, s"should be less than $n")

  val nonEmpty: Validator[String] = (t: String) => Either.cond(t.nonEmpty, t, "should not be empty")

  val isPersonValid: Validator[Person] = (person: Person) => {
    val validName = nonEmpty.validate(person.name)
    val validAge = positiveInt.and(lessThan(100)).validate(person.age)

    if (validName.isLeft && validAge.isLeft) Left(s"name ${validName.left.get} and age ${validAge.left.get}")
    else if (validName.isLeft) Left(s"name ${validName.left.get}")
    else if (validAge.isLeft) Left(s"age ${validAge.left.get}")
    else Right(person)
  }

  implicit class IntImprovements(val n: Int) {
    def validate(implicit t: Validator[Int] = positiveInt): Either[String, Int] = t.validate(n)
  }

  implicit class StringImprovements(val n: String) {
    def validate(implicit t: Validator[String] = nonEmpty): Either[String, String] = t.validate(n)
  }

  implicit class PersonImprovements(val n: Person) {
    def validate(implicit t: Validator[Person] = isPersonValid): Either[String, Person] = t.validate(n)
  }

}

object ValidApp {

  import Validator._

  2 validate (positiveInt and lessThan(10))

  "" validate Validator.nonEmpty

  Person(name = "John", age = 25) validate isPersonValid
}

object ImplicitValidApp {

  import Validator._

  Person(name = "John", age = 25) validate

  "asdasd" validate

  234.validate
}


case class Person(name: String, age: Int)