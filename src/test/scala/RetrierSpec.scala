import org.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class RetrierSpec extends FlatSpec with Matchers {

  "The retry call" should "return valid result" in {
    await(
      Retrier.retry[Int](() => Future(1 + 1), res => res % 2 == 0, List(0.seconds, 1.seconds, 2.seconds))
    ) shouldEqual 2
  }

  "The retry call without retries" should "return valid result" in {
    await(Retrier.retry[Int](() => Future(1 + 1), res => res % 2 == 0, List.empty)) shouldEqual 2
  }

  "The always false retry call " should "wait and return last result" in {
    await(Retrier.retry[Int](() => Future(1 + 1), _ => false, List(0.seconds, 1.seconds, 2.seconds))) shouldEqual 2
  }

  "The retry call" should "throw exception" in {
    assertThrows[Exception](
      Retrier.retry[Int](() => throw new Exception, res => res % 2 == 0, List(0.seconds, 1.seconds, 2.seconds)))
  }

  private def await[T] = (future: Future[T]) => Await.result(future, 5.seconds)
}
