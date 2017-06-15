package services

import dbaccess.{EmployeeDao, EmployeeDaoT}
import models.Employee

/**
  * Service class for user related operations.
  *
  * @author ob, scs
  */
trait EmployeeServiceT {
  val error = "error"
  val employeeDao: EmployeeDaoT = EmployeeDao

  /**
    * Adds a new user to the system.
    *
    * @param name name of the new user.
    * @return the new user.
    */
  def addEmployee(name: String, lastname: String,
                  workplace: String, acces: String,
                  accesLevel: Int, netRate: Double,
                  email: String, password: String): Employee = {
    // create User
    val newEmployee = Employee(-1, name, lastname, workplace,
      acces, accesLevel, netRate, email, password, 1)
    // persist and return User
    EmployeeDao.addEmployee(newEmployee)
  }

  def getEmployeeByID(id: Long): Employee = {
    // persist and return User
    EmployeeDao.getEmployee(id)
  }

  def updateEmployee(id: Long, name: String, lastname: String,
                     workplace: String, acces: String, accesLevel: Int,
                     netRate: Double, email: String,
                     password: String): Employee = {
    // create User
    val updateEmployeeService = Employee(id, name, lastname, workplace,
      acces, accesLevel, netRate, email, password, -1)
    // persist and return User
    EmployeeDao.updateEmployeeDao(updateEmployeeService)
  }

  def logInEmployee(email: String, password: String): Employee = {
    // create User
    val logInEmployee = Employee(-1, error, error, error, error, -1, -1, email, password, -1)
    // persist and return User
    employeeDao.logInEmployee(logInEmployee)
  }

  def setEmployeeFlag0(id: Long): Long = {
    // create User
    // persist and return User
    EmployeeDao.deactivateEmployee(id)
  }

  def setEmployeeFlag1(id: Long): Long = {
    // create User
    // persist and return User
    EmployeeDao.activateEmployee(id)
  }


  /**
    * Removes a user by id from the system.
    *
    * @param id users id.
    * @return a boolean success flag.
    */
  def rmEmployee(id: Long): Boolean = EmployeeDao.rmEmployee(id)

  /**
    * Gets a list of all registered users.
    *
    * @return list of users.
    */
  def registredEmployees: List[Employee] = {
    EmployeeDao.availableEmployees
  }

}

object EmployeeService extends EmployeeServiceT
