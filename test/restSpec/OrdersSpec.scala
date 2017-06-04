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

    "respond with a json representation for a specific order" in new WithApplication {
      val response = route(FakeRequest(GET, "/api/order/1")).get
      status(response) must beEqualTo(OK)
      contentType(response) must beSome.which(_== "application/json")
    }
  }
}
