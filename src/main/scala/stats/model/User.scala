package stats.model

import com.outr.arango.{Document, DocumentModel, Field, Id, Index}
import fabric.rw.RW

case class User(username: String,
                password: String,
                _id: Id[User] = User.id()) extends Document[User]

object User extends DocumentModel[User] {
  override val collectionName: String = "users"

  val username: Field[String] = field("username")
  val password: Field[String] = field("password")

  override implicit val rw: RW[User] = RW.gen

  override def indexes: List[Index] = List(
    username.index.persistent(unique = true)
  )
}