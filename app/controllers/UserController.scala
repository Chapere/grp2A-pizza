package controllers

import forms.{CreateUserForm, CreateUserLogInForm, UpdateUserForm, SelectUserForm, DeleteUserForm}
import play.api.mvc.{Controller, Action, AnyContent}
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, of}
import services.{OrderService, UserService}
import play.api.data.format.Formats.{longFormat}

/**
 * Controller for user specific operations.
  *
  * GoogleMaps API KEY = AIzaSyC6VCdDfHl2b9yRYnJnMq2PgSjPoXlEXow
 *
 * @author ob, scs
 */
object UserController extends Controller {
 val name = "Name"
  val vorname = "Vorname"
  val Adresse = "Adresse"
  val Stadt = "Stadt"
  val entfernung = "Entfernung"
  val plz = "PLZ"
  val eMail = "E-Mail"
  val passwort = "Passwort"
  val userID = "User ID"
  val success = "success"
  val userSaved = " User saved!"
  val employeeSaved = "Employee Saved!"
  val loggedInUser = "loggedInUser"

  /**
    * Form object for user data.
    */
  val userForm = Form(
    mapping(
      name -> nonEmptyText,
      vorname -> nonEmptyText,
      Adresse -> nonEmptyText,
      Stadt -> nonEmptyText,
      plz -> nonEmptyText,
      eMail -> nonEmptyText,
      passwort -> nonEmptyText)(CreateUserForm.apply)(CreateUserForm.unapply))

  val userLogInForm = Form(
    mapping(
      eMail -> nonEmptyText,
      passwort -> nonEmptyText)(CreateUserLogInForm.apply)(CreateUserLogInForm.unapply))

  val updateUserForm = Form(
    mapping(
      "userID" -> of(longFormat),
      name -> nonEmptyText,
      vorname -> nonEmptyText,
      Adresse -> nonEmptyText,
      Stadt -> nonEmptyText,
      plz -> nonEmptyText,
      eMail -> nonEmptyText,
      passwort -> nonEmptyText)(UpdateUserForm.apply)(UpdateUserForm.unapply))

  val selectUserForm = Form(
    mapping(
      userID -> of(longFormat))(SelectUserForm.apply)(SelectUserForm.unapply))

