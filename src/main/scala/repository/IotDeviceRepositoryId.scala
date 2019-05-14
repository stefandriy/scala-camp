package repository

import cats.Id
import domain.IotDevice

import scala.collection.mutable.{Map => MutableMap}

class IotDeviceRepositoryId extends IotDeviceRepository[Id] {
  private val storage: MutableMap[Long, IotDevice] = MutableMap()

  override def registerDevice(userId: Long, serialNumber: String): Id[IotDevice] = {
    val id = nextId()
    val iotDevice = IotDevice(id, userId, serialNumber)
    storage.put(id, iotDevice)
    iotDevice
  }

  override def getById(id: Long): Id[Option[IotDevice]] = storage.get(id)

  override def getBySn(sn: String): Id[Option[IotDevice]] = storage.values.find(device => device.sn == sn)

  override def getByUser(userId: Long): Id[Seq[IotDevice]] =
    storage.values.filter(device => device.userId == userId).toSeq
}
