package org.msgpack.core

import org.scalatest._
import xerial.core.log.Logger
import xerial.core.util.{TimeReport, Timer}
import scala.language.implicitConversions

trait MessagePackSpec extends WordSpec with Matchers with GivenWhenThen with OptionValues with BeforeAndAfter with Benchmark with Logger {

  implicit def toTag(s:String) : Tag = Tag(s)

  def toHex(arr:Array[Byte]) = arr.map(x => f"$x%02x").mkString(" ")

}


trait Benchmark extends Timer {

  val numWarmUpRuns = 10

  override protected def block[A](name: String, repeat: Int)(f: => A): TimeReport = {
    var i = 0
    while(i < numWarmUpRuns) {
      f
      i += 1
    }

    super.block(name, repeat)(f)

  }

}