package com.sergigp.quasar.test.dummy.modules.dummyuser.application.find

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.sergigp.quasar.test.dummy.modules.dummyuser.application.DummyUserResponse
import com.sergigp.quasar.test.dummy.modules.dummyuser.domain.find.UserFinder

object QueryHandlers {
  def dummyHandler(userFinder: UserFinder): FindDummyUserQuery => Future[DummyUserResponse] =
    (q: FindDummyUserQuery) => userFinder.find(q.id).map(user => DummyUserResponse(user.id, user.name))

//  def handlerWithService(userAdder: UserAdder, id: String, name: String): AddDummyUserCommand => Future[Unit] =
//    (_: AddDummyUserCommand) => userAdder.add(id, name)
}
