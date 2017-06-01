package servicesSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import services.{EmployeeService}

@RunWith(classOf[JUnitRunner])
class EmployeeServiceSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The EmployeeService" should {

    "employee Padrone have to be registered at the start" in memDB {
      val registeredEmployees = EmployeeService.registredEmployees
      registeredEmployees.length must be equalTo (1)
      registeredEmployees(0).name must be equalTo ("Padrone")
    }

    "employee Padrone should have ID 1" in memDB {
      val registeredEmployee = EmployeeService.registredEmployees
      registeredEmployee(0).id must be equalTo (1)
    }


    "return a list containing just Padrone & Andi after adding employee Andi" in memDB {
      EmployeeService.addEmployee("Andi", "Frey", "IT", "root", 10, 15.84, "root", "root");
      val registeredEmployee = EmployeeService.registredEmployees
      registeredEmployee.length must be equalTo (2)
      registeredEmployee(0).name must be equalTo ("Padrone")
      registeredEmployee(1).name must be equalTo ("Andi")
    }

    "set employee Andi inactive" in memDB {
      EmployeeService.addEmployee("Andi", "Frey", "IT", "root", 10, 15.84, "root", "root").activeFlag must
        beEqualTo(1)
      EmployeeService.setEmployeeFlag0(2)
      val registeredEmployee = EmployeeService.registredEmployees
      registeredEmployee(1).activeFlag must beEqualTo(0)
    }
    "set user Padrone inactive and activ again" in memDB {
      EmployeeService.setEmployeeFlag0(1)
      val registeredEmployee = EmployeeService.registredEmployees
      registeredEmployee(0).activeFlag must beEqualTo(0)
      EmployeeService.setEmployeeFlag1(1)
      val registeredEmployee2 = EmployeeService.registredEmployees
      registeredEmployee2(0).activeFlag must beEqualTo(1)
    }

    "update information from employe Padrone" in memDB{
      EmployeeService.updateEmployee(1, "Padrone", "Müller", "IT", "root", 10, 15.84, "root", "root").lastname must
      beEqualTo("Müller")
    }

    "add a employee Andi" in memDB {
      EmployeeService.addEmployee("Andi", "Frey", "IT", "root", 10, 15.84, "root", "root").name must
      beEqualTo("Andi")
    }

    "remove employee Padrone" in memDB {
      EmployeeService.addEmployee("Andi", "Frey", "IT", "root", 10, 15.84, "root", "root").name must
        beEqualTo("Andi")
      val registeredEmployee = EmployeeService.registredEmployees
      registeredEmployee.length must beEqualTo(2)
      EmployeeService.rmEmployee(2)
      val registeredEmployee2 = EmployeeService.registredEmployees
      registeredEmployee2.length must beEqualTo(1)
    }

    "search Employee by ID" in memDB {
      EmployeeService.getEmployeeByID(1).name must beEqualTo("Padrone")
    }

    "login employee to website" in memDB {
      EmployeeService.logInEmployee("root", "root").name must beEqualTo("Padrone")
    }
  }
}
