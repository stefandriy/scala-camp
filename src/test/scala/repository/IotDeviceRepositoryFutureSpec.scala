package repository

import org.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class IotDeviceRepositoryFutureSpec extends FlatSpec with Matchers {

  "IotDeviceRepositoryFuture" should "save and retrieve IotDevice" in {
    val userId = 1
    val sn = "serial number"
    val wrongId = 999
    val wrongSn = "bla-bla-bla"
    val deviceRepository = new IotDeviceRepositoryFuture
    val savedDevice = await(deviceRepository.registerDevice(userId, sn))
    savedDevice.userId shouldEqual userId
    savedDevice.sn shouldEqual sn
    await(deviceRepository.getById(savedDevice.id)) shouldEqual Option(savedDevice)
    await(deviceRepository.getBySn(sn)) shouldEqual Option(savedDevice)
    await(deviceRepository.getByUser(userId)) shouldEqual Seq(savedDevice)
    await(deviceRepository.getById(wrongId)) shouldEqual Option.empty
    await(deviceRepository.getBySn(wrongSn)) shouldEqual Option.empty
    await(deviceRepository.getByUser(wrongId)) shouldEqual Seq.empty
  }

  private def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
