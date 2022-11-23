package stats

import cats.effect.{ExitCode, IO, IOApp}
import com.outr.arango.core.ArangoDBConfig
import profig._
import spice.http.client.HttpClient
import spice.net.{URL, URLParser, interpolation}
import stats.db.DB
import stats.server.WebServer

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object SliceAndDiceApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- init(args)
    _ <- WebServer.whileRunning()
    _ <- DB.shutdown()
  } yield {
    ExitCode.Success
  }

  def init(args: List[String] = Nil): IO[Unit] = for {
    _ <- IO(Profig.initConfiguration())
    _ <- IO(Profig.merge(args))
    _ = scribe.info("Waiting for database...")
    _ <- waitForDatabase()
    _ = scribe.info("Database online! Continuing!")
    _ <- DB.init()
    _ <- WebServer.start()
  } yield {
    ()
  }

  private lazy val dbHost = ArangoDBConfig().hosts.head

  private def waitForDatabase(attempts: Int = 0): IO[Unit] = HttpClient
    .url(URLParser(s"http://${dbHost.host}:${dbHost.port}/_api/version", validateTLD = false).toOption.get)
    .send()
    .flatMap {
      case Success(_) => IO.unit
      case Failure(exception) =>
        if (attempts > 20) {
          throw new RuntimeException("Failing to connect to database. Timing out!")
        }
        scribe.warn(s"Error connecting to database: ${exception.getMessage}. Trying again in five seconds...")
        IO.sleep(5.seconds).flatMap(_ => waitForDatabase(attempts + 1))
    }
    .recoverWith { throwable =>
      if (attempts > 20) {
        throw new RuntimeException("Failing to connect to database. Timing out!")
      }
      scribe.warn(s"Error connecting to database: ${throwable.getMessage}. Trying again in five seconds...")
      IO.sleep(5.seconds).flatMap(_ => waitForDatabase(attempts + 1))
    }

  def dispose(): IO[Unit] = for {
    _ <- IO(WebServer.dispose())
    _ <- DB.shutdown()
  } yield {
    ()
  }
}
