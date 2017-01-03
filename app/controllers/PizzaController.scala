package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.Form
import play.api.data.Forms._
import services._
import forms._

/**
 * Controller for user specific operations.
 *
 * @author ob, scs
 */
object PizzaController extends Controller {


  /**
   * Form object for user data.
   */
  /**val userForm = Form(
    mapping(
      "Name" -> text, "Preis" -> text, "Zutaten" -> text, "Kommentar" -> text, "Zusatzstoffe" -> text)(CreateUserForm.apply)(CreateUserForm.unapply))
**/
  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */
 /** def addPizza : Action[AnyContent] = Action { implicit request =>
    pizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.produkts(formWithErrors))
      },
      userData => {
        val newUser = services.UserService.addUser(userData.name, userData.lastname, userData.adress, userData.city, userData.plz)
        Redirect(routes.UserController.newUserCreated(newUser.name, newUser.lastname, newUser.adress, newUser.city, newUser.plz)).
          flashing("success" -> "User saved!")
      })
  }**/

  /**
   * Shows the welcome view for a newly registered user.
   */

  def products = Action { request =>
    request.session.get("loggedIn").map { userID =>
      Ok(views.html.products(PizzaService.availablePizza, ProductService.availableProduct, OrderController.orderForm, userID.toLong))
    }.getOrElse {
      Ok(views.html.products(PizzaService.availablePizza, ProductService.availableProduct, OrderController.orderForm, 0))
    }
  }

  /**
   * List all users currently available in the system.
   */

}
