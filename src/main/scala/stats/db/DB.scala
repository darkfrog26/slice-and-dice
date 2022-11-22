package stats.db

import com.outr.arango.Graph
import com.outr.arango.collection.DocumentCollection
import com.outr.arango.upgrade.DatabaseUpgrade
import stats.model.{Authenticated, Employee, User}

object DB extends Graph("slice-and-dice") {
  val employees: DocumentCollection[Employee] = vertex(Employee)
  val users: DocumentCollection[User] = vertex(User)
  val authenticated: DocumentCollection[Authenticated] = vertex(Authenticated)

  override def upgrades: List[DatabaseUpgrade] = List(
    InitialDatasetImport, CreateDefaultUser
  )
}