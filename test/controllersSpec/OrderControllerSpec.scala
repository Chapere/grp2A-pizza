package controllersSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import controllers.OrderController

/**
  * Created by ifw15124 on 20.04.2017.
  */
@RunWith(classOf[JUnitRunner])
class OrderControllerSpec extends Specification{

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "OrderController" should{

    "create an order" in memDB {
      val request = FakeRequest(POST, "/createOrder").withFormUrlEncodedBody(
        "userID" -> "1",
        "pizzaID" -> "1",
        "productID" -> "0",
        "pizzaAmount" -> "1",
        "pizzaSize" -> "14",
        "productAmount" -> "1",
        "extraOneID" -> "0",
        "extraTwoID" -> "0",
        "extraThreeID" -> "0"
      )
      val result = OrderController.createOrder()(request)
      status(result) must equalTo(SEE_OTHER)
      //contentAsString(result) must contain ("")
    }
  }
}
