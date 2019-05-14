package repository

import domain.IotDevice

import scala.collection.mutable.{Map => MutableMap}
import scala.concurrent.{ExecutionContext, Future}

class IotDeviceRepositoryFuture(implicit ec: ExecutionContext) extends IotDeviceRepository[Future] {
  private val storage: MutableMap[Long, IotDevice] = MutableMap()

  override def registerDevice(userId: Long, serialNumber: String): Future[IotDevice] = Future {
    val id = nextId()
    val iotDevice = IotDevice(id, userId, serialNumber)
    storage.put(id, iotDevice)
    iotDevice
  }

  override def getById(id: Long): Future[Option[IotDevice]] = Future.successful(storage.get(id))

  override def getBySn(sn: String): Future[Option[IotDevice]] =
    Future.successful(storage.values.find(device => device.sn == sn))

  override def getByUser(userId: Long): Future[Seq[IotDevice]] =
    Future.successful(storage.values.filter(device => device.userId == userId).toSeq)
}
