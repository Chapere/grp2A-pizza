package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import services._
import forms._
import play.api.data.format.Formats._




/**
 * Controller for user specific operations.
  *
  * GoogleMaps API KEY = AIzaSyC6VCdDfHl2b9yRYnJnMq2PgSjPoXlEXow
 *
 * @author ob, scs
 */
object UserController extends Controller {


  /**
    * Form object for user data.
    */
  val userForm = Form(
    mapping(
      "Name" -> nonEmptyText,
      "Vorname" -> nonEmptyText,
      "Adresse" -> nonEmptyText,
      "Stadt" -> nonEmptyText,
      "PLZ" -> nonEmptyText,
      "Entfernung" -> of(doubleFormat),
      "E-Mail" -> nonEmptyText,
      "Passwort" -> nonEmptyText)(CreateUserForm.apply)(CreateUserForm.unapply))

  val userLogInForm = Form(
    mapping(
      "E-Mail" -> nonEmptyText,
      "Passwort" -> nonEmptyText)(CreateUserLogInForm.apply)(CreateUserLogInForm.unapply))

  val updateUserForm = Form(
    mapping(
      "Name" -> nonEmptyText,
      "Vorname" -> nonEmptyText,
      "Adresse" -> nonEmptyText,
      "Stadt" -> nonEmptyText,
      "PLZ" -> nonEmptyText,
      "Entfernung" -> of(doubleFormat),
      "E-Mail" -> nonEmptyText,
      "Passwort" -> nonEmptyText)(UpdateUserForm.apply)(UpdateUserForm.unapply))

  val selectUserForm = Form(
    mapping(
      "User ID" -> of(longFormat))(SelectUserForm.apply)(SelectUserForm.unapply))

  val deleteUserForm = Form(
    mapping(
      "User ID" -> of(longFormat))(DeleteUserForm.apply)(DeleteUserForm.unapply))

  /**
    * Adds a new user with the given data to the system.
    *
    * @return welcome page for new user
    */
  def addUser: Action[AnyContent] = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.userLogIn(formWithErrors, UserService.registeredUsers, userLogInForm))
      },
      userData => {
        val newUser = services.UserService.addUser(userData.name, userData.lastname, userData.adress, userData.city, userData.plz, userData.distance, userData.email, userData.password)
        Redirect(routes.UserController.newUserCreated(newUser.name, newUser.lastname, newUser.adress, newUser.city, newUser.plz, newUser.distance, newUser.email, newUser.password)).
          flashing("success" -> "User saved!")
      })
  }

  def updateUser: Action[AnyContent] = Action { implicit request =>
    updateUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.userLogIn(userForm, UserService.registeredUsers, userLogInForm))
      },
      updateUserData => {
        val selectUser = services.UserService.updateUser(updateUserData.name, updateUserData.lastname, updateUserData.adress, updateUserData.city, updateUserData.plz, updateUserData.distance, updateUserData.email, updateUserData.password)
        Redirect(routes.UserController.upgradeUser(selectUser.name, selectUser.lastname, selectUser.adress, selectUser.city, selectUser.plz, selectUser.distance, selectUser.email, selectUser.password)).
          flashing("success" -> "Employee saved!")
      })
  }

  def rmUser: Action[AnyContent] = Action { implicit request =>
    deleteUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.userLogIn(userForm, UserService.registeredUsers, userLogInForm))
      },
      deleteUserData => {
        val deleteUserVal = services.UserService.rmUser(deleteUserData.id)
        Redirect(routes.UserController.userDeleted(deleteUserVal)).
          flashing("success" -> "User saved!")
      })
  }


  def chooseUser: Action[AnyContent] = Action { implicit request =>
    selectUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.userLogIn(userForm, UserService.registeredUsers, userLogInForm))
      },
      selectUserData => {
        val selectUser = services.UserService.chooseUser(selectUserData.id)
        Redirect(routes.UserController.changeUser1(selectUser.id, selectUser.name, selectUser.lastname, selectUser.adress, selectUser.city, selectUser.plz, selectUser.distance, selectUser.email, selectUser.password)).
          flashing("success" -> "User saved!")
      })
  }

  def logInUser: Action[AnyContent] = Action { implicit request =>
    userLogInForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.userLogIn(userForm, UserService.registeredUsers, formWithErrors))
      },
      userData => {
        try {
          val newUser = services.UserService.logInUser(userData.email, userData.password)
          Redirect(routes.UserController.completeLogInUser(newUser.id, newUser.name)).
            flashing("success" -> "User saved!")
        } catch {
          case e: RuntimeException => BadRequest(views.html.loginFailed())
        }
      }
    )
  }


  /**
    * Shows the welcome view for a newly registered user.
    *
  def registerUser: Action[AnyContent] = Action {
    Ok(views.html.userLogIn(controllers.UserController.userForm, UserService.registeredUsers, controllers.UserController.userLogInForm))
  }*/


  def registerUser = Action { request =>
    request.session.get("loggedInUser").map { userID =>
      Ok(views.html.userLoggedIn(models.activeUser.name, OrderService.availableOrderByID(userID.toDouble)))
    }.getOrElse {
      Ok(views.html.userLogIn(controllers.UserController.userForm, UserService.registeredUsers, controllers.UserController.userLogInForm))
    }
  }


  def completeLogInUser(id: Long, name: String): Action[AnyContent] = Action {
    Ok(views.html.userLoggedIn(name, OrderService.availableOrderByID(id))).withSession(
      "loggedInUser" -> id.toString
    )
  }

  def newUserCreated(username: String, name: String, adress: String, city: String, plz: String, distance: Double, email: String, password: String): Action[AnyContent] = Action {
    Ok(views.html.newUserCreated(username, name, adress, city, plz, email, password))
  }

  def changeUser1(id: Long, name: String, lastname: String, adress: String, city: String, plz: String, distance: Double, email: String, password: String): Action[AnyContent] = Action {
    Ok(views.html.changeUser(id, name, lastname, adress, city, plz, distance, email, password, controllers.UserController.updateUserForm))
  }

  def upgradeUser(name: String, lastname: String, adress: String, city: String, plz: String, distance: Double, email: String, password: String): Action[AnyContent] = Action {
    Ok(views.html.userUpdated(name, lastname, adress, city, plz, distance, email, password))
  }

  def userDeleted(deleted: Boolean): Action[AnyContent] = Action {
    Ok(views.html.userDeleted(UserService.registeredUsers))
  }


  /**
    * List all users currently available in the system.
    */
  def showUsers: Action[AnyContent] = Action {
    Ok(views.html.benutzer(UserService.registeredUsers))
  }


  /**def getUserByIdentification: Action[AnyContent] = Action { implicit request =>
        try {
          val selectedUser = services.UserService.getUserByID(models.activeUser.id)
          Redirect(routes.UserController.getDistance1(selectedUser.adress, selectedUser.plz, selectedUser.city)).
            flashing("success" -> "User saved!")
        } catch {
          case e: RuntimeException => BadRequest(views.html.loginFailed())
        }
  }*/

}

