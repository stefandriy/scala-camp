import scala.annotation.tailrec
import scala.concurrent.duration._

class Application extends App {

  @tailrec
  final def retry[A](block: () => A,
                     acceptResult: A => Boolean,
                     retries: List[FiniteDuration]): A = {
    val result = block()
    if (acceptResult(result) || retries.isEmpty)
      result
    else {
      Thread.sleep(retries.head.toMillis)
      retry(block, acceptResult, retries.tail)
    }
  }
}
