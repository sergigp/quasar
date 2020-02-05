package com.sergigp.quasar.event

trait EventPublisher[P[_]] {
  def publish[E <: Event](event: E): P[Unit]
}
