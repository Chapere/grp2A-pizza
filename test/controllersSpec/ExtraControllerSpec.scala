package controllersSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

import play.api.test.Helpers._
import play.api.test._
import controllers.ExtraController

/**
  * Created by ifw15124 on 20.04.2017.
  */
@RunWith(classOf[JUnitRunner])
class ExtraControllerSpec extends Specification{

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

    "update an Extra" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.addExtra()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/newExtraCreated?id=-1&name=Kas&price=1.0")
      val request2 = FakeRequest(POST, "/updateExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Speck",
        "Preis" -> "1"
      )
      val result2 = ExtraController.updateExtra()(request2)
      status(result2) must equalTo(SEE_OTHER)
      redirectLocation(result2) must beSome("/upgradeExtra?id=0&name=Speck&price=1.0")
    }

    "remove an Extra" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.addExtra()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/newExtraCreated?id=-1&name=Kas&price=1.0")
      val request2 = FakeRequest(POST, "/rmExtra").withFormUrlEncodedBody(
        "id" -> "0"
      )
      val result2 = ExtraController.rmExtra()(request2)
      status(result2) must equalTo(SEE_OTHER)
      redirectLocation(result2) must beSome("/extraDeleted?deleted=true")
    }

    "get an Extra" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "id" -> "0",
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.addExtra()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/newExtraCreated?id=-1&name=Kas&price=1.0")
      val request2 = FakeRequest(POST, "/selectExtra").withFormUrlEncodedBody(
        "id" -> "0"
      )
      val result2 = ExtraController.getExtra()(request2)
      status(result2) must equalTo(SEE_OTHER)
      redirectLocation(result2) must beSome("/changeExtra?id=0&name=&price=0.0")
    }
  }
}
