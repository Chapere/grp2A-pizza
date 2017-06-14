package controllersSpec

import controllers.UserController
import dbaccess.UserDao
import models.User

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

import play.api.test.Helpers._
import play.api.test._
import services.UserService

/**
  * Created by Taha Obed on 20.04.2017.
  * @author Taha Obed
  */
@RunWith(classOf[JUnitRunner])
class UserControllerSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The UserController" should {

    "add a user" in memDB {
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Name" -> "Obed",
        "Vorname" -> "Taha",
        "Adresse" -> "Flurstr 14",
        "Stadt" -> "München",
        "PLZ" -> "81425",
        "E-Mail" -> "taha@pronto",
        "Passwort" -> "2131"
      )
      val result = UserController.addUser()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/newUserCreated?id=2&username=Obed&lastname=Taha&adress=Flurstr+14&city=M%C3%BCnchen&plz=81425&distance=0.0&email=taha%40pronto&password=2131")
    }

    "add a user BadRequest" in memDB {
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Vorname" -> "Taha",
        "Adresse" -> "Flurstr 14",
        "Stadt" -> "München",
        "PLZ" -> "81425",
        "E-Mail" -> "taha@pronto",
        "Passwort" -> "2131"
      )
      val result = UserController.addUser()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone

    }

    "update a user" in memDB {
      val request = FakeRequest(POST, "/updateUser").withFormUrlEncodedBody(
        "userID" -> "1",
        "Name" -> "Emil",
        "Vorname" -> "Schulz",
        "Adresse" -> "iesestr 2",
        "Stadt" -> "München",
        "PLZ" -> "84125",
        "E-Mail" -> "emil@pronto",
        "Passwort" -> "1234"
      )
      val result = UserController.updateUser()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/upgradeUser?id=1&name=Emil&lastname=Schulz&adress=iesestr+2&city=M%C3%BCnchen&plz=84125&distance=0.0&email=emil%40pronto&password=1234")
    }

    "update a user BadRequest" in memDB {
      val request = FakeRequest(POST, "/updateUser").withFormUrlEncodedBody(
        "Vorname" -> "Schulz",
        "Adresse" -> "iesestr 2",
        "Stadt" -> "München",
        "PLZ" -> "84125",
        "E-Mail" -> "emil@pronto",
        "Passwort" -> "1234"
      )
      val result = UserController.updateUser()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "change a flag of an user to zero" in memDB {
      val request = FakeRequest(POST, "/changeUserFlag0").withFormUrlEncodedBody(
        "User ID" -> "1"
      )
      val result = UserController.userFlagZero()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/userFlagSet?id=0.0")
    }

    "change a flag of an user to zero BadRequest" in memDB {
      val request = FakeRequest(POST, "/changeUserFlag0").withFormUrlEncodedBody(
      )
      val result = UserController.userFlagZero()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "change a flag of an user to one" in memDB {
      val request = FakeRequest(POST, "/changeUserFlag1").withFormUrlEncodedBody(
        "User ID" -> "1"
      )
      val result = UserController.userFlagOne()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/userFlagSet?id=1.0")
    }

    "change a flag of an user to one BadRequest" in memDB {
      val request = FakeRequest(POST, "/changeUserFlag1").withFormUrlEncodedBody(
      )
      val result = UserController.userFlagOne()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "choose a specific user from the database" in memDB {
      val request = FakeRequest(POST, "/chooseUser").withFormUrlEncodedBody(
        "User ID" -> "1"
      )
      val result = UserController.chooseUser()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/changeUser?id=1&name=Emil&lastname=Schulz&adress=Lerchenauer+Str.+12&city=M%C3%BCnchen&plz=80935&distance=19000.0&email=user&password=&activeFlag=1")
    }

    "choose a specific user from the database BadRequest" in memDB {
      val request = FakeRequest(POST, "/chooseUser").withFormUrlEncodedBody(
      )
      val result = UserController.chooseUser()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "remove user from the database" in memDB {
      val request = FakeRequest(POST, "/rmUser").withFormUrlEncodedBody(
        "User ID" -> "1"
      )
      val result = UserController.rmUser()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/userDeleted?deleted=true")
    }

    "remove user from the database BadRequest" in memDB {
      val request = FakeRequest(POST, "/rmUser").withFormUrlEncodedBody(
      )
      val result = UserController.rmUser()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    /*"user makes an distance error" in memDB {
      val request = FakeRequest(POST, "addUser").withFormUrlEncodedBody(
        "User ID" -> "1"
      )
      val result = UserController.distanceError()(request)
      status(result) must beEqualTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }*/

    "login an user" in memDB {
      val request = FakeRequest(POST, "/userLoggedIn").withFormUrlEncodedBody(
        "E-Mail" -> "user",
        "Passwort" -> "user"
      )
      val result = UserController.logInUser()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/userLoggedIn?id=1&name=Emil")
    }

    "login an user BadRequest" in memDB {
      val request = FakeRequest(POST, "/userLoggedIn").withFormUrlEncodedBody(
        "Passwort" -> "user"
      )
      val result = UserController.logInUser()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "register an user" in memDB {
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Name" -> "Obed",
        "Vorname" -> "Taha",
        "Adresse" -> "Flurstr 14",
        "Stadt" -> "München",
        "PLZ" -> "81425",
        "E-Mail" -> "taha@pronto",
        "Passwort" -> "2131"
      )
      val result = UserController.registerUser()(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("Home")

    }

    "complete the login of an user" in memDB {
      val request = FakeRequest(POST, "/userLoggedIn").withFormUrlEncodedBody(
        "E-Mail" -> "user",
        "Passwort" -> "user"
      )
      val result = UserController.completeLogInUser(1, "Emil")(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("Home")

    }

    "create a new user in the database" in memDB {
      val request = FakeRequest(POST, "/addUser").withFormUrlEncodedBody(
        "Name" -> "Obed",
        "Vorname" -> "Taha",
        "Adresse" -> "Flurstr 14",
        "Stadt" -> "München",
        "PLZ" -> "81425",
        "E-Mail" -> "taha@pronto",
        "Passwort" -> "2131"
      )
      val result = UserController.newUserCreated(2, "Obed", "Taha", "Flurstr 14", "München", "81425", 0, "taha@pronto", "2131")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Obed") contain "Taha"
    }

    "change the user" in memDB {
      val request = FakeRequest(POST, "/updateUser").withFormUrlEncodedBody(
        "userID" -> "1",
        "Name" -> "Emil",
        "Vorname" -> "Schulz",
        "Adresse" -> "iesestr 2",
        "Stadt" -> "München",
        "PLZ" -> "84125",
        "E-Mail" -> "emil@pronto",
        "Passwort" -> "1234"
      )
      val result = UserController.changeUser1(1, "Emil", "Schulz", "iesestr 2", "München", "81425", 0, "emil@pronto", "1234", 1) (request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain ("iesestr 2") contain "emil@pronto"

    }

    "upgrade the user" in memDB {
      val request = FakeRequest(POST, "/updateUser").withFormUrlEncodedBody(
        "userID" -> "1",
        "Name" -> "Emil",
        "Vorname" -> "Schulz",
        "Adresse" -> "iesestr 2",
        "Stadt" -> "München",
        "PLZ" -> "84125",
        "E-Mail" -> "emil@pronto",
        "Passwort" -> "1234"
      )
      val result = UserController.upgradeUser(1, "Emil", "Schulz", "iesestr 2", "München", "81425", 0, "emil@pronto", "1234") (request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain ("iesestr 2") contain "emil@pronto"

    }

    "delete the user from the database" in memDB {
      val request = FakeRequest(POST, "/rmUser").withFormUrlEncodedBody(
        "User ID" -> "1"
      )
      val result = UserController.userDeleted(true)(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain ("Benutzer Gelöscht!")
    }

    "set the flag for an user" in memDB {
      val request = FakeRequest(POST, "/changeUserFlag0").withFormUrlEncodedBody(
        "User ID" -> "1"
      )
      val result = UserController.setUserFlag(1)(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("Zustand des Benutzers wurde geändert.")
    }

    "show all user in the database" in memDB {
      val request = FakeRequest(POST, "/showUsers")
      val result = UserController.showUsers()(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("Emil")

    }
  }
}