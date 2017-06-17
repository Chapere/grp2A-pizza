package controllersSpec

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers.{OK, POST, SEE_OTHER, BAD_REQUEST, redirectLocation, contentAsString, running, status}
import play.api.test.{FakeApplication, FakeRequest}
import controllers.ProductController
import scala.concurrent.duration._
import akka.util.Timeout

/**
  * @author Felix Thomas
  */
@RunWith(classOf[JUnitRunner])
class ProductControllerSpec extends Specification{
  implicit val duration: Timeout = 20 seconds

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "ProductController" should {

    "add a Product" in memDB {
      val request = FakeRequest(POST, "/addProduct").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "3",
        "Größe" -> "500",
        "Einheit" -> "g"
      )
      val result = ProductController.addProduct()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/newProductCreated?id=6&name=Kas&price=3.0&size=500.0&unit=g")
    }

    "add a Product bad request" in memDB {
      val request = FakeRequest(POST, "/addProduct").withFormUrlEncodedBody(
        "Name" -> "Kas",
        "Preis" -> "3",
        "Größe" -> "500",
        "Einheit" -> "g"
      )
      val result = ProductController.addProduct()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "update a Product" in memDB {
      val request = FakeRequest(POST, "/updateProduct").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Nuggets",
        "Preis" -> "2",
        "Größe" -> "300",
        "Einheit" -> "g"
      )
      val result = ProductController.updateProduct()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/upgradeProduct?id=0&name=Nuggets&price=2.0&size=300.0&unit=g")
    }

    "update a Product bad request" in memDB {
      val request = FakeRequest(POST, "/updateProduct").withFormUrlEncodedBody(
        "Name" -> "Kas",
        "Preis" -> "3",
        "Größe" -> "500",
        "Einheit" -> "g"
      )
      val result = ProductController.updateProduct()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "remove a Product" in memDB {
      val request = FakeRequest(POST, "/rmProduct").withFormUrlEncodedBody(
        "id" -> "0"
      )
      val result = ProductController.rmProduct()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/productDeleted?deleted=true")
    }

    "remove a Product bad request" in memDB {
      val request = FakeRequest(POST, "/rmProduct").withFormUrlEncodedBody(
        "ID" -> "0"
      )
      val result = ProductController.rmProduct()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "get a Product" in memDB {
      val request = FakeRequest(POST, "/selectProduct").withFormUrlEncodedBody(
        "id" -> "0"
      )
      val result = ProductController.getProduct()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/changeProduct?id=0&name=&price=0.0&size=0.0&unit=")
    }

    "get a Product bad request" in memDB {
      val request = FakeRequest(POST, "/selectProduct").withFormUrlEncodedBody(
        "Id" -> "0"
      )
      val result = ProductController.getProduct()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "show new Product" in memDB {
     val request = FakeRequest(POST, "/addProduct").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "3",
        "Größe" -> "500",
        "Einheit" -> "g"
      )
      val result = ProductController.newProductCreated(0,"Kas",3,500,"g")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("0") contain "Kas" contain "500"
    }

    "show view to change Pizza" in memDB {
      val request = FakeRequest(POST, "/addProduct").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "3",
        "Größe" -> "500",
        "Einheit" -> "g"
      )
      val result = ProductController.changeProduct1(0,"Speck",1,100,"g")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("0") contain "Speck" contain "g"
    }

    "show changed Product" in memDB {
      val request = FakeRequest(POST, "/addProduct").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "3",
        "Größe" -> "500",
        "Einheit" -> "g"
      )
      val result = ProductController.upgradeProduct(0,"Speck",1,100,"g")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("0") contain "Speck" contain "100"
    }

    "show view for deleted Product" in memDB {
      val request = FakeRequest(POST, "/addProduct").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "3",
        "Größe" -> "500",
        "Einheit" -> "g"
      )
      val result = ProductController.productDeleted(true)(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Produkt Gelöscht!")
    }

    "showProducts" in memDB {
      val request = FakeRequest(POST, "/addProduct").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "3",
        "Größe" -> "500",
        "Einheit" -> "g"
      )
      val result = ProductController.showProducts()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Produktübersicht")
    }
  }
}


