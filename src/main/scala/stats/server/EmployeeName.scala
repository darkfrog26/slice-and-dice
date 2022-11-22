package stats.server

import fabric.rw.RW

case class EmployeeName(value: String)

object EmployeeName {
  implicit val rw: RW[EmployeeName] = RW.gen
}