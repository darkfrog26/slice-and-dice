package stats.server

import spice.http.server.MutableHttpServer
import spice.http.server.dsl._
import spice.net._

object WebServer extends MutableHttpServer {
  handler(
    filters(
      path"/dataset/create" / CreateRecordService,
      path"/dataset/delete" / DeleteRecordService,
      path"/dataset/statistics" / StatisticsService,
      path"/dataset/statistics/department" / DepartmentStatisticsService,
      path"/dataset/statistics/subdepartment" / SubDepartmentStatisticsService,

      path"/user/authenticate" / AuthenticateUserService,
      path"/user/logout" / LogOutUserService
    )
  )
}