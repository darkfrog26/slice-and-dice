package stats

import cats.effect.{ExitCode, IO, IOApp}
import profig._
import stats.db.DB
import stats.server.WebServer

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
    _ <- DB.init()
    _ <- WebServer.start()
  } yield {
    ()
  }

  def dispose(): IO[Unit] = for {
    _ <- IO(WebServer.dispose())
    _ <- DB.shutdown()
  } yield {
    ()
  }
}
