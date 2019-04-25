package com.sergigp.quasar.test

import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.DummyUser
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.find.UserFinder
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.slf4j.helpers.NOPLogger
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.add.AddDummyUserError.AddDummyUserError
import com.sergigp.quasar.test.dummy.modules.dummyuser.application.find.FindDummyUserError.DummyUserNotFoundError
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.add.UserAdder
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.send.EmailSender

abstract class TestCase
  extends WordSpecLike
  with Matchers
  with OptionValues
  with ScalaFutures
  with MockFactory
  with Eventually
  with GivenWhenThen
  with EitherValues
  with OneInstancePerTest
  with ParallelTestExecution {

  val logger: org.slf4j.Logger = NOPLogger.NOP_LOGGER

  val userFinder: UserFinder   = mock[UserFinder]
  val userAdder: UserAdder     = mock[UserAdder]
  val emailSender: EmailSender = mock[EmailSender]

  def shouldFindUser(userId: String, user: DummyUser): Unit =
    (userFinder.find _)
      .expects(userId)
      .once()
      .returns(Future.successful(Right(user)))

  def shouldNotFindUser(userId: String): Unit =
    (userFinder.find _)
      .expects(userId)
      .once()
      .returns(Future.successful(Left(DummyUserNotFoundError(userId))))

  def shouldAddUser(userId: String, name: String, email: String): Unit =
    (userAdder.add _)
      .expects(userId, name, email)
      .once()
      .returns(Future.successful(Right(())))


  def shouldReturnErrorAddingUser(userId: String, name: String, email: String, error: AddDummyUserError): Unit =
    (userAdder.add _)
      .expects(userId, name, email)
      .once()
      .returns(Future.successful(Left(error)))

  def shouldSendEmail(email: String): Unit =
    (emailSender.send _)
      .expects(email)
      .once()
      .returns(Future.successful(()))

  def shouldReturnErrorSendingEmail(email: String, error: Throwable): Unit =
    (emailSender.send _)
      .expects(email)
      .once()
      .returns(Future.failed(error))
}
