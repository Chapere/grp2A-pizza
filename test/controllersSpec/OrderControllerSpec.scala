package controllersSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import controllers.OrderController
import com.github.nscala_time.time.Imports._
import dbaccess.OrderDao
import models.Order

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

  var time: Int = (2 * (19000 / 1000)) + (10 * 1)
  var deliveryTime = DateTimeFormat.forPattern("kk:mm+-+DD.MM.YYYY").print(DateTime.now() + time.minutes)

  "OrderController" should{

    "create an order" in memDB {
      val request = FakeRequest(POST, "/createOrder").withFormUrlEncodedBody(
        "userID" -> "1",
        "pizzaID" -> "1",
        "productID" -> "0",
        "pizzaAmount" -> "1",
        "pizzaSize" -> "14",
        "productAmount" -> "0",
        "extraOneID" -> "0",
        "extraTwoID" -> "0",
        "extraThreeID" -> "0"
      )
      val result = OrderController.createOrder()(request)
      status(result) must equalTo(SEE_OTHER)
      //redirectLocation(result) must beSome("/newOrderCreated?id=1&customerID=1.0&pizzaID=1.0&productID=0.0&pizzaName=Margherita&productName=&pizzaAmount=1.0&pizzaSize=14.0&pizzaPrice=0.6&productAmount=0.0&productPrice=0.0&extrasName=%28%29&extraTotalPrice=0.0&totalPrice=8.4&orderTime=" + DateTimeFormat.forPattern("kk:mm+-+DD.MM.YYYY").print(DateTime.now()) + "&status=Bestellung+empfangen&deliveryTime=" + deliveryTime)
    }

    "create an order bad request" in memDB {
      val request = FakeRequest(POST, "/createOrder").withFormUrlEncodedBody(
        "pizzaID" -> "1",
        "productID" -> "0",
        "pizzaAmount" -> "1",
        "pizzaSize" -> "14",
        "productAmount" -> "0",
        "extraOneID" -> "0",
        "extraTwoID" -> "0",
        "extraThreeID" -> "0"
      )
      val result = OrderController.createOrder()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "set the status of an order" in memDB {
      val add = OrderDao.createOrder(Order(-1,1,1,0,"N/A", "N/A",1,14,-1,0,-1,0,"N/A", 0,0,"N/A", 0,0,"N/A",0,0,
        DateTimeFormat.forPattern("kk:mm+-+DD.MM.YYYY").print(DateTime.now()),"N/A", deliveryTime))
      val request = FakeRequest(POST, "/setOrderStatus").withFormUrlEncodedBody(
        "orderID" -> "1",
        "orderStatusKZ" -> "Bestellung erhalten"
      )
      val result = OrderController.setStatusOrder()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/allOrderDetails")
    }

    "set the status of an order bad request" in memDB {
      val add = OrderDao.createOrder(Order(-1,1,1,0,"N/A", "N/A",1,14,-1,0,-1,0,"N/A", 0,0,"N/A", 0,0,"N/A",0,0,
        DateTimeFormat.forPattern("kk:mm+-+DD.MM.YYYY").print(DateTime.now()),"N/A", deliveryTime))
      val request = FakeRequest(POST, "/setOrderStatus").withFormUrlEncodedBody(
        "orderStatusKZ" -> "Bestellung erhalten"
      )
      val result = OrderController.setStatusOrder()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }
  }
}
