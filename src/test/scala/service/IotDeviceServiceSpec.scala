package service

import cats.implicits._
import org.scalatest._
import repository.{IotDeviceRepositoryFuture, IotDeviceRepositoryId, UserRepositoryFuture, UserRepositoryId}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class IotDeviceServiceSpec extends FlatSpec with Matchers {

  "IotDeviceServiceId" should "save IotDevice" in {
    val username = "username"
    val wrongId = 999
    val sn = "serial number"
    val userRepository = new UserRepositoryId
    val deviceService = new IotDeviceService(new IotDeviceRepositoryId, userRepository)
    val savedUser = userRepository.registerUser(username)
    val savedDevice = deviceService.registerDevice(savedUser.id, sn).right.get
    savedDevice.userId shouldEqual savedUser.id
    savedDevice.sn shouldEqual sn
    deviceService.registerDevice(wrongId, sn).left.get shouldEqual "User doesn't exist"
    deviceService.registerDevice(savedDevice.id, sn).left.get shouldEqual "Device serial number is already registered"
  }

  "IotDeviceServiceFuture" should "save IotDevice" in {
    val username = "username"
    val wrongId = 999
    val sn = "serial number"
    val userRepository = new UserRepositoryFuture
    val deviceService = new IotDeviceService(new IotDeviceRepositoryFuture, userRepository)
    val savedUser = await(userRepository.registerUser(username))
    val savedDevice = await(deviceService.registerDevice(savedUser.id, sn)).right.get
    savedDevice.userId shouldEqual savedUser.id
    savedDevice.sn shouldEqual sn
    await(deviceService.registerDevice(wrongId, sn)).left.get shouldEqual "User doesn't exist"
    await(deviceService.registerDevice(savedDevice.id, sn)).left.get shouldEqual "Device serial number is already registered"
  }

  private def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
