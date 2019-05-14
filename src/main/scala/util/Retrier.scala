package util

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object Retrier extends App {

  final def retry[A](block: () => Future[A],
                     acceptResult: A => Boolean,
                     retries: List[FiniteDuration]): Future[A] = {
    block()
      .flatMap(result => {
        if (acceptResult(result) || retries.isEmpty) {
          Future(result)
        } else {
          Thread.sleep(retries.head.toMillis)
          retry(block, acceptResult, retries.tail)
        }
      })
  }
}
