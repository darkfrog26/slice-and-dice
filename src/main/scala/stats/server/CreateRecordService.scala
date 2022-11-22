package stats.server

import cats.effect.IO
import spice.http.{HttpExchange, HttpStatus}
import stats.controller.EmployeeController
import stats.model.Employee

object CreateRecordService extends Service[Employee, Unit] {
  override protected def validated: Boolean = true

  override def call(exchange: HttpExchange,
                    employee: Employee): IO[Either[(ServerResponse[Unit], HttpStatus), Unit]] =
    EmployeeController.create(employee).map(_ => Right(()))
}