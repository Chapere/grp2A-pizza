package rest

import play.api.mvc.{Action, AnyContent, Controller}
import play.api.libs.json.Json

/**
 * The root element of the REST API.
 */
object Api extends Controller {
  val rel = "rel"
  val href = "href"
  val method = "method"
  val get = "GET"

  /**
   * The entry point of the REST API.
   * Use for example
   * {{{
   * curl --include http://localhost:9000/api
   * }}}
   * @return just links.
   */
  def api: Action[AnyContent] = Action { implicit request =>
    Ok(Json.obj(
      "links" -> Json.arr(
        Json.obj(
          rel -> "self",
          href -> routes.Api.api.absoluteURL(),
          method -> get
        ),
        Json.obj(
          rel -> "users",
          href -> routes.Users.users.absoluteURL(),
          method -> get
        ),
        Json.obj(
          rel -> "employees",
          href -> routes.Employees.employees.absoluteURL(),
          method -> get
        ),
        Json.obj(
          rel -> "pizzas",
          href -> routes.Pizzas.pizzas.absoluteURL(),
          method -> get
        )
      )
    )
    )
  }
}
