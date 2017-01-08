package controllers

import play.api.mvc._
import services._
import play.api.data.Form
import play.api.data.Forms._
import forms._
import play.api.data.format.Formats._
/**
 * Controller for employee specific operations.
 *
 * @author ob, scs
 */
object EmployeeController extends Controller {


  /**
   * Form object for employee data.
   */

  val employeeForm = Form(
    mapping(
      "Name" -> nonEmptyText,
      "Vorname" -> nonEmptyText,
      "Gebiet" -> nonEmptyText,
      "Zugriff" -> nonEmptyText,
      "Zugriffsebene" -> number,
      "Stundenrate" -> of(doubleFormat),
      "E-Mail" -> nonEmptyText,
      "Passwort" -> nonEmptyText)(CreateEmployeeForm.apply)(CreateEmployeeForm.unapply))


  val updateEmployeeForm = Form(
    mapping(
      "Name" -> text,
      "Vorname" -> text,
      "Gebiet" -> text,
      "Zugriff" -> text,
      "Zugriffsebene" -> number,
      "Stundenrate" -> of(doubleFormat),
      "E-Mail" -> text,
      "Passwort" -> text)(UpdateEmployeeForm.apply)(UpdateEmployeeForm.unapply))


  val employeeLogInForm = Form(
    mapping(
      "E-Mail" -> nonEmptyText,
      "Passwort" -> nonEmptyText)(CreateEmployeeLogInForm.apply)(CreateEmployeeLogInForm.unapply))

  val selectEmployeeForm = Form(
    mapping(
      "Mitarbeiter ID" -> of(longFormat))(SelectEmployeeForm.apply)(SelectEmployeeForm.unapply))

  def addEmployee : Action[AnyContent] = Action { implicit request =>
    employeeForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.employeeLoggedIn(null, models.activeUser.name, formWithErrors, controllers.EmployeeController.selectEmployeeForm, EmployeeService.registredEmployees, controllers.UserController.userForm, UserService.registeredUsers, controllers.PizzaController.pizzaForm))
      },
      employeeData => {
        val newEmployee = services.EmployeeService.addEmployee(employeeData.name, employeeData.lastname, employeeData.workplace, employeeData.acces, employeeData.accesLevel, employeeData.netRate, employeeData.email, employeeData.password)
        Redirect(routes.EmployeeController.newEmployeeCreated(newEmployee.name, newEmployee.lastname, newEmployee.workplace, newEmployee.acces, newEmployee.accesLevel, newEmployee.netRate, newEmployee.email, newEmployee.password)).
          flashing("success" -> "Employee saved!")
      })
  }


  def updateEmployee : Action[AnyContent] = Action { implicit request =>
    updateEmployeeForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      updateEmployeeData => {
        val selectEmployee = services.EmployeeService.updateEmployee(updateEmployeeData.name, updateEmployeeData.lastname, updateEmployeeData.workplace, updateEmployeeData.acces, updateEmployeeData.accesLevel, updateEmployeeData.netRate, updateEmployeeData.email, updateEmployeeData.password)
        Redirect(routes.EmployeeController.upgradeEmployee(selectEmployee.name, selectEmployee.lastname, selectEmployee.workplace, selectEmployee.acces, selectEmployee.accesLevel, selectEmployee.netRate, selectEmployee.email, selectEmployee.password)).
          flashing("success" -> "Employee saved!")
      })
  }


  def chooseEmployee : Action[AnyContent] = Action { implicit request =>
    selectEmployeeForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      selectEmployeeData => {
        val selectEmployee = services.EmployeeService.chooseEmployee(selectEmployeeData.id)
        Redirect(routes.EmployeeController.changeEmployee(selectEmployee.id, selectEmployee.name, selectEmployee.lastname, selectEmployee.workplace, selectEmployee.acces, selectEmployee.accesLevel, selectEmployee.netRate, selectEmployee.email, selectEmployee.password)).
          flashing("success" -> "Employee saved!")
      })
  }

  def logInEmployee : Action[AnyContent] = Action { implicit request =>
    employeeLogInForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.employeeLogIn(employeeForm, EmployeeService.registredEmployees, formWithErrors))
      },
      employeeData => {
        try {
          val newEmployee = services.EmployeeService.logInEmployee(employeeData.email, employeeData.password)
          Redirect(routes.EmployeeController.completeLogInEmployee(newEmployee.id, newEmployee.name)).
            flashing("success" -> "Employee saved!")
      } catch {
          case e: RuntimeException => BadRequest(views.html.loginFailed())
        }
      }
    )
  }

  /**
    * Shows the welcome view for a newly registered employee.
    */

  def registerEmployee = Action { request =>
    request.session.get("loggedInEmployee").map { userID =>
      Ok(views.html.employeeLoggedIn(userID, EmployeeService.chooseEmployee(userID.toLong).name, controllers.EmployeeController.employeeForm, controllers.EmployeeController.selectEmployeeForm, EmployeeService.registredEmployees, controllers.UserController.userForm, UserService.registeredUsers, controllers.PizzaController.pizzaForm))
    }.getOrElse {
      Ok(views.html.employeeLogIn(controllers.EmployeeController.employeeForm, EmployeeService.registredEmployees, controllers.EmployeeController.employeeLogInForm))
    }
  }

  def completeLogInEmployee(id: Long, name: String) : Action[AnyContent] = Action {
    Ok(views.html.employeeLoggedIn(id.toString, name, controllers.EmployeeController.employeeForm, controllers.EmployeeController.selectEmployeeForm, EmployeeService.registredEmployees, controllers.UserController.userForm, UserService.registeredUsers, controllers.PizzaController.pizzaForm)).withSession(
      "loggedInEmployee" -> id.toString
    )
  }

  def newEmployeeCreated(name: String, lastname: String, workplace: String, acces: String, accesLevel: Int, netRate: Double, email: String, password: String) : Action[AnyContent] = Action {
    Ok(views.html.newEmployeeCreated(name, lastname, workplace, acces, accesLevel, netRate, email, password))
  }

  def changeEmployee(id: Long, name: String, lastname: String, workplace: String, acces: String, accesLevel: Int, netRate: Double, email: String, password: String) : Action[AnyContent] = Action {
    Ok(views.html.changeEmployee(id, name, lastname, workplace, acces, accesLevel, netRate, email, password, controllers.EmployeeController.updateEmployeeForm))
  }

  def upgradeEmployee(name: String, lastname: String, workplace: String, acces: String, accesLevel: Int, netRate: Double, email: String, password: String) : Action[AnyContent] = Action {
    Ok(views.html.employeeUpdated(name, lastname, workplace, acces, accesLevel, netRate, email, password))
  }


  /**
   * Shows the welcome view for a newly registered employee.
   */

  def showEmployees : Action[AnyContent] = Action {
    Ok(views.html.admin(EmployeeService.registredEmployees))
  }

  def showAllOrderDetails : Action[AnyContent] = Action {
    Ok(views.html.allOrders(OrderService.availableOrderWithAdress))
  }

}
