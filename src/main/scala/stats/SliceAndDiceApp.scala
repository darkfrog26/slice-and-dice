package stats

import cats.effect.{ExitCode, IO, IOApp}
import profig._
import stats.db.DB

object SliceAndDiceApp extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = for {
    _ <- IO(Profig.initConfiguration())
    _ <- IO(Profig.merge(args))
    _ <- DB.init()
    // TODO: Start server
    // TODO: Wait for server to exit
    _ <- DB.shutdown()
  } yield {
    ExitCode.Success
  }
}
