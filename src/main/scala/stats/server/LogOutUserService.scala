package stats.server
import cats.effect.IO
import spice.http.{HttpExchange, HttpStatus}
import stats.controller.UserController

object LogOutUserService extends Service[Unit, Unit] {
  override protected def validated: Boolean = true

  override def call(exchange: HttpExchange,
                    request: Unit): IO[Either[(ServerResponse[Unit], HttpStatus), Unit]] = {
    val token = exchange.token.get
    UserController.logOut(token).map { _ =>
      Right(())
    }
  }
}
