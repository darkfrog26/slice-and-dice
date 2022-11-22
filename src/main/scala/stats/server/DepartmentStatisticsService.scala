package stats.server

import cats.effect.IO
import spice.http.{HttpExchange, HttpStatus}
import stats.controller.EmployeeController

object DepartmentStatisticsService extends Service[StatisticsFilters, Map[String, Statistics]] {
  override protected def validated: Boolean = false

  override def call(exchange: HttpExchange,
                    filters: StatisticsFilters): IO[Either[(ServerResponse[Map[String, Statistics]], HttpStatus), Map[String, Statistics]]] = {
    EmployeeController.statisticsByDepartment(filters).map(Right.apply)
  }
}