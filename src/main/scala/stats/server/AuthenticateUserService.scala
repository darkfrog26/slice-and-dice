package stats.server
import cats.effect.IO
import spice.http.{HttpExchange, HttpStatus}
import stats.controller.UserController

object AuthenticateUserService extends Service[UsernameAndPassword, String] {
  override protected def validated: Boolean = false

  override def call(exchange: HttpExchange,
                    request: UsernameAndPassword): IO[Either[(ServerResponse[String], HttpStatus), String]] =
    UserController.authenticate(
      username = request.username,
      password = request.password
    ).map {
      case Some(authenticated) => Right(authenticated.token)
      case None => Left((ServerResponse(
        success = false,
        message = Some("Authentication failed"),
        data = None,
        validationErrors = Nil
      ), HttpStatus.Forbidden))
    }
}