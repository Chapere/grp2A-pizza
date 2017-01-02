package dbaccess

import anorm.{SQL, SqlParser}
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
        SQL("insert into Employees(name, lastname, workplace, acces, accesLevel, netRate, email, password, activeFlag) values ({name}, {lastname}, {workplace}, {acces}, {accesLevel}, {netRate}, {email}, {password}, 1)").on(
          'name -> employee.name, 'lastname -> employee.lastname, 'workplace -> employee.workplace, 'acces -> employee.acces, 'accesLevel -> employee.accesLevel, 'netRate -> employee.netRate, 'email -> employee.email, 'password -> employee.password).executeInsert()
      employee.id = id.get
    }
    employee
  }

  def updateEmployeeDao(employee: Employee): Employee = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("UPDATE Employees SET name = {name}, lastname = {lastname}, workplace = {workplace}, acces = {acces}, netRate = {netRate}, email = {email}, password = {password} WHERE id = {id}").on(
          'name -> employee.name, 'lastname -> employee.lastname, 'workplace -> employee.workplace, 'acces -> employee.acces, 'accesLevel -> employee.accesLevel, 'netRate -> employee.netRate, 'email -> employee.email, 'password -> employee.password, 'id -> models.Debug.updateUserId).executeInsert()
    }
    employee
  }

  def displayEmployee(employee: Employee): Employee = {

    DB.withConnection { implicit c =>
      val lastname: String =
        SQL("Select lastname from Employees where id = {id};").on(
          'id -> employee.id).
          as(SqlParser.str("lastname").single)

      val workplace: String =
        SQL("Select workplace from Employees where id = {id};").on(
          'id -> employee.id).
          as(SqlParser.str("workplace").single)

      val acces: String =
        SQL("Select acces from Employees where id = {id};").on(
          'id -> employee.id).
          as(SqlParser.str("acces").single)

      val netRate: Double =
        SQL("Select netRate from Employees where id = {id};").on(
          'id -> employee.id).
          as(SqlParser.double("netRate").single)

      val email: String =
        SQL("Select email from Employees where id = {id};").on(
          'id -> employee.id).
          as(SqlParser.str("email").single)


      val accesLevel: Int =
        SQL("Select accesLevel from Employees where id = {id};").on(
          'id -> employee.id).
          as(SqlParser.int("accesLevel").single)

      val name: String =
        SQL("Select name from Employees where id = {id};").on(
          'id -> employee.id).
          as(SqlParser.str("name").single)

      val id: Long =
        SQL("Select id from Employees where id = {id};").on(
          'id -> employee.id).
          as(SqlParser.long("id").single)

      val activeFlag: Int =
        SQL("Select activeFlag from Employees where id = {id};").on(
          'id -> employee.id).
          as(SqlParser.int("activeFlag").single)

      models.Debug.updateUserId = id

      val chooseEmployee = Employee(employee.id, name, lastname, workplace, acces, accesLevel, netRate, email, null, activeFlag)

      chooseEmployee

    }

  }


  def logInEmployee(employee: Employee): Employee = {
    DB.withConnection { implicit c =>
      val selectEmployees = SQL("SELECT * FROM EMPLOYEES WHERE email = {email} AND password = {password};").on(
        'email -> employee.email, 'password -> employee.password)
      val employees = selectEmployees().map(row => Employee(row[Long]("id"), row[String]("name"), row[String]("lastname"),
        row[String]("workplace"), row[String]("acces"), row[Int]("accesLevel"), row[Double]("netRate"), row[String]("email"), null, row[Int]("activeFlag"))).toList
      models.activeUser.lastname = employees.head.lastname

      models.activeUser.workplace = employees.head.workplace
      models.activeUser.acces = employees.head.acces
      models.activeUser.netRate = employees.head.netRate
      models.activeUser.email = employees.head.email
      models.activeUser.id = employees.head.id
      models.activeUser.accesLevel = employees.head.accesLevel
      models.activeUser.name = employees.head.name
      models.activeUser.activeFlag = employees.head.activeFlag
      models.activeUser.typ = "Employee"

      return employees.head
    }
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
      val selectEmployees = SQL("Select id, name, lastname, workplace, acces, accesLevel, netRate, email, password, activeFlag from Employees;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val employees = selectEmployees().map(row => Employee(row[Long]("id"), row[String]("name"), row[String]("lastname"),
        row[String]("workplace"), row[String]("acces"), row[Int]("accesLevel"), row[Double]("netRate"), row[String]("email"), null, row[Int]("activeFlag"))).toList
      employees
    }
  }
}

object EmployeeDao extends EmployeeDaoT
