package com.letgo.cqrs.test.dummy.application

import com.letgo.cqrs.query.QueryResponse

case class DummyUserResponse(id: String, name: String) extends QueryResponse

