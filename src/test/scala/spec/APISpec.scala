package spec

import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import spice.http.client.HttpClient
import spice.net._
import stats.SliceAndDiceApp
import stats.db.DB
import stats.model.Employee
import stats.server.{EmployeeName, ServerResponse, Statistics, StatisticsFilters, UsernameAndPassword}

import scala.util.{Failure, Success}

class APISpec extends AsyncWordSpec with AsyncIOSpec with Matchers {
  private var token: Option[String] = None

  private def client = {
    val client = HttpClient.url(url"http://localhost:8080").failOnHttpStatus(false)
    token match {
      case Some(t) => client.header("token", t)
      case None => client
    }
  }

  "API" should {
    "start up application" in {
      SliceAndDiceApp.init().map { _ =>
        succeed
      }
    }
    "fail to authenticate" in {
      client.path(path"/user/authenticate").restful[UsernameAndPassword, ServerResponse[String]](
        UsernameAndPassword("admin", "admin")
      ).map {
        case Success(response) =>
          response.success should be(false)
          response.data should be(None)
          response.message should be(Some("Authentication failed"))
          response.validationErrors should be(Nil)
        case Failure(exception) => fail(exception)
      }
    }
    "authenticate and get a token" in {
      client.path(path"/user/authenticate").restful[UsernameAndPassword, ServerResponse[String]](
        UsernameAndPassword("clipboard", "clipboard")
      ).map {
        case Success(response) =>
          response.success should be(true)
          response.data should not be None
          response.message should be(None)
          response.validationErrors should be(Nil)
          token = response.data
        case Failure(exception) => fail(exception)
      }
    }
    "get statistics for the default dataset" in {
      client.path(path"/dataset/statistics").restful[StatisticsFilters, ServerResponse[Statistics]](
        StatisticsFilters()
      ).map {
        case Success(response) =>
          response.success should be(true)
          response.data should be(Some(Statistics(2.229501E7, 30.0, 2.0E8)))
        case Failure(exception) => fail(exception)
      }
    }
    "get on contract statistics for the default dataset" in {
      client.path(path"/dataset/statistics").restful[StatisticsFilters, ServerResponse[Statistics]](
        StatisticsFilters(onContract = Some(true))
      ).map {
        case Success(response) =>
          response.success should be(true)
          response.data should be(Some(Statistics(100000.0, 90000.0, 110000.0)))
        case Failure(exception) => fail(exception)
      }
    }
    "get statistics by department" in {
      client.path(path"/dataset/statistics/department").restful[StatisticsFilters, ServerResponse[Map[String, Statistics]]](
        StatisticsFilters()
      ).map {
        case Success(response) =>
          response.success should be(true)
          response.data should be(Some(Map(
            "Administration" -> Statistics(30.0, 30.0, 30.0),
            "Banking" -> Statistics(90000.0, 90000.0, 90000.0),
            "Engineering" -> Statistics(4.0099006E7, 30.0, 2.0E8),
            "Operations" -> Statistics(35015.0, 30.0, 70000.0)
          )))
        case Failure(exception) => fail(exception)
      }
    }
    "get statistics by department and sub-department" in {
      client.path(path"/dataset/statistics/subdepartment").restful[StatisticsFilters, ServerResponse[Map[String, Map[String, Statistics]]]](
        StatisticsFilters()
      ).map {
        case Success(response) =>
          response.success should be(true)
          response.data should be(Some(Map(
            "Engineering" -> Map("Platform" -> Statistics(4.0099006E7, 30.0, 2.0E8)),
            "Operations" -> Map("CustomerOnboarding" -> Statistics(35015.0, 30.0, 70000.0)),
            "Administration" -> Map("Agriculture" -> Statistics(30.0, 30.0, 30.0)),
            "Banking" -> Map("Loan" -> Statistics(90000.0, 90000.0, 90000.0))
          )))
        case Failure(exception) => fail(exception)
      }
    }
    "create a new employee" in {
      client.path(path"/dataset/create").restful[Employee, ServerResponse[Unit]](
        Employee("testuser1", 1234, "USD", "Basket Weaving", None)
      ).map {
        case Success(response) => response should be(ServerResponse(
          success = true, message = None, data = None, validationErrors = Nil
        ))
        case Failure(exception) => fail(exception)
      }
    }
    "delete the new employee" in {
      client.path(path"/dataset/delete").restful[EmployeeName, ServerResponse[Unit]](
        EmployeeName("testuser1")
      ).map {
        case Success(response) => response.success should be(true)
        case Failure(exception) => fail(exception)
      }
    }
    "log out" in {
      client.path(path"/user/logout").restful[Unit, ServerResponse[Unit]](()).map {
        case Success(response) => response.success should be(true)
        case Failure(exception) => fail(exception)
      }
    }
    "shut down application" in {
      SliceAndDiceApp.dispose().map { _ =>
        succeed
      }
    }
  }
}
