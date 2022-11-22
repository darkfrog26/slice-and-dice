package stats.model

import com.outr.arango.{Document, DocumentModel, Field, Id, Index, Unique}
import fabric.rw.RW

case class Authenticated(userId: Id[User],
                         token: String = Authenticated.generateToken(),
                         created: Long = System.currentTimeMillis(),
                         _id: Id[Authenticated] = Authenticated.id()) extends Document[Authenticated]

object Authenticated extends DocumentModel[Authenticated] {
  override val collectionName: String = "authenticated"

  val token: Field[String] = field("token")
  val userId: Field[Id[User]] = field("userId")
  val created: Field[Long] = field("created")

  def generateToken(): String = Unique(secure = true)

  override implicit val rw: RW[Authenticated] = RW.gen

  override def indexes: List[Index] = List(
    token.index.persistent(unique = true),
    userId.index.persistent(),
    created.index.persistent()
  )
}
