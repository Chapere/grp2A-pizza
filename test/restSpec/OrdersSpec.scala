package restSpec

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers.{GET, OK, contentType, route, status}
import play.api.test.FakeRequest
import play.test.WithApplication

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  * @author Taha Obed
  */
@RunWith(classOf[JUnitRunner])
class OrdersSpec extends Specification {

  "OrdersSpec" should {

    "respond with a json representation for all orders" in new WithApplication {
      val response = route(FakeRequest(GET, "/api/orders")).get
      status(response) must beEqualTo(OK)
      contentType(response) must beSome.which(_ == "application/json")
    }

    /*
    "respond with a json representation for a specific order" in new WithApplication {
      val response = route(FakeRequest(GET, "/api/order/1")).get
      status(response) must beEqualTo(OK)
      contentType(response) must beSome.which(_== "application/json")
    }
    */
  }
}
