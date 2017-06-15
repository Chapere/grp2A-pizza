package rest

import models.User
import play.api.libs.json.{JsValue, JsError, Json, Writes}
import play.api.mvc.{Action, AnyContent, BodyParsers, Controller, RequestHeader}
import services.UserService

/**
  * REST API for the User Class.
  */
object Users extends Controller {
  val links = "links"
  val rel = "rel"
  val status = "status"
  val href = "href"
  val method = "method"
  val ok = "Ok"
  val user = "users"
  val self = "self"
  val get = "GET"

  private case class HateoasUser(user: User, url: String, name: String,
                                 lastname: String, adress: String,
                                 city: String, plz: String,
                                 email: String, password: String)

  private def mkHateoasUser(user: User)(implicit request: RequestHeader): HateoasUser = {
    val url = routes.Users.user(user.id).absoluteURL()
    val name = routes.Users.user(user.id).absoluteURL()
    val lastname = routes.Users.user(user.id).absoluteURL()
    val adress = routes.Users.user(user.id).absoluteURL()
    val city = routes.Users.user(user.id).absoluteURL()
    val plz = routes.Users.user(user.id).absoluteURL()
    val email = routes.Users.user(user.id).absoluteURL()
    val password = routes.Users.user(user.id).absoluteURL()

    HateoasUser(user, url, name, lastname, adress, city, plz, email, password)
  }

  private implicit val hateoasUserWrites = new Writes[HateoasUser] {
    def writes(huser: HateoasUser): JsValue = Json.obj(
      user -> Json.obj(
        "id" -> huser.user.id,
        "name" -> huser.user.name,
        "lastname" -> huser.user.lastname,
        "adress" -> huser.user.adress,
        "city" -> huser.user.city,
        "plz" -> huser.user.plz
      ),
      links -> Json.arr(
        Json.obj(
          rel -> self,
          href -> huser.url,
          method -> get
        ),
        Json.obj(
          rel -> "remove",
          href -> huser.url,
          method -> "DELETE"
        )
      )
    )
  }

  /**
    * Get all users.
    * {{{
    * curl --include http://localhost:9000/api/users
    * }}}
    *
    * @return all users in a JSON representation.
    */
  def users: Action[AnyContent] = Action { implicit request =>
    val users = UserService.registeredUsers
    Ok(Json.obj(
      user -> Json.toJson(users.map { user => Json.toJson(mkHateoasUser(user)) }),
      links -> Json.arr(
        Json.obj(
          rel -> self,
          href -> routes.Users.users.absoluteURL(),
          method -> get
        ),
        Json.obj(
          rel -> "create",
          href -> routes.Users.addUser.absoluteURL(),
          method -> "POST"
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
    *
    * @param id user id.
    * @return user info in a JSON representation.
    */
  def user(id: Long): Action[AnyContent] = Action { implicit request =>
    UserService.registeredUsers.find {
      _.id == id
    }.headOption.map { user =>
      Ok(Json.toJson(mkHateoasUser(user)))
    }.getOrElse(NotFound)
  }

  private case class Username(name: String, lastname: String,
                              adress: String, city: String,
                              plz: String, distance: Double,
                              email: String, password: String)

  private implicit val usernameReads = Json.reads[Username]

  /**
    * Create a new user by a POST request including the user name as JSON content.
    * Use for example
    * {{{
    * curl --include --request POST --header "Content-type: application/json"
    *      --data '{"name" : "WieAuchImmer"}' http://localhost:9000/api/user
    * }}}
    *
    * @return info about the new user in a JSON representation
    */
  def addUser: Action[JsValue] = Action(BodyParsers.parse.json) { implicit request =>
    val username = request.body.validate[Username]
    username.fold(
      errors => {
        BadRequest(Json.obj(status -> ok, "message" -> JsError.toFlatJson(errors)))
      },
      username => {
        Ok(Json.obj(status -> ok,
          "user" -> Json.toJson(mkHateoasUser(UserService.addUser(username.name,
            username.lastname, username.adress, username.city,
            username.plz, username.distance, username.email,
            username.password)))))
      }
    )
  }

  /**
    * Delete a user by id using a DELETE request.
    * {{{
    * curl --include --request DELETE http://localhost:9000/api/user/1
    * }}}
    *
    * @param id the user id.
    * @return success info or NotFound
    */
  def rmUser(id: Long): Action[AnyContent] = Action { implicit request =>
    val success = UserService.rmUser(id)
    if (success) {
      Ok(Json.obj(status -> ok))
    } else {
      NotFound
    }
  }
}
