package stats.server

import cats.effect.IO
import spice.http.{HttpExchange, HttpStatus}
import stats.controller.EmployeeController

object SubDepartmentStatisticsService extends Service[StatisticsFilters, Map[String, Map[String, Statistics]]] {
  override protected def validated: Boolean = false

  override def call(exchange: HttpExchange, filters: StatisticsFilters): IO[Either[(ServerResponse[Map[String, Map[String, Statistics]]], HttpStatus), Map[String, Map[String, Statistics]]]] = {
    EmployeeController.statisticsBySubDepartment(filters).map(Right.apply)
  }
}