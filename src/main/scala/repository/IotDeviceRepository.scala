package repository

import java.util.concurrent.atomic.AtomicLong

import domain.IotDevice

trait IotDeviceRepository[F[_]] {
  private val sequence = new AtomicLong

  protected def nextId(): Long = sequence.getAndIncrement

  def registerDevice(userId: Long, serialNumber: String): F[IotDevice]

  def getById(id: Long): F[Option[IotDevice]]

  def getBySn(sn: String): F[Option[IotDevice]]

  def getByUser(userId: Long): F[Seq[IotDevice]]
}