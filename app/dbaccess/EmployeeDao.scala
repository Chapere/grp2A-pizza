package dbaccess

import anorm.{SQL, SqlParser}
import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.Employee

/**
 * Data access object for user related operations.
 */
trait EmployeeDaoT {
  val id1 = "id"
  val name = "name"
  val lastname = "lastname"
  val workplace = "workplace"
  val acces = "acces"
  val accesLevel = "accesLevel"
  val netRate = "netRate"
  val eMail = "email"
  val activeFlag = "activeFlag"
  val leer = "leer"

  /**
   * Creates the given user in the database.
   * @param employee the user object to be stored.
   * @return the persisted user object
   */
  def addEmployee(employee: Employee): Employee = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Employees(name, lastname, workplace, acces, accesLevel, netRate, email, " +
          "password, activeFlag) values ({name}, {lastname}, {workplace}, {acces}, {accesLevel}, " +
          "{netRate}, {email}, {password}, 1)").on(
          'name -> employee.name, 'lastname -> employee.lastname, 'workplace -> employee.workplace,
          'acces -> employee.acces, 'accesLevel -> employee.accesLevel,
          'netRate -> employee.netRate, 'email -> employee.email,
          'password -> employee.password).executeInsert()
      employee.id = id.get
    }
    employee
  }

   /**
    * Edit the given user in the database.
    * @param employee the user object to be updated.
    * @return the persisted user object
    */
  def updateEmployeeDao(employee: Employee): Employee = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("UPDATE Employees SET name = {name}, lastname = {lastname}, workplace = {workplace}, " +
          "acces = {acces}, netRate = {netRate}, email = {email}, password = {password} " +
          "WHERE id = {id}").on(
          'name -> employee.name, 'lastname -> employee.lastname,
          'workplace -> employee.workplace, 'acces -> employee.acces,
          'accesLevel -> employee.accesLevel, 'netRate -> employee.netRate,
          'email -> employee.email, 'password -> employee.password,
          'id -> employee.id).executeInsert()
    }
    employee
  }

   /**
    * get the name of the user.
    * @param id the user id.
    * @return name of the user
    */
  def getEmployee(id: Double): Employee = {
    DB.withConnection { implicit c =>
      val selectEmployees = SQL("SELECT Employees.* FROM Employees WHERE id = {id};").on(
        'id -> id)
      val employees = selectEmployees().map(row => Employee(row[Long](id1),
        row[String](name), row[String](lastname),
        row[String](workplace), row[String](acces),
        row[Int](accesLevel), row[Double](netRate),
        row[String](eMail), leer, row[Int](activeFlag))).toList
      employees.head
    }
  }

  /**
    * login an employee into the system
    * @param employee employee in the database
    * @return successful login into the system
    */
  def logInEmployee(employee: Employee): Employee = {
    DB.withConnection { implicit c =>
      val selectEmployees = SQL("SELECT Employees.* FROM EMPLOYEES " +
        "WHERE email = {email} AND password = {password};").on(
        'email -> employee.email, 'password -> employee.password)
      val employees = selectEmployees().map(row => Employee(row[Long](id1),
        row[String](name), row[String](lastname),
        row[String](workplace), row[String](acces),
        row[Int](accesLevel), row[Double](netRate),
        row[String](eMail), leer, row[Int](activeFlag))).toList
      employees.head
    }
  }

  /**
    * deactivate an active employee in the system
    * @param id ID of the active employee
    * @return an inactive employee
    */
  def deactivateEmployee(id: Long): Long = {
    DB.withConnection { implicit c =>
      val updateFlag: Option[Long] =
        SQL("UPDATE Employees SET activeFlag = 0 WHERE id = {id}").on(
          'id -> id).executeInsert()
    }
    0
  }

  /**
    * activate an inactive employee in the system
    * @param id ID of the inactive employee
    * @return an active employee
    */
  def activateEmployee(id: Long): Long = {
    DB.withConnection { implicit c =>
      val updateFlag: Option[Long] =
        SQL("UPDATE Employees SET activeFlag = 1 WHERE id = {id}").on(
          'id -> id).executeInsert()
    }
    1
  }

  /**
   * Removes a employee by id from the database.
   * @param id the employee's id
   * @return a boolean success flag
   */
  def rmEmployee(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Employees " +
        "where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
   * Returns a list of available user from the database.
   * @return a list of user objects.
   */
  def availableEmployees: List[Employee] = {
    DB.withConnection { implicit c =>
      val selectEmployees = SQL("Select id, name, lastname, workplace, acces," +
        "accesLevel, netRate, email, password, activeFlag from Employees;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val employees = selectEmployees().map(row => Employee(row[Long](id1),
        row[String](name), row[String](lastname),
        row[String](workplace), row[String](acces),
        row[Int](accesLevel), row[Double](netRate),
        row[String](eMail), leer, row[Int](activeFlag))).toList
      employees
    }
  }
}

object EmployeeDao extends EmployeeDaoT
