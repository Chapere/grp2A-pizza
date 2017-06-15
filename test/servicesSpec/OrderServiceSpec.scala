package servicesSpec

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.FakeApplication
import play.api.test.Helpers._
import services.OrderService

/**
  * @author Felix Thomas
  */
@RunWith(classOf[JUnitRunner])
class OrderServiceSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The OrderService" should {

    "return an empty list of orders" in memDB {
      OrderService.availableOrder.length must be equalTo(0)
    }

    "create an order and return it" in memDB {
      OrderService.createOrder(1,1,0,"N/A","N/A",1,14,0,0,0,0,"N/A",0,0,"N/A",0,0,"N/A",0,"N/A","N/A","N/A")
      OrderService.availableOrder.length must be equalTo(1)
      OrderService.getOrderbyID(1).pizzaPrice must be equalTo(0.60)
      OrderService.availableOrderByID(1).head.pizzaName must be equalTo("Margherita")
      OrderService.availableOrderByID(1).head.status must be equalTo("Bestellung empfangen")
      OrderService.availableOrderWithAdress.head.adress must be equalTo("Lerchenauer Str. 12")
      OrderService.availableOrderWithAdress.head.city must be equalTo("München")
    }

    "change order status" in memDB {
      OrderService.createOrder(1,1,0,"N/A","N/A",1,14,0,0,0,0,"N/A",0,0,"N/A",0,0,"N/A",0,"N/A","N/A","N/A")
      OrderService.orderSetStaus(1,"ausgeliefert")
      OrderService.getOrderbyID(1).status must be equalTo("ausgeliefert")
    }

    "delete an order" in memDB {
      OrderService.createOrder(1,1,0,"N/A","N/A",1,14,0,0,0,0,"N/A",0,0,"N/A",0,0,"N/A",0,"N/A","N/A","N/A")
      OrderService.availableOrder.length must be equalTo(1)
      OrderService.rmOrder(1)
      OrderService.availableOrder.length must be equalTo(0)
    }

    "deactivate an order" in memDB {
      OrderService.createOrder(1,1,0,"N/A","N/A",1,14,0,0,0,0,"N/A",0,0,"N/A",0,0,"N/A",0,"N/A","N/A","N/A")
      OrderService.availableOrder.length must be equalTo(1)
      OrderService.deactivateOrder(1)
      OrderService.availableOrder.length must be equalTo(1)
      OrderService.getOrderbyID(1).status must be equalTo("Storniert")
    }
  }
}
