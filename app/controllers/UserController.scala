package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import services.{PizzaService, UserService}
import forms.CreateUserForm

/**
 * Controller for user specific operations.
 *
 * @author ob, scs
 */
object UserController extends Controller {

  /**
   * Form object for user data.
   */
  val userForm = Form(
    mapping(
      "Name" -> text, "Vorname" -> text, "Adresse" -> text, "Stadt" -> text, "PLZ" -> text)(CreateUserForm.apply)(CreateUserForm.unapply))

  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */
  def addUser : Action[AnyContent] = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.welcomeUser(formWithErrors, UserService.registeredUsers))
      },
      userData => {
        val newUser = services.UserService.addUser(userData.name, userData.lastname, userData.adress, userData.city, userData.plz)
        Redirect(routes.UserController.newUserCreated(newUser.name, newUser.lastname, newUser.adress, newUser.city, newUser.plz)).
          flashing("success" -> "User saved!")
      })
  }

  def addEmployee : Action[AnyContent] = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.welcomeUser(formWithErrors, UserService.registeredUsers))
      },
      userData => {
        val newUser = services.UserService.addUser(userData.name, userData.lastname, userData.adress, userData.city, userData.plz)
        Redirect(routes.UserController.newUserCreated(newUser.name, newUser.lastname, newUser.adress, newUser.city, newUser.plz)).
          flashing("success" -> "User saved!")
      })
  }

  /**
   * Shows the welcome view for a newly registered user.
   */
  def welcomeUser : Action[AnyContent] = Action {
    Ok(views.html.welcomeUser(controllers.UserController.userForm, UserService.registeredUsers))
  }

  def produkts : Action[AnyContent] = Action {
    Ok(views.html.produkts(PizzaService.availablePizza))
  }

  def newUserCreated(username: String, name: String, adress: String, city: String, plz: String) : Action[AnyContent] = Action {
    Ok(views.html.newUserCreated(username, name, adress, city, plz))
  }

  /**
   * List all users currently available in the system.
   */
  def showUsers : Action[AnyContent] = Action {
    Ok(views.html.benutzer(UserService.registeredUsers))
  }

}
