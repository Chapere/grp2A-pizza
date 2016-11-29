package services

import dbaccess.{EmployeeDao, EmployeeDaoT}
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
  def addEmployee(name: String, lastname: String, workplace: String, acces: String, netRate: String, email: String, password: String): Employee = {
    // create User
    val newEmployee = Employee(-1, name, lastname, workplace, acces, netRate, email, password)
    // persist and return User
    EmployeeDao.addEmployee(newEmployee)
  }

  def logInEmployee(email: String, password: String): String = {
    // create User
    val newEmployee = Employee(-1, null, null, null, null, null, email, password)
    // persist and return User
    return employeeDao.logInEmployee(newEmployee)
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
