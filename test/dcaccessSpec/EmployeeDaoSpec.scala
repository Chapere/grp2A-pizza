package dcaccessSpec

import dbaccess.EmployeeDao
import models.Employee
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import play.api.test.Helpers.running
import play.api.test.FakeApplication


/**
  * Created by ifw15124 on 20.04.2017.
  */
@RunWith(classOf[JUnitRunner])
class EmployeeDaoSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The EmployeeDao" should {

    "add a new employee" in memDB {
      val add = EmployeeDao.addEmployee(Employee(-1, "Andi", "Frey", "IT", "root", 10, 13.37, "andi", "andi", 1))
      add.name must beEqualTo("Andi")
      add.lastname must beEqualTo("Frey")
      add.workplace must beEqualTo("IT")
      add.acces must beEqualTo("root")
      add.accesLevel must beEqualTo(10)
      add.netRate must beEqualTo(13.37)
      add.email must beEqualTo("andi")
      add.password must beEqualTo("andi")
      add.activeFlag must beEqualTo(1)
    }

    "update Employee" in memDB {
      val update = EmployeeDao.updateEmployeeDao(Employee(1, "change", "change", "change", "change", 1337, 13.37, "change", "change", 1))
      update must beEqualTo(Employee(1, "change", "change", "change", "change", 1337, 13.37, "change", "change", 1))
    }

    "get Employee by ID" in memDB {
      val id = EmployeeDao.getEmployee(1)
      id must beEqualTo(Employee(1, "Padrone", "Hubert", "IT", "root", 10, 15.84, "root", null, 1))
    }

    "login Employee into System" in memDB {
      val id = EmployeeDao.logInEmployee(Employee(1, "Padrone", "Hubert", "IT", "root", 10, 15.84, "root", "root", 1))
      id.name must beEqualTo("Padrone")
    }

    "deactivate Employee by ID" in memDB {
      EmployeeDao.deactivateEmployee(1)
      val getEmployee = EmployeeDao.getEmployee(1)
      getEmployee.activeFlag must beEqualTo(0)
    }

    "deactivate Employee and activate Employee by ID" in memDB {
      EmployeeDao.deactivateEmployee(1)
      val getEmployee = EmployeeDao.getEmployee(1)
      getEmployee.activeFlag must beEqualTo(0)
      EmployeeDao.activateEmployee(1)
      val getEmployee1 = EmployeeDao.getEmployee(1)
      getEmployee1.activeFlag must beEqualTo(1)
    }

    "remove Employee" in memDB {
      val remove = EmployeeDao.rmEmployee(1)
      remove must beTrue
    }

    "list of all Employee" in memDB {
      val available = EmployeeDao.availableEmployees.length
      available must beEqualTo(1)
    }
  }
}