package com.letgo.cqrs.test

import scala.concurrent.Future

import org.scalamock.handlers.CallHandler1
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.slf4j.helpers.NOPLogger

import com.letgo.cqrs.test.dummy.DummyUser
import com.letgo.cqrs.test.dummy.application.FindDummyUserError
import com.letgo.cqrs.test.dummy.application.FindDummyUserError.DummyUserNotFoundError
import com.letgo.cqrs.test.dummy.domain.find.UserFinder

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

  val userFinder: UserFinder = mock[UserFinder]


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
}
