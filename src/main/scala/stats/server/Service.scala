package stats.server

import cats.effect.IO
import spice.ValidationError
import spice.http.{HeaderKey, HttpExchange, HttpStatus, StringHeaderKey}
import spice.http.server.rest.{Restful, RestfulResponse}
import stats.controller.UserController
import stats.model.User

trait Service[Request, Data] extends Restful[Request, ServerResponse[Data]] {
  protected def validated: Boolean

  implicit class HttpExchangeExtras(exchange: HttpExchange) {
    def token: Option[String] = exchange.request.headers.first(Service.tokenKey)
    def verifyToken(): IO[Option[User]] = token match {
      case Some(token) => UserController.byToken(token)
      case None => IO.pure(None)
    }
    def authorized(): IO[Boolean] = if (validated) {
      verifyToken().map(_.nonEmpty)
    } else {
      IO.pure(true)
    }
  }

  override final def apply(exchange: HttpExchange, request: Request): IO[RestfulResponse[ServerResponse[Data]]] = {
    exchange.authorized().flatMap {
      case true => call(exchange, request).map {
        case Left((serverResponse, status)) => RestfulResponse(serverResponse, status)
        case Right(data) => RestfulResponse(
          ServerResponse(success = true, message = None, data = Some(data), validationErrors = Nil), HttpStatus.OK
        )
      }
      case false if exchange.token.isEmpty =>
        IO(error(List(ValidationError("No token specified in the header")), HttpStatus.Forbidden))
      case false =>
        IO(error(List(ValidationError("Invalid token")), HttpStatus.Forbidden))
    }
  }

  def call(exchange: HttpExchange, request: Request): IO[Either[(ServerResponse[Data], HttpStatus), Data]]

  override def error(errors: List[ValidationError], status: HttpStatus): RestfulResponse[ServerResponse[Data]] =
    RestfulResponse(
      ServerResponse(
        success = false,
        message = Some("Validation errors"),
        data = None,
        validationErrors = errors
      ),
      status
    )
}

object Service {
  val tokenKey: StringHeaderKey = HeaderKey("token")
}
