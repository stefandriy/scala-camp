package service

import domain.User
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import repository.{IotDeviceRepositoryFuture, UserRepository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class IotDeviceServiceSpec extends FlatSpec with Matchers with MockFactory {

  "IotDeviceService" should "save IotDevice" in {
    val id = 1
    val username = "username"
    val email = "user@email.test"
    val wrongId = 999
    val sn = "serial number"
    val userRepository = stub[UserRepository]
    val user = User(id, username, None, email)
    (userRepository.findById _).when(id).returns(Future.successful(Option(user)))
    (userRepository.findById _).when(wrongId).returns(Future.successful(Option.empty))
    val deviceService = new IotDeviceService(new IotDeviceRepositoryFuture, userRepository)

    val savedDevice = await(deviceService.registerDevice(id, sn)).right.get
    savedDevice.userId shouldEqual user.id
    savedDevice.sn shouldEqual sn
    await(deviceService.registerDevice(wrongId, sn)).left.get shouldEqual "User doesn't exist"
    await(deviceService.registerDevice(id, sn)).left.get shouldEqual "Device serial number is already registered"
  }

  private def await[T](future: Future[T]): T = Await.result(future, 1.second)
}
