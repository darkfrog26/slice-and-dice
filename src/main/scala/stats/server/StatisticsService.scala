package stats.server
import cats.effect.IO
import spice.http.{HttpExchange, HttpStatus}
import stats.controller.EmployeeController

object StatisticsService extends Service[StatisticsFilters, Statistics] {
  override protected def validated: Boolean = true

  override def call(exchange: HttpExchange,
                    filters: StatisticsFilters): IO[Either[(ServerResponse[Statistics], HttpStatus), Statistics]] = {
    EmployeeController.statistics(filters).map(Right.apply)
  }
}