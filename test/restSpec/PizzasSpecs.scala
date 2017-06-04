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
class PizzasSpecs extends Specification {

  "PizzasSpecs" should {

    "respond with a json representation for all pizzas" in new WithApplication{
      val response = route(FakeRequest(GET, "/api/pizzas")).get
      status(response) must equalTo(OK)
      contentType(response) must beSome.which(_ == "application/json")
    }

    "respond with a json representation for an specific pizza" in new WithApplication{
      val response = route(FakeRequest(GET, "/api/pizza/2")).get
      status(response) must equalTo(OK)
      contentType(response) must beSome.which(_ == "application/json")
    }
  }
}