package com.letgo.cqrs.test.dummy.application.find

import com.letgo.cqrs.query.Query
import com.letgo.cqrs.test.dummy.application.DummyUserResponse
import com.letgo.cqrs.test.dummy.application.FindDummyUserError.FindDummyUserError

case class FindUserDummyQuery(id: String) extends Query {
  override type QueryError = FindDummyUserError
  override type QueryResponse = DummyUserResponse
}
