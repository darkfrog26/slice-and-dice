package stats.controller

import cats.effect.IO
import com.outr.arango.query._
import com.outr.arango.query.dsl._
import fabric.Json
import fabric.rw.Asable
import stats.db.DB
import stats.model.Employee
import stats.server.{Statistics, StatisticsFilters}

object EmployeeController {
  def create(employee: Employee): IO[Employee] = DB
    .employees
    .insert(employee)
    .map(_ => employee)

  def statistics(filters: StatisticsFilters): IO[Statistics] = {
    val e = Employee.ref
    val subQuery = aql {
      FOR (e) IN DB.employees
      filters.onContract.foreach { b =>
        FILTER(e.on_contract === b)
      }
      filters.currency.foreach { c =>
        FILTER(e.currency === c)
      }
      RETURN(e.salary)
    }
    val q =
      aql"""
          LET salaries = (
            $subQuery
          )
          RETURN {
            mean: AVERAGE(salaries),
            min: MIN(salaries),
            max: MAX(salaries)
          }
         """
    DB.query[Statistics](q).one
  }

  def statisticsByDepartment(filters: StatisticsFilters): IO[Map[String, Statistics]] = {
    val e = Employee.ref
    val subQuery = aql {
      FOR(e) IN DB.employees
      filters.onContract.foreach { b =>
        FILTER(e.on_contract === b)
      }
      filters.currency.foreach { c =>
        FILTER(e.currency === c)
      }
    }
    val q =
      aql"""
          LET departments = UNIQUE(
            FOR e IN ${DB.employees}
            RETURN e.${Employee.department}
          )
          FOR d IN departments
          LET salaries = (
            $subQuery
            FILTER e.${Employee.department} == d
            RETURN e.${Employee.salary}
          )
          RETURN {
            department: d,
            mean: AVERAGE(salaries),
            min: MIN(salaries),
            max: MAX(salaries)
          }
         """
    DB.query[Json](q).all.map { list =>
      list.map { json =>
        json("department").asString -> json.as[Statistics]
      }.toMap
    }
  }

  def statisticsBySubDepartment(filters: StatisticsFilters): IO[Map[String, Map[String, Statistics]]] = {
    val e = Employee.ref
    val subQuery = aql {
      FOR(e) IN DB.employees
      filters.onContract.foreach { b =>
        FILTER(e.on_contract === b)
      }
      filters.currency.foreach { c =>
        FILTER(e.currency === c)
      }
    }
    val q =
      aql"""
          LET departments = UNIQUE(
            FOR e IN ${DB.employees}
            RETURN e.${Employee.department}
          )
          FOR d IN departments
          LET subDepartments = UNIQUE(
            FOR e IN ${DB.employees}
            FILTER e.${Employee.department} == d
            RETURN e.${Employee.sub_department}
          )
          FOR sd IN subDepartments
          LET salaries = (
            $subQuery
            FILTER e.${Employee.department} == d
            FILTER e.${Employee.sub_department} == sd
            RETURN e.${Employee.salary}
          )
          RETURN {
            department: d,
            subDepartment: sd,
            mean: AVERAGE(salaries),
            min: MIN(salaries),
            max: MAX(salaries)
          }
         """
    DB.query[Json](q).all.map { list =>
      list.map { json =>
        (json("department").asString, json("subDepartment").asString) -> json.as[Statistics]
      }.toMap.groupBy(_._1._1).map {
        case (department, map) => department -> map.map {
          case (key, stats) => key._2 -> stats
        }
      }
    }
  }

  def deleteByName(name: String): IO[Unit] = {
    scribe.info(s"Deleting where name is $name")
    val q =
      aql"""
          FOR e IN ${DB.employees}
          FILTER e.${Employee.name} == $name
          REMOVE e IN ${DB.employees}
         """
    DB.execute(q)
  }
}