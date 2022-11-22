package stats.db

import cats.effect.IO
import com.outr.arango.Graph
import com.outr.arango.upgrade.DatabaseUpgrade
import stats.controller.UserController

object CreateDefaultUser extends DatabaseUpgrade {
  override def applyToNew: Boolean = true
  override def blockStartup: Boolean = true

  override def upgrade(graph: Graph): IO[Unit] = UserController
    .create("clipboard", "clipboard")
    .map(_ => ())
}
