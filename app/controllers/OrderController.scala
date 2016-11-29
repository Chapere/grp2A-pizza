package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import services.OrderService
import services.PizzaService

/**
 * Controller for user specific operations.
 *
 * @author ob, scs
 */
object OrderController extends Controller {


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

  def orders : Action[AnyContent] = Action {
    Ok(views.html.produkts(PizzaService.availablePizza))
  }

  /**
   * List all users currently available in the system.
   */

}
