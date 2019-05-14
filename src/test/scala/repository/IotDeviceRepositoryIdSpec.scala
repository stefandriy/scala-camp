package repository

import org.scalatest._

class IotDeviceRepositoryIdSpec extends FlatSpec with Matchers {

  "IotDeviceRepositoryId" should "save and retrieve IotDevice" in {
    val userId = 1
    val sn = "serial number"
    val wrongId = 999
    val wrongSn = "bla-bla-bla"
    val deviceRepository = new IotDeviceRepositoryId
    val savedDevice = deviceRepository.registerDevice(userId, sn)
    savedDevice.userId shouldEqual userId
    savedDevice.sn shouldEqual sn
    deviceRepository.getById(savedDevice.id) shouldEqual Option(savedDevice)
    deviceRepository.getBySn(sn) shouldEqual Option(savedDevice)
    deviceRepository.getByUser(userId) shouldEqual Seq(savedDevice)
    deviceRepository.getById(wrongId) shouldEqual Option.empty
    deviceRepository.getBySn(wrongSn) shouldEqual Option.empty
    deviceRepository.getByUser(wrongId) shouldEqual Seq.empty
  }
}
