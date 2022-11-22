package stats.db

import com.outr.arango.Graph
import com.outr.arango.collection.DocumentCollection
import com.outr.arango.upgrade.DatabaseUpgrade
import stats.model.Employee

object DB extends Graph("slice-and-dice") {
  val employees: DocumentCollection[Employee] = vertex(Employee)

  override def upgrades: List[DatabaseUpgrade] = List(
    InitialDatasetImport
  )
}