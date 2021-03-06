package controllersSpec

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers.{OK, POST, SEE_OTHER, redirectLocation, BAD_REQUEST, contentAsString, running, status}
import play.api.test.{FakeApplication, FakeRequest}
import controllers.ExtraController
import scala.concurrent.duration._
import akka.util.Timeout

/**
  * @author Felix Thomas
  */
@RunWith(classOf[JUnitRunner])
class ExtraControllerSpec extends Specification {
  implicit val duration: Timeout = 20 seconds

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "ExtraController" should {

    "add an Extra" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.addExtra()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/newExtraCreated?id=-1&name=Kas&price=1.0")
    }

    "add an Extra bad request" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.addExtra()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "update an Extra" in memDB {
      val request = FakeRequest(POST, "/updateExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Speck",
        "Preis" -> "1"
      )
      val result = ExtraController.updateExtra()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/upgradeExtra?id=0&name=Speck&price=1.0")
    }

    "update an Extra bad request" in memDB {
      val request = FakeRequest(POST, "/updateExtra").withFormUrlEncodedBody(
        "Name" -> "Speck",
        "Preis" -> "1"
      )
      val result = ExtraController.updateExtra()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "remove an Extra" in memDB {
      val request = FakeRequest(POST, "/rmExtra").withFormUrlEncodedBody(
        "id" -> "0"
      )
      val result = ExtraController.rmExtra()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/extraDeleted?deleted=true")
    }

    "remove an Extra bad request" in memDB {
      val request = FakeRequest(POST, "/rmExtra").withFormUrlEncodedBody(
        "ID" -> "0"
      )
      val result = ExtraController.rmExtra()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "get an Extra" in memDB {
      val request = FakeRequest(POST, "/selectExtra").withFormUrlEncodedBody(
        "id" -> "0"
      )
      val result = ExtraController.getExtra()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/changeExtra?id=0&name=&price=0.0")
    }

    "get an Extra bad request" in memDB {
      val request = FakeRequest(POST, "/selectExtra").withFormUrlEncodedBody(
        "Id" -> "0"
      )
      val result = ExtraController.getExtra()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "show new Extra" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.newExtraCreated(0, "Kas", 1)(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("0") contain "Kas" contain "1"
    }

    "show view to change Extra" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.changeExtra1(0, "Speck", 2)(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("0") contain "Speck" contain "2"
    }

    "show changed Extra" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.upgradeExtra(0, "Paprika", 2)(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("0") contain "Paprika" contain "2"
    }

    "show view for deleted Extra" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.extraDeleted(true)(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Extra Gelöscht!")
    }

    "showExtras" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.showExtras()(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Extrasübersicht")
    }
  }
}
