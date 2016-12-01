package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import services. UserService
import forms.{CreateUserForm, CreateUserLogInForm}

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
      "Name" -> text, "Vorname" -> text, "Adresse" -> text, "Stadt" -> text, "PLZ" -> text, "e-Mail" -> text, "Passwort" -> text)(CreateUserForm.apply)(CreateUserForm.unapply))

  val userLogInForm = Form(
    mapping(
      "e-mail" -> text, "passwort" -> text)(CreateUserLogInForm.apply)(CreateUserLogInForm.unapply))

  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */
  def addUser : Action[AnyContent] = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.userLogIn(formWithErrors, UserService.registeredUsers, userLogInForm))
      },
      userData => {
        val newUser = services.UserService.addUser(userData.name, userData.lastname, userData.adress, userData.city, userData.plz, userData.email, userData.password)
        Redirect(routes.UserController.newUserCreated(newUser.name, newUser.lastname, newUser.adress, newUser.city, newUser.plz, newUser.email, newUser.password)).
          flashing("success" -> "User saved!")
      })
  }


  def logInUser : Action[AnyContent] = Action { implicit request =>
    userLogInForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.userLogIn(userForm, UserService.registeredUsers, formWithErrors))
      },
      userData => {
        val newUser = services.UserService.logInUser(userData.email, userData.password)
        Redirect(routes.UserController.completeLogInUser(newUser)).
          flashing("success" -> "User saved!")
      })
  }

  /**
   * Shows the welcome view for a newly registered user.
   */
  def registerUser : Action[AnyContent] = Action {
    Ok(views.html.userLogIn(controllers.UserController.userForm, UserService.registeredUsers, controllers.UserController.userLogInForm))
  }


  def completeLogInUser(name: String) : Action[AnyContent] = Action {
    Ok(views.html.userLoggedIn(name))
  }

  def newUserCreated(username: String, name: String, adress: String, city: String, plz: String, email: String, password: String) : Action[AnyContent] = Action {
    Ok(views.html.newUserCreated(username, name, adress, city, plz, email, password))
  }

  /**
   * List all users currently available in the system.
   */
  def showUsers : Action[AnyContent] = Action {
    Ok(views.html.benutzer(UserService.registeredUsers))
  }

}
