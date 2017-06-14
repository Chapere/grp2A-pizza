package controllersSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import controllers.PizzaController


/**
  * Created by Felix Thomas 04.06.2017.
  */
@RunWith(classOf[JUnitRunner])
class PizzaControllerSpec extends Specification{

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "PizzaController" should {

    "add a Pizza" in memDB {
      val request = FakeRequest(POST, "/addPizza").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis pro cm" -> "0.2",
        "Zutaten" -> "Käse",
        "Kommentar" -> "Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.addPizza()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/newPizzaCreated?id=4&name=Kas&price=0.2&ingredients=K%C3%A4se&comment=Lecker&supplements=Gluten")
    }

    "add a Pizza bad request" in memDB {
      val request = FakeRequest(POST, "/addPizza").withFormUrlEncodedBody(
        "Name" -> "Kas",
        "Preis pro cm" -> "0.2",
        "Zutaten" -> "Käse",
        "Kommentar" -> "Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.addPizza()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "update a Pizza" in memDB {
      val request = FakeRequest(POST, "/updatePizza").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Speck",
        "Preis pro cm" -> "0.3",
        "Zutaten" -> "Speck",
        "Kommentar" -> " Super Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.updatePizza()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/upgradePizza?id=0&name=Speck&price=0.3&ingredients=Speck&comment=+Super+Lecker&supplements=Gluten")
    }

    "update a Pizza bad request" in memDB {
      val request = FakeRequest(POST, "/updatePizza").withFormUrlEncodedBody(
        "Name" -> "Speck",
        "Preis pro cm" -> "0.3",
        "Zutaten" -> "Speck",
        "Kommentar" -> " Super Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.updatePizza()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "remove a Pizza" in memDB {
      val request = FakeRequest(POST, "/rmPizza").withFormUrlEncodedBody(
        "id" -> "0"
      )
      val result = PizzaController.rmPizza()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/pizzaDeleted?deleted=true")
    }

    "remove a Pizza bad request" in memDB {
      val request = FakeRequest(POST, "/rmPizza").withFormUrlEncodedBody(
        "ID" -> "0"
      )
      val result = PizzaController.rmPizza()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "get a Pizza" in memDB {
      val request = FakeRequest(POST, "/selectPizza").withFormUrlEncodedBody(
        "id" -> "0"
      )
      val result = PizzaController.getPizza()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/changePizza?id=0&name=&price=0.0&ingredients=&comment=&supplements=")
    }

    "get a Pizza bad request" in memDB {
      val request = FakeRequest(POST, "/selectPizza").withFormUrlEncodedBody(
        "Id" -> "0"
      )
      val result = PizzaController.getPizza()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "show new Pizza" in memDB {
      val request = FakeRequest(POST, "/addPizza").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis pro cm" -> "0.2",
        "Zutaten" -> "Käse",
        "Kommentar" -> "Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.newPizzaCreated(0,"Kas",0.2,"Käse","Lecker","Gluten")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("0") contain "Käse" contain "Gluten"
    }

    "show view to change Pizza" in memDB {
      val request = FakeRequest(POST, "/addPizza").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis pro cm" -> "0.2",
        "Zutaten" -> "Käse",
        "Kommentar" -> "Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.changePizza1(0,"Speck",0.3,"Speck","Super Lecker","Gluten")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("0") contain "Speck" contain "Super"
    }

    "show changed Pizza" in memDB {
      val request = FakeRequest(POST, "/addPizza").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis pro cm" -> "0.2",
        "Zutaten" -> "Käse",
        "Kommentar" -> "Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.upgradePizza(0,"Speck",0.3,"Speck","Super Lecker","Gluten")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("0") contain "Speck" contain "Gluten"
    }

    "show view for deleted Pizza" in memDB {
      val request = FakeRequest(POST, "/addPizza").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis pro cm" -> "0.2",
        "Zutaten" -> "Käse",
        "Kommentar" -> "Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.pizzaDeleted(true)(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Pizza Gelöscht!")
    }

    "showPizzas" in memDB {
      val request = FakeRequest(POST, "/addPizza").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis pro cm" -> "0.2",
        "Zutaten" -> "Käse",
        "Kommentar" -> "Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.showPizzas()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Pizzasübersicht")
    }

    "show Products" in memDB {
      val request = FakeRequest(POST, "/addPizza").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis pro cm" -> "0.2",
        "Zutaten" -> "Käse",
        "Kommentar" -> "Lecker",
        "Zusatzstoffe" -> "Gluten"
      )
      val result = PizzaController.products()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Produktauswahl")
    }
  }
}

