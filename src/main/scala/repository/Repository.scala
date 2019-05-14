package repository

import java.util.concurrent.atomic.AtomicLong

trait Repository {
  private val sequence = new AtomicLong

  protected def nextId(): Long = sequence.getAndIncrement
}
