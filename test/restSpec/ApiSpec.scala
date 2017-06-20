package restSpec

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers.{GET, OK, contentType, route, status, writeableOf_AnyContentAsEmpty}
import play.api.test.{FakeRequest, WithApplication}
import akka.util.Timeout
import scala.concurrent.duration.DurationLong

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApiSpec extends Specification {
  implicit val duration: Timeout = 20 seconds

  "Api" should {

    "respond with a json representation" in new WithApplication{
      val response = route(FakeRequest(GET, "/api")).get
      status(response) must equalTo(OK)
      contentType(response) must beSome.which(_ == "application/json")
    }
  }
}

