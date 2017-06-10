package controllersSpec

import controllers.{EmployeeController}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.{FakeApplication, FakeRequest}
import play.api.test.Helpers.{POST, SEE_OTHER, BAD_REQUEST, OK, redirectLocation, running, status, contentAsString}
import scala.concurrent.duration._
import akka.util.Timeout
/**
  * @author Andreas Frey
  */
@RunWith(classOf[JUnitRunner])
class EmployeeControllerSpec extends Specification {
  implicit val duration: Timeout = 20 seconds

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The EmployeeController" should {

    "add a employee" in memDB {
      val request = FakeRequest(POST, "/addEmployee").withFormUrlEncodedBody(
        "Name" -> "Andreas",
        "Vorname" -> "Frey",
        "Gebiet" -> "IT",
        "Zugriff" -> "root",
        "Zugriffsebene" -> "10",
        "Stundenrate" -> "10.5",
        "E-Mail" -> "andi",
        "Passwort" -> "andi"
      )
      val result = EmployeeController.addEmployee()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/newEmployeeCreated?name=Andreas&lastname=Frey&workplace=IT&acces=root&accesLevel=10&netRate=10.5&email=andi&password=andi")
    }

    "add a employee with fail" in memDB {
      val request = FakeRequest(POST, "/addEmployee").withFormUrlEncodedBody(
        "Name" -> "Andreas",
        "Vorname" -> "Frey",
        "Gebiet" -> "IT",
        "Zugriff" -> "root",
        "Zugriffsebene" -> "10",
        "Stundenrate" -> "10.5",
        "E-Mail" -> "andi"
      )
      val result = EmployeeController.addEmployee()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "update a employee" in memDB {
      val request = FakeRequest(POST, "/updateEmployee").withFormUrlEncodedBody(
        "id" -> "1",
        "Name" -> "Padrone",
        "Vorname" -> "Hubert",
        "Gebiet" -> "IT",
        "Zugriff" -> "root",
        "Zugriffsebene" -> "10",
        "Stundenrate" -> "10",
        "E-Mail" -> "root",
        "Passwort" -> "root"
      )
      val result = EmployeeController.updateEmployee()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/upgradeEmployee?id=1&name=Padrone&lastname=Hubert&workplace=IT&acces=root&accesLevel=10&netRate=10.0&email=root&password=root")
    }

    "update a employee with fail" in memDB {
      val request = FakeRequest(POST, "/updateEmployee").withFormUrlEncodedBody(
        "id" -> "1",
        "Name" -> "Padrone",
        "Vorname" -> "Hubert",
        "Gebiet" -> "IT",
        "Zugriff" -> "root",
        "Zugriffsebene" -> "10",
        "Stundenrate" -> "10",
        "E-Mail" -> "root"
      )
      val result = EmployeeController.updateEmployee()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "choose a specific employee" in memDB {
      val request = FakeRequest(POST, "/chooseEmployee").withFormUrlEncodedBody(
        "Mitarbeiter ID" -> "1"
      )
      val result = EmployeeController.chooseEmployee()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/changeEmployee?id=1&name=Padrone&lastname=Hubert&workplace=IT&acces=root&accesLevel=10&netRate=15.84&email=root&password=&activeFlag=1")
    }

    "employee LogIn" in memDB {
      val request = FakeRequest(POST, "/logInEmployee").withFormUrlEncodedBody(
        "E-Mail" -> "root",
        "Passwort" -> "root"
      )
      val result = EmployeeController.logInEmployee()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/employeeLoggedIn?id=1&name=Padrone")
    }

    "employee LogIn with fail" in memDB {
      val request = FakeRequest(POST, "/logInEmployee").withFormUrlEncodedBody(
        "E-Mail" -> "root"
      )
      val result = EmployeeController.logInEmployee()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "flag of inaktiv employee " in memDB {
      val request = FakeRequest(POST, "/employeeFalgZero").withFormUrlEncodedBody(
        "Mitarbeiter ID" -> "1"
      )
      val result = EmployeeController.employeeFlagZero()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/employeeFlagSet?id=0.0")
    }

    "flag of inaktiv employee with fail" in memDB {
      val request = FakeRequest(POST, "/employeeFalgZero").withFormUrlEncodedBody()
      val result = EmployeeController.employeeFlagZero()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "flag of aktiv employee " in memDB {
      val request = FakeRequest(POST, "/employeeFalgOne").withFormUrlEncodedBody(
        "Mitarbeiter ID" -> "1"
      )
      val result = EmployeeController.employeeFlagOne()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/employeeFlagSet?id=1.0")
    }

    "flag of aktiv employee with fail" in memDB {
      val request = FakeRequest(POST, "/employeeFalgOne").withFormUrlEncodedBody()
      val result = EmployeeController.employeeFlagOne()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "remove employee from the database" in memDB {
      val request = FakeRequest(POST, "/rmEmployee").withFormUrlEncodedBody(
        "Mitarbeiter ID" -> "1"
      )
      val result = EmployeeController.rmEmployee()(request)
      status(result) must beEqualTo(SEE_OTHER)
      redirectLocation(result) must beSome("/employeeDeleted?deleted=true")
    }

    "remove employee from the database with fail" in memDB {
      val request = FakeRequest(POST, "/rmEmployee").withFormUrlEncodedBody(
      )
      val result = EmployeeController.rmEmployee()(request)
      status(result) must equalTo(BAD_REQUEST)
      redirectLocation(result) must beNone
    }

    "register an employee" in memDB {
      val request = FakeRequest(POST, "/addEmployee").withFormUrlEncodedBody(
        "Name" -> "Andreas",
        "Vorname" -> "Frey",
        "Gebiet" -> "IT",
        "Zugriff" -> "root",
        "Zugriffsebene" -> "10",
        "Stundenrate" -> "10.5",
        "E-Mail" -> "andi"
      )
      val result = EmployeeController.registerEmployee()(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("/registerEmployee")
    }

    "login an employee" in memDB {
      val request = FakeRequest(POST, "/employeeLoggedIn").withFormUrlEncodedBody(
        "E-Mail" -> "root",
        "Passwort" -> "root"
      )
      val result = EmployeeController.completeLogInEmployee(1, "Padrone")(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("Home")
    }

    "new employee created" in memDB {
      val request = FakeRequest(POST, "/addEmployee").withFormUrlEncodedBody(
        "Name" -> "Andreas",
        "Vorname" -> "Frey",
        "Gebiet" -> "IT",
        "Zugriff" -> "root",
        "Zugriffsebene" -> "10",
        "Stundenrate" -> "10.5",
        "E-Mail" -> "andi",
        "Passwort" -> "andi"
      )
      val result = EmployeeController.newEmployeeCreated("Andreas", "Frey", "IT", "root", 10, 10.5, "andi", "andi")(request)
      status(result) must equalTo(OK)
      contentAsString(result) must contain ("Andreas") contain "Frey"
    }

    "edit an employee" in memDB {
      val request = FakeRequest(POST, "/updateEmployee").withFormUrlEncodedBody(
        "id" -> "1",
        "Name" -> "Padrone",
        "Vorname" -> "Hubert",
        "Gebiet" -> "IT",
        "Zugriff" -> "root",
        "Zugriffsebene" -> "10",
        "Stundenrate" -> "10",
        "E-Mail" -> "root",
        "Passwort" -> "root"
      )
      val result = EmployeeController.changeEmployee(1, "Padrone", "Hubert", "IT", "root", 10, 10, "root", "root", 1) (request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain ("Padrone") contain "Hubert"
    }

    "update an employee" in memDB {
      val request = FakeRequest(POST, "/updateEmployee").withFormUrlEncodedBody(
        "id" -> "1",
        "Name" -> "Padrone",
        "Vorname" -> "Hubert",
        "Gebiet" -> "IT",
        "Zugriff" -> "root",
        "Zugriffsebene" -> "10",
        "Stundenrate" -> "10",
        "E-Mail" -> "root",
        "Passwort" -> "root"
      )
      val result = EmployeeController.upgradeEmployee(1, "Padrone", "Hubert", "IT", "root", 10, 10, "root", "root") (request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain ("Mitarbeiter Aktualisiert")
    }

    "delete an employee" in memDB {
      val request = FakeRequest(POST, "/rmEmployee").withFormUrlEncodedBody(
        "Mitarbeiter ID" -> "1"
      )
      val result = EmployeeController.employeeDeleted(true)(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("Mitarbeiter Gelöscht!")
    }

    "set employee flag" in memDB {
      val request = FakeRequest(POST, "/changeEmployeeFlag0").withFormUrlEncodedBody(
        "Mitarbeiter ID" -> "1"
      )
      val result = EmployeeController.setEmployeeFlag(1)(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("Der Mitarbeiter wurde reaktiviert!")
    }

    "show all employee" in memDB {
      val request = FakeRequest(POST, "/showEmployee")
      val result = EmployeeController.showEmployees()(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("Mitarbeiterübersicht")
    }

    "show order detail" in memDB {
      val request = FakeRequest(POST, "/showAllOrderDetails")
      val result = EmployeeController.showAllOrderDetails()(request)
      status(result) must beEqualTo(OK)
      contentAsString(result) must contain("Bestellübersicht")
    }
  }
}
