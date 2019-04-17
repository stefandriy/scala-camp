import org.scalatest._
import scala.concurrent.duration._

class ApplicationSpec extends FlatSpec with Matchers {

  "The retry call" should "return valid result" in {
    new Application().retry[Int](
      block = () => 1 + 1,
      acceptResult = res => res % 2 == 0,
      retries = List[FiniteDuration](0.seconds, 1.seconds, 2.seconds)
    ) shouldEqual 2
  }

  "The retry call without retries" should "return valid result" in {
    new Application().retry[Int](
      block = () => 1 + 1,
      acceptResult = res => res % 2 == 0,
      retries = List.empty
    ) shouldEqual 2
  }

  "The always false retry call " should "wait and return last result" in {
    new Application().retry[Int](
      block = () => 1 + 1,
      acceptResult = res => false,
      retries = List[FiniteDuration](0.seconds, 1.seconds, 2.seconds)
    ) shouldEqual 2
  }

  "The retry call" should "throw exception" in {
    assertThrows[Exception](
      new Application().retry[Int](
        block = () => throw new Exception,
        acceptResult = res => res % 2 == 0,
        retries = List[FiniteDuration](0.seconds, 1.seconds, 2.seconds)
      )
    )
  }
}
