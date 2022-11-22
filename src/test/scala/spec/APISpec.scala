package spec

import cats.effect.testing.scalatest.AsyncIOSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import stats.SliceAndDiceApp

class APISpec extends AsyncWordSpec with AsyncIOSpec with Matchers {
  "API" should {
    "start up application" in {
      SliceAndDiceApp.init().map { _ =>
        succeed
      }
    }
    "fail to authenticate" in {
      fail()
    }
    "authenticate and get a token" in {
      fail()
    }
    "get statistics for the default dataset" in {
      fail()
    }
    "get on contract statistics for the default dataset" in {
      fail()
    }
    "get statistics by department" in {
      fail()
    }
    "get statistics by department and sub-department" in {
      fail()
    }
    "log out" in {
      fail()
    }
    "shut down application" in {
      SliceAndDiceApp.dispose().map { _ =>
        succeed
      }
    }
  }
}
