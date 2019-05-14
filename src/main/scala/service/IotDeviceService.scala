package service

import cats.Monad
import cats.implicits._
import domain.IotDevice
import repository.{IotDeviceRepository, UserRepository}

class IotDeviceService[F[_]](repository: IotDeviceRepository[F],
                             userRepository: UserRepository[F])
                            (implicit monad: Monad[F]) {
  def registerDevice(userId: Long, sn: String): F[Either[String, IotDevice]] = {
    userRepository.getById(userId)
      .flatMap(user => repository.getBySn(sn)
        .flatMap(device =>
          if (user.isEmpty)
            monad.pure(Left("User doesn't exist"))
          else if (device.isDefined)
            monad.pure(Left("Device serial number is already registered"))
          else
            repository.registerDevice(userId, sn).map(Right(_))))
  }
}
