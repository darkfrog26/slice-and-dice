package stats.server

import fabric.rw._
import spice.ValidationError

case class ServerResponse[Data](success: Boolean,
                                message: Option[String],
                                data: Option[Data],
                                validationErrors: List[ValidationError] = Nil)

object ServerResponse {
  implicit def rw[Data](implicit drw: RW[Data]): RW[ServerResponse[Data]] = RW.gen
}
