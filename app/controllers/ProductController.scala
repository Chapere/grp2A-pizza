package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import services._

/**
 * Controller for user specific operations.
 *
 * @author ob, scs
 */
object ProductController extends Controller {


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
 /** def addProduct : Action[AnyContent] = Action { implicit request =>
    productForm.bindFromRequest.fold(
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

  def products : Action[AnyContent] = Action {
    Ok(views.html.products(PizzaService.availablePizza, OrderController.orderForm, 1))
  }

  /**
   * List all users currently available in the system.
   */

}
