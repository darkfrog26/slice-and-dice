package stats.db

import cats.effect.IO
import com.outr.arango.Graph
import com.outr.arango.upgrade.DatabaseUpgrade
import fabric.io.JsonParser
import fabric.rw._
import spice.streamer._
import stats.model.Employee

import scala.collection.mutable

/**
 * Do initial import of dataset into the database
 */
object InitialDatasetImport extends DatabaseUpgrade {
  override def applyToNew: Boolean = true
  override def blockStartup: Boolean = true

  override def upgrade(graph: Graph): IO[Unit] = for {
    jsonString <- Streamer(
      reader = getClass.getClassLoader.getResourceAsStream("dataset.json"),
      writer = new mutable.StringBuilder
    ).map(_.toString)
    json <- IO(JsonParser(jsonString).asVector.toList)
    employees <- IO(json.map(_.as[Employee]))
    _ <- DB.employees.batch.insert(employees)
  } yield {
    ()
  }
}
