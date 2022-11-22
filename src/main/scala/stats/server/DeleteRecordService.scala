package stats.server
import cats.effect.IO
import spice.http.{HttpExchange, HttpStatus}
import stats.controller.EmployeeController

object DeleteRecordService extends Service[String, Unit] {
  override protected def validated: Boolean = true

  override def call(exchange: HttpExchange,
                    employeeName: String): IO[Either[(ServerResponse[Unit], HttpStatus), Unit]] = {
    EmployeeController.deleteByName(employeeName).map(_ => Right(()))
  }
}
