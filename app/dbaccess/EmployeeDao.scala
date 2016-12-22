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
        SQL("insert into Employees(name, lastname, workplace, acces, accesLevel, netRate, email, password) values ({name}, {lastname}, {workplace}, {acces}, {accesLevel}, {netRate}, {email}, {password})").on(
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

      models.Debug.updateUserId = id

      val chooseEmployee = Employee(employee.id, name, lastname, workplace, acces, accesLevel, netRate, email, null)

      chooseEmployee

    }

  }


  def logInEmployee(employee: Employee): String = {
    DB.withConnection { implicit c =>
      val lastname: String =
        SQL("Select lastname from Employees where email = {email} AND password = {password};").on(
          'email -> employee.email, 'password -> employee.password).
          as(SqlParser.str("lastname").single)
      models.activeUser.lastname = lastname

      val workplace: String =
        SQL("Select workplace from Employees where email = {email} AND password = {password};").on(
          'email -> employee.email, 'password -> employee.password).
          as(SqlParser.str("workplace").single)
      models.activeUser.workplace = workplace

      val acces: String =
        SQL("Select acces from Employees where email = {email} AND password = {password};").on(
          'email -> employee.email, 'password -> employee.password).
          as(SqlParser.str("acces").single)
      models.activeUser.acces = acces

      val netRate: Double =
        SQL("Select netRate from Employees where email = {email} AND password = {password};").on(
          'email -> employee.email, 'password -> employee.password).
          as(SqlParser.double("netRate").single)
      models.activeUser.netRate = netRate

      val email: String =
        SQL("Select email from Employees where email = {email} AND password = {password};").on(
          'email -> employee.email, 'password -> employee.password).
          as(SqlParser.str("email").single)
      models.activeUser.email = email

      val id: Int =
        SQL("Select id from Employees where email = {email} AND password = {password};").on(
          'email -> employee.email, 'password -> employee.password).
          as(SqlParser.int("id").single)
      models.activeUser.id = id

      val accesLevel: Int =
        SQL("Select accesLevel from Employees where email = {email} AND password = {password};").on(
          'email -> employee.email, 'password -> employee.password).
          as(SqlParser.int("accesLevel").single)
      models.activeUser.accesLevel = accesLevel

      val name: String =
        SQL("Select name from Employees where email = {email} AND password = {password};").on(
          'email -> employee.email, 'password -> employee.password).
          as(SqlParser.str("name").single)
      models.activeUser.name = name

      models.activeUser.typ = "Employee"

      name
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
      val selectEmployees = SQL("Select id, name, lastname, workplace, acces, accesLevel, netRate, email, password from Employees;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val employees = selectEmployees().map(row => Employee(row[Long]("id"), row[String]("name"), row[String]("lastname"),
        row[String]("workplace"), row[String]("acces"), row[Int]("accesLevel"), row[Double]("netRate"), null, null)).toList
      employees
    }
  }
}

object EmployeeDao extends EmployeeDaoT
