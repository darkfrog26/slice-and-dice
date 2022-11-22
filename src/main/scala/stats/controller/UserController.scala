package stats.controller

import cats.effect.IO
import com.outr.arango.query._
import com.outr.scalapass.Argon2PasswordFactory
import stats.db.DB
import stats.model.{Authenticated, User}

object UserController {
  private lazy val passwordFactory = Argon2PasswordFactory()

  /**
   * Creates a new user account
   *
   * @param username the username to use. Must be at least 4 characters in length.
   * @param password the password to use. Must be at least 8 characters in length.
   * @return IO[User]
   */
  def create(username: String, password: String): IO[User] = {
    assert(username.length >= 4, "Username must be at least 4 characters")
    assert(password.length >= 8, "Password must be at least 8 characters")
    val user = User(username.toLowerCase, passwordFactory.hash(password))
    DB.users.insert(user).map(_ => user)
  }

  /**
   * Verify and retrieve the user account for the supplied username and password.
   */
  def verify(username: String, password: String): IO[Option[User]] = byUsername(username).map {
    case Some(user) if passwordFactory.verify(password, user.password) => Some(user)
    case _ => None
  }

  /**
   * Verifies the username and password and then creates an Authenticated entry if successful for token authentication
   */
  def authenticate(username: String, password: String): IO[Option[Authenticated]] = verify(username, password).flatMap {
    case Some(user) =>
      val auth = Authenticated(user._id)
      DB.authenticated.insert(auth).map(_ => Some(auth))
    case _ => IO.pure(None)
  }

  /**
   * Looks up a user by username
   */
  def byUsername(username: String): IO[Option[User]] = {
    val q =
      aql"""
          FOR u IN ${DB.users}
          FILTER u.${User.username} == ${username.toLowerCase}
          RETURN u
         """
    DB.users.query(q).first
  }

  /**
   * Verifies a user by a token
   */
  def byToken(token: String): IO[Option[User]] = {
    val q =
      aql"""
          FOR a IN ${DB.authenticated}
          FILTER a.${Authenticated.token} == $token
          RETURN DOCUMENT(a.${Authenticated.userId})
         """
    DB.users.query(q).first
  }

  def logOut(token: String): IO[Unit] = {
    val q =
      aql"""
          FOR a IN ${DB.authenticated}
          FILTER a.${Authenticated.token} == $token
          REMOVE a IN ${DB.authenticated}
         """
    DB.execute(q)
  }
}