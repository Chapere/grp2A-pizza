package restSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
@RunWith(classOf[JUnitRunner])
class UsersSpec extends Specification {

  "UserSpec" should {

    "respond with a json representation" in new WithApplication {
      val response = route(FakeRequest(GET, "/api/users")).get
      status(response) must beEqualTo(OK)
      contentType(response) must beSome.which(_== "application/json")
    }
  }
}
