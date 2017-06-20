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
class PizzasSpecs extends Specification {
  implicit val duration: Timeout = 20 seconds

    "PizzasSpecs" should {

    "respond with a json representation for all pizzas" in new WithApplication {
      val response = route(FakeRequest(GET, "/api/pizzas")).get
      status(response) must equalTo(OK)
      contentType(response) must beSome.which(_ == "application/json")
    }

    "respond with a json representation for an specific pizza" in new WithApplication {
      val response = route(FakeRequest(GET, "/api/pizza/2")).get
      status(response) must equalTo(OK)
      contentType(response) must beSome.which(_ == "application/json")
    }
  }
}