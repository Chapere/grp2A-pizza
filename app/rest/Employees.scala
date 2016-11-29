package rest

import models.Employee
import play.api.libs.json.{JsError, JsValue, Json, Writes}
import play.api.mvc._
import services.EmployeeService

/**
 * REST API for the Employee Class.
 */
object Employees extends Controller {

  private case class HateoasEmployee(employee: Employee, url: String, name: String, lastname: String, workplace: String, acces: String, netRate: String, email: String, password: String)

  private def mkHateoasEmployee(employee: Employee)(implicit request: RequestHeader): HateoasEmployee = {
    val url = routes.Employees.employee(employee.id).absoluteURL()
    val name = routes.Employees.employee(employee.id).absoluteURL()
    val lastname = routes.Employees.employee(employee.id).absoluteURL()
    val workplace = routes.Employees.employee(employee.id).absoluteURL()
    val acces = routes.Employees.employee(employee.id).absoluteURL()
    val netRate = routes.Employees.employee(employee.id).absoluteURL()
    val email = routes.Employees.employee(employee.id).absoluteURL()
    val password = routes.Employees.employee(employee.id).absoluteURL()

    HateoasEmployee(employee, url, name, lastname, workplace, acces, netRate, email, password)
  }

  private implicit val hateoasEmployeeWrites = new Writes[HateoasEmployee] {
    def writes(hemployee: HateoasEmployee): JsValue = Json.obj(
      "employee" -> Json.obj(
        "id" -> hemployee.employee.id,
        "name" -> hemployee.employee.name,
        "lastname" -> hemployee.employee.lastname,
        "workplace" -> hemployee.employee.workplace,
        "acces" -> hemployee.employee.acces,
        "netRate" -> hemployee.employee.netRate,
        "email" -> hemployee.employee.email,
        "password" -> hemployee.employee.password
      ),
      "links" -> Json.arr(
        Json.obj(
          "rel" -> "self",
          "href" -> hemployee.url,
          "method" -> "GET"
        ),
        Json.obj(
          "rel" -> "remove",
          "href" -> hemployee.url,
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
  def employees: Action[AnyContent] = Action { implicit request =>
    val employees = EmployeeService.registredEmployees
    Ok(Json.obj(
      "employees" -> Json.toJson(employees.map { employee => Json.toJson(mkHateoasEmployee(employee)) }),
      "links" -> Json.arr(
        Json.obj(
          "rel" -> "self",
          "href" -> routes.Employees.employees.absoluteURL(),
          "method" -> "GET"
        ),
        Json.obj(
          "rel" -> "create",
          "href" -> routes.Employees.addEmployee.absoluteURL(),
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
  def employee(id: Long): Action[AnyContent] = Action { implicit request =>
    EmployeeService.registredEmployees.find {
      _.id == id
    }.headOption.map { employee =>
      Ok(Json.toJson(mkHateoasEmployee(employee)))
    }.getOrElse(NotFound)
  }

  private case class EmployeeName(name: String, lastname: String, workplace: String, acces: String, netRate: String, email: String, password: String)
  private implicit val EmployeeReads = Json.reads[EmployeeName]

  /**
   * Create a new user by a POST request including the user name as JSON content.
   * Use for example
   * {{{
   * curl --include --request POST --header "Content-type: application/json"
   *      --data '{"name" : "WieAuchImmer"}' http://localhost:9000/api/user
   * }}}
   * @return info about the new user in a JSON representation
   */
  def addEmployee: Action[JsValue] = Action(BodyParsers.parse.json) { implicit request =>
    val employeename = request.body.validate[EmployeeName]
    employeename.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      employeename => {
        Ok(Json.obj("status" -> "OK",
          "employee" -> Json.toJson(mkHateoasEmployee(EmployeeService.addEmployee(employeename.name, employeename.lastname, employeename.workplace, employeename.acces, employeename.netRate, employeename.email, employeename.password)))))
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
  def rmEmployee(id: Long): Action[AnyContent] = Action { implicit request =>
    val success = EmployeeService.rmEmployee(id)
    if (success)
      Ok(Json.obj("status" -> "OK"))
    else
      NotFound
  }
}
