package com.letgo.quasar.test.stub

import scala.annotation.tailrec
import scala.util.Random

object StringStub {
  def random(numChars: Int): String = Random.alphanumeric take numChars mkString ""

  @tailrec
  def randomNotNumeric(numChars: Int): String = {
    def isAllDigits(x: String): Boolean = x forall Character.isDigit

    val randomStringCandidate = random(numChars)

    if (isAllDigits(randomStringCandidate)) randomNotNumeric(numChars) else randomStringCandidate
  }
}
