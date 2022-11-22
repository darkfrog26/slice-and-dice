package stats.server
import cats.effect.IO
import spice.http.{HttpExchange, HttpStatus}
import stats.controller.EmployeeController

object DeleteRecordService extends Service[EmployeeName, Unit] {
  override protected def validated: Boolean = false

  override def call(exchange: HttpExchange,
                    employeeName: EmployeeName): IO[Either[(ServerResponse[Unit], HttpStatus), Unit]] = {
    EmployeeController.deleteByName(employeeName.value).map(_ => Right(()))
  }
}