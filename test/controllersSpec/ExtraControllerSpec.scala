package controllersSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.mock.Mockito

import play.api.test.Helpers._
import play.api.test._
import controllers.ExtraController

import controllers.UserController
import dbaccess.UserDao
import models.User

/**
  * Created by ifw15124 on 20.04.2017.
  */
@RunWith(classOf[JUnitRunner])
class ExtraControllerSpec extends Specification{

  /*def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "ExtraController" should {

    "add an Extra" in memDB {
      val request = FakeRequest(POST, "/addExtra").withFormUrlEncodedBody(
        "Name" -> "Kas",
        "Preis" -> "1"
      )
      val result = ExtraController.addExtra()(request)
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome("/newExtraCreated")
    }
  }

  object ExtraController {
    val extraService = mock[services.ExtraServiceT]
    extraService.addExtra("Käse",1)
  }*/
}
