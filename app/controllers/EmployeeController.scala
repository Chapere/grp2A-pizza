package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import services.EmployeeService
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import forms.{CreateEmployeeForm, CreateEmployeeLogInForm}
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
      "Name" -> text, "Vorname" -> text, "Gebiet" -> text, "Zugriff" -> text, "Stundenrate" -> text, "e-Mail" -> text, "Passwort" -> text)(CreateEmployeeForm.apply)(CreateEmployeeForm.unapply))

  val employeeLogInForm = Form(
    mapping(
      "email" -> text, "passwort" -> text)(CreateEmployeeLogInForm.apply)(CreateEmployeeLogInForm.unapply))

  def addEmployee : Action[AnyContent] = Action { implicit request =>
    employeeForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.employeeLogIn(formWithErrors, EmployeeService.registredEmployees, employeeLogInForm))
      },
      employeeData => {
        val newEmployee = services.EmployeeService.addEmployee(employeeData.name, employeeData.lastname, employeeData.workplace, employeeData.acces, employeeData.netRate, employeeData.email, employeeData.password)
        Redirect(routes.EmployeeController.newEmployeeCreated(newEmployee.name, newEmployee.lastname, newEmployee.workplace, newEmployee.acces, newEmployee.netRate, newEmployee.email, newEmployee.password)).
          flashing("success" -> "Employee saved!")
      })
  }


  def logInEmployee : Action[AnyContent] = Action { implicit request =>
    employeeLogInForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.employeeLogIn(employeeForm, EmployeeService.registredEmployees, formWithErrors))
      },
      employeeData => {
        val newEmployee = services.EmployeeService.logInEmployee(employeeData.email, employeeData.password)
        Redirect(routes.EmployeeController.completeLogInEmployee(newEmployee)).
          flashing("success" -> "Employee saved!")
      })
  }

  /**
    * Shows the welcome view for a newly registered employee.
    */
  def registerEmployee : Action[AnyContent] = Action {
    Ok(views.html.employeeLogIn(controllers.EmployeeController.employeeForm, EmployeeService.registredEmployees, controllers.EmployeeController.employeeLogInForm))
  }


  def completeLogInEmployee(name: String) : Action[AnyContent] = Action {
    Ok(views.html.employeeLoggedIn(name))
  }

  def newEmployeeCreated(name: String, lastname: String, workplace: String, acces: String, netRate: String, email: String, password: String) : Action[AnyContent] = Action {
    Ok(views.html.newEmployeeCreated(name, lastname, workplace, acces, netRate, email, password))
  }

  /**
   * Shows the welcome view for a newly registered employee.
   */

 /** def employees : Action[AnyContent] = Action {
    Ok(views.html.allEmployees(EmployeeService.registredEmployees))
  }**/

  /**
   * List all employees currently available in the system.
   */

  def showEmployees : Action[AnyContent] = Action {
    Ok(views.html.admin(EmployeeService.registredEmployees))
  }

}
