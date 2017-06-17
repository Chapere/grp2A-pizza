package restSpec

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers.{OK, GET, status, contentType, route}
import play.api.test.{FakeRequest, WithApplication}

/**
  * @author Felix Thomas
  */
@RunWith(classOf[JUnitRunner])
class ProductsSpec extends Specification {

  "ProductsSpecs" should {

    "respond with a json representation for all products" in new WithApplication{
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
