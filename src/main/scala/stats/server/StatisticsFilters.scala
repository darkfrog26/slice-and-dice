package stats.server

import fabric.rw.RW

case class StatisticsFilters(onContract: Option[Boolean] = None, currency: Option[String] = None)

object StatisticsFilters {
  implicit val rw: RW[StatisticsFilters] = RW.gen
}