package services

import dbaccess._
import models.Employee

/**
 * Service class for user related operations.
 *
 * @author ob, scs
 */
trait EmployeeServiceT {

  val employeeDao: EmployeeDaoT = EmployeeDao

  /**
   * Adds a new user to the system.
   * @param name name of the new user.
   * @return the new user.
   */
  def addEmployee(name: String, lastname: String, workplace: String, acces: String, accesLevel: Int, netRate: Double, email: String, password: String): Employee = {
    // create User
    val newEmployee = Employee(-1, name, lastname, workplace, acces, accesLevel, netRate, email, password, 1)
    // persist and return User
    EmployeeDao.addEmployee(newEmployee)
  }

  def chooseEmployee(id: Long): Employee = {
    // create User
    val showEmployee = Employee(id, null, null, null, null, -1, -1, null, null, -1)
    // persist and return User
    EmployeeDao.displayEmployee(showEmployee)
  }

  def updateEmployee(name: String, lastname: String, workplace: String, acces: String, accesLevel: Int, netRate: Double, email: String, password: String): Employee = {
    // create User
    val updateEmployeeService = Employee(-1, name, lastname, workplace, acces, accesLevel, netRate, email, password, -1)
    // persist and return User
    EmployeeDao.updateEmployeeDao(updateEmployeeService)
  }

  def logInEmployee(email: String, password: String): Employee = {
    // create User
    val logInEmployee = Employee(-1, null, null, null, null, -1, -1, email, password, -1)
    // persist and return User
    return employeeDao.logInEmployee(logInEmployee)
  }

  /**
   * Removes a user by id from the system.
   * @param id users id.
   * @return a boolean success flag.
   */
  def rmEmployee(id: Long): Boolean = EmployeeDao.rmEmployee(id)

  /**
   * Gets a list of all registered users.
   * @return list of users.
   */
  def registredEmployees: List[Employee] = {
    EmployeeDao.availableEmployees
  }

}

object EmployeeService extends EmployeeServiceT
