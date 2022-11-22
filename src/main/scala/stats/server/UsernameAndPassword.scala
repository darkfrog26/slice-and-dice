package stats.server

import fabric.rw.RW

case class UsernameAndPassword(username: String, password: String)

object UsernameAndPassword {
  implicit val rw: RW[UsernameAndPassword] = RW.gen
}