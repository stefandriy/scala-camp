package service

import domain.IotDevice
import repository.{IotDeviceRepository, UserRepository}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class IotDeviceService(repository: IotDeviceRepository[Future], userRepository: UserRepository) {

  def registerDevice(userId: Long, sn: String): Future[Either[String, IotDevice]] = {
    userRepository.findById(userId)
      .flatMap(user => repository.getBySn(sn)
        .flatMap(device =>
          if (user.isEmpty)
            Future.successful(Left("User doesn't exist"))
          else if (device.isDefined)
            Future.successful(Left("Device serial number is already registered"))
          else
            repository.registerDevice(userId, sn).map(Right(_))))
  }
}
