package stats.server

import fabric.rw.RW

case class Statistics(mean: Double, min: Double, max: Double)

object Statistics {
  implicit val rw: RW[Statistics] = RW.gen
}