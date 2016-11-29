package dbaccess

import anorm.SQL
import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.Employee

/**
 * Data access object for user related operations.
 *
 * @author ob, scs
 */
trait EmployeeDaoT {

  /**
   * Creates the given user in the database.
   * @param employee the user object to be stored.
   * @return the persisted user object
   */
  def addEmployee(employee: Employee): Employee = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Employees(name, lastname, workplace, acces, netRate) values ({name}, {lastname}, {workplace}, {acces}, {netRate})").on(
          'name -> employee.name, 'lastname -> employee.lastname, 'workplace -> employee.workplace, 'acces -> employee.acces, 'netRate -> employee.netRate).executeInsert()
      employee.id = id.get
    }
    employee
  }

  /**
   * Removes a user by id from the database.
   * @param id the users id
   * @return a boolean success flag
   */
  def rmEmployee(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Employees where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
   * Returns a list of available user from the database.
   * @return a list of user objects.
   */
  def availableEmployees: List[Employee] = {
    DB.withConnection { implicit c =>
      val selectEmployees = SQL("Select id, name, lastname, workplace, acces, netRate from Employees;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val employees = selectEmployees().map(row => Employee(row[Long]("id"), row[String]("name"), row[String]("lastname"),
        row[String]("workplace"), row[String]("acces"), row[String]("netRate"))).toList
      employees
    }
  }
}

object EmployeeDao extends EmployeeDaoT