  val deleteUserForm = Form(
    mapping(
      userID -> of(longFormat))(DeleteUserForm.apply)(DeleteUserForm.unapply))

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
        val newUser = services.UserService.addUser(userData.name, userData.lastname,
          userData.adress, userData.city, userData.plz, 0,
          userData.email, userData.password)
        Redirect(routes.UserController.newUserCreated(newUser.id, newUser.name,
          newUser.lastname, newUser.adress, newUser.city,
          newUser.plz, newUser.distance, newUser.email, newUser.password)).
          flashing(success -> userSaved)
      })
  }

  def updateUser: Action[AnyContent] = Action { implicit request =>
    updateUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      updateUserData => {
        val selectUser = services.UserService.updateUser(updateUserData.id,
          updateUserData.name, updateUserData.lastname,
          updateUserData.adress, updateUserData.city,
          updateUserData.plz, 0, updateUserData.email,
          updateUserData.password)
        Redirect(routes.UserController.upgradeUser(selectUser.id,
          selectUser.name, selectUser.lastname, selectUser.adress,
          selectUser.city, selectUser.plz, selectUser.distance,
          selectUser.email, selectUser.password)).
          flashing(success -> employeeSaved)
      })
  }

  def userFlagZero: Action[AnyContent] = Action { implicit request =>
    selectUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      updateUserData => {
        val selectUser = services.UserService.setUserFlag0(updateUserData.id)
        Redirect(routes.UserController.setUserFlag(selectUser)).
          flashing(success -> employeeSaved)
      })
  }

  def userFlagOne: Action[AnyContent] = Action { implicit request =>
    selectUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      updateUserData => {
        val selectUser = services.UserService.setUserFlag1(updateUserData.id)
        Redirect(routes.UserController.setUserFlag(selectUser)).
          flashing(success -> employeeSaved)
      })
  }

  def rmUser: Action[AnyContent] = Action { implicit request =>
    deleteUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      deleteUserData => {
        val deleteUserVal = services.UserService.rmUser(deleteUserData.id)
        Redirect(routes.UserController.userDeleted(deleteUserVal)).
          flashing(success -> userSaved)
      })
  }

  def chooseUser: Action[AnyContent] = Action { implicit request =>
    selectUserForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      selectUserData => {
        val selectUser = services.UserService.selectUser(selectUserData.id)
        Redirect(routes.UserController.changeUser1(selectUser.id,
          selectUser.name, selectUser.lastname, selectUser.adress,
          selectUser.city, selectUser.plz, selectUser.distance,
          selectUser.email, selectUser.password, selectUser.activeFlag)).
          flashing(success -> userSaved)
      })
  }

  /*
  def distanceError(email: String, password: String,
  distance: Double): Action[AnyContent] = Action { implicit request =>
    val delete = services.UserService.makeError(distance, email, password)
    BadRequest(views.html.badDistance())
  }
  */

  def logInUser: Action[AnyContent] = Action { implicit request =>
    userLogInForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.userLogIn(userForm, UserService.registeredUsers, formWithErrors))
      },
      userData => {
        try {
          val newUser = services.UserService.accesUserData(userData.email, userData.password)
          Redirect(routes.UserController.completeLogInUser(newUser.id, newUser.name)).
            flashing(success -> userSaved)
        } catch {
          case e: RuntimeException => BadRequest(views.html.loginFailed())
        }
      }
    )
  }

  /**
    * Shows the welcome view for a newly registered user.
    */
  def registerUser = Action { request =>
    request.session.get(loggedInUser).map { userID =>
      Ok(views.html.userLoggedIn(UserService.getUserByID(userID.toLong),
        OrderService.availableOrderByID(userID.toDouble), userID.toLong))
    }.getOrElse {
      Ok(views.html.userLogIn(controllers.UserController.userForm,
        UserService.registeredUsers, controllers.UserController.userLogInForm))
    }
  }

  def completeLogInUser(id: Long, name: String): Action[AnyContent] = Action {
    Ok(views.html.userLoggedIn(UserService.getUserByID(id),
      OrderService.availableOrderByID(id), id)).withSession(
      loggedInUser -> id.toString
    )
  }

  def newUserCreated(id: Long, username: String, name: String, adress: String,
                     city: String, plz: String, distance: Double,
                     email: String,
                     password: String): Action[AnyContent] = Action {
    Ok(views.html.newUserCreated(username, name, adress, city,
      plz, email, password)).withSession(
      loggedInUser -> id.toString
    )
  }

  def changeUser1(id: Long, name: String, lastname: String,
                  adress: String, city: String, plz: String,
                  distance: Double, email: String, password: String,
                  activeFlag: Int): Action[AnyContent] = Action {
    Ok(views.html.changeUser(id, name, lastname, adress,
      city, plz, distance, email, password,
      activeFlag, controllers.UserController.updateUserForm))
  }

  def upgradeUser(id: Long, name: String, lastname: String,
                  adress: String, city: String, plz: String,
                  distance: Double, email: String,
                  password: String): Action[AnyContent] = Action {
    Ok(views.html.userUpdated(id, name, lastname,
      adress, city, plz, distance, email, password))
  }

  def userDeleted(deleted: Boolean): Action[AnyContent] = Action {
    Ok(views.html.userDeleted())
  }

  def setUserFlag(id: Double): Action[AnyContent] = Action {
    Ok(views.html.userFlagChanged(id))
  }

  /**
    * List all users currently available in the system.
    */
  def showUsers: Action[AnyContent] = Action {
    Ok(views.html.allUsers(UserService.registeredUsers))
  }
}