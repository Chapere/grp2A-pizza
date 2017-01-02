package rest

import models.Pizza
import play.api.libs.json._
import play.api.mvc._
import services._

/**
 * REST API for the Pizza Class.
 */
object Products extends Controller {

  private case class HateoasPizza(pizza: Pizza, url: String, name: String, ingredients: String, comment: String, supplements: String)

  private def mkHateoasPizza(pizza: Pizza)(implicit request: RequestHeader): HateoasPizza = {
    val url = routes.Pizzas.pizza(pizza.id).absoluteURL()
    val name = routes.Pizzas.pizza(pizza.id).absoluteURL()
    val ingredients = routes.Pizzas.pizza(pizza.id).absoluteURL()
    val comment = routes.Pizzas.pizza(pizza.id).absoluteURL()
    val supplements = routes.Pizzas.pizza(pizza.id).absoluteURL()

    HateoasPizza(pizza, url, name, ingredients, comment, supplements)
  }

  private implicit val hateoasPizzaWrites = new Writes[HateoasPizza] {
    def writes(hpizza: HateoasPizza): JsValue = Json.obj(
      "pizza" -> Json.obj(
        "id" -> hpizza.pizza.id,
        "name" -> hpizza.pizza.name,
        "lastname" -> hpizza.pizza.price,
        "adress" -> hpizza.pizza.ingredients,
        "city" -> hpizza.pizza.comment,
        "plz" -> hpizza.pizza.supplements
      ),
      "links" -> Json.arr(
        Json.obj(
          "rel" -> "self",
          "href" -> hpizza.url,
          "method" -> "GET"
        ),
        Json.obj(
          "rel" -> "remove",
          "href" -> hpizza.url,
          "method" -> "DELETE"
        )
      )
    )
  }

  /**
   * Get all users.
   * {{{
   * curl --include http://localhost:9000/api/users
   * }}}
   * @return all users in a JSON representation.
   */
  def pizzas: Action[AnyContent] = Action { implicit request =>
    val pizzas = PizzaService.availablePizza
    Ok(Json.obj(
      "pizzas" -> Json.toJson(pizzas.map { pizza => Json.toJson(mkHateoasPizza(pizza)) }),
      "links" -> Json.arr(
        Json.obj(
          "rel" -> "self",
          "href" -> routes.Pizzas.pizzas.absoluteURL(),
          "method" -> "GET"
        ),
        Json.obj(
          "rel" -> "create",
          "href" -> routes.Pizzas.addPizza.absoluteURL(),
          "method" -> "POST"
        )
      )
    )
    )
  }

  /**
   * Gets a user by id.
   * Use for example
   * {{{
   * curl --include http://localhost:9000/api/user/1
   * }}}
   * @param id user id.
   * @return user info in a JSON representation.
   */
  def pizza(id: Long): Action[AnyContent] = Action { implicit request =>
    PizzaService.availablePizza.find {
      _.id == id
    }.headOption.map { pizza =>
      Ok(Json.toJson(mkHateoasPizza(pizza)))
    }.getOrElse(NotFound)
  }

  private case class PizzaName(name: String, price: Double, ingredients: String, comment: String, supplements: String)
  private implicit val PizzaReads = Json.reads[PizzaName]

  /**
   * Create a new user by a POST request including the user name as JSON content.
   * Use for example
   * {{{
   * curl --include --request POST --header "Content-type: application/json"
   *      --data '{"name" : "WieAuchImmer"}' http://localhost:9000/api/user
   * }}}
   * @return info about the new user in a JSON representation
   */
  def addPizza: Action[JsValue] = Action(BodyParsers.parse.json) { implicit request =>
    val pizzaname = request.body.validate[PizzaName]
    pizzaname.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      pizzaname => {
        Ok(Json.obj("status" -> "OK",
          "pizza" -> Json.toJson(mkHateoasPizza(PizzaService.addPizza(pizzaname.name, pizzaname.price, pizzaname.ingredients, pizzaname.comment, pizzaname.supplements)))))
      }
    )
  }

  /**
   * Delete a user by id using a DELETE request.
   * {{{
   * curl --include --request DELETE http://localhost:9000/api/user/1
   * }}}
   * @param id the user id.
   * @return success info or NotFound
   */
  def rmPizza(id: Long): Action[AnyContent] = Action { implicit request =>
    val success = PizzaService.rmPizza(id)
    if (success)
      Ok(Json.obj("status" -> "OK"))
    else
      NotFound
  }
}
