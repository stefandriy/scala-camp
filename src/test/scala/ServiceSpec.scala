import cats.implicits._
import org.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ServiceSpec extends FlatSpec with Matchers {

  "UserRepositoryId" should "save and retrieve User" in {
    val username = "username"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val userRepository = new UserRepositoryId()
    val savedUser = userRepository.registerUser(username)
    savedUser.username shouldEqual username
    userRepository.getById(savedUser.id) shouldEqual Option(savedUser)
    userRepository.getByUsername(username) shouldEqual Option(savedUser)
    userRepository.getById(wrongId) shouldEqual Option.empty
    userRepository.getByUsername(wrongUsername) shouldEqual Option.empty
  }

  "UserRepositoryFuture" should "save and retrieve User" in {
    val username = "username"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val userRepository = new UserRepositoryFuture()
    val savedUser = await(userRepository.registerUser(username))
    savedUser.username shouldEqual username
    await(userRepository.getById(savedUser.id)) shouldEqual Option(savedUser)
    await(userRepository.getByUsername(username)) shouldEqual Option(savedUser)
    await(userRepository.getById(wrongId)) shouldEqual Option.empty
    await(userRepository.getByUsername(wrongUsername)) shouldEqual Option.empty
  }

  "IotDeviceRepositoryId" should "save and retrieve IotDevice" in {
    val userId = 1
    val sn = "serial number"
    val wrongId = 999
    val wrongSn = "bla-bla-bla"
    val deviceRepository = new IotDeviceRepositoryId()
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

  "IotDeviceRepositoryFuture" should "save and retrieve IotDevice" in {
    val userId = 1
    val sn = "serial number"
    val wrongId = 999
    val wrongSn = "bla-bla-bla"
    val deviceRepository = new IotDeviceRepositoryFuture()
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

  "UserServiceId" should "save and retrieve User" in {
    val username = "username"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val userService = new UserService(new UserRepositoryId())
    val savedUser = userService.registerUser(username).right.get
    savedUser.username shouldEqual username
    userService.getById(savedUser.id) shouldEqual Option(savedUser)
    userService.getByUsername(username) shouldEqual Option(savedUser)
    userService.registerUser(username).left.get shouldEqual s"User $savedUser already exists."
    userService.getById(wrongId) shouldEqual Option.empty
    userService.getByUsername(wrongUsername) shouldEqual Option.empty
  }

  "UserServiceFuture" should "save and retrieve User" in {
    val username = "username"
    val wrongUsername = "bla-bla-bla"
    val wrongId = 999
    val userService = new UserService(new UserRepositoryFuture())
    val savedUser = await(userService.registerUser(username)).right.get
    savedUser.username shouldEqual username
    await(userService.getById(savedUser.id)) shouldEqual Option(savedUser)
    await(userService.getByUsername(username)) shouldEqual Option(savedUser)
    await(userService.registerUser(username)).left.get shouldEqual s"User $savedUser already exists."
    await(userService.getById(wrongId)) shouldEqual Option.empty
    await(userService.getByUsername(wrongUsername)) shouldEqual Option.empty
  }

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
