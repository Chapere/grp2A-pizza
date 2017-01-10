package controllers

import com.ning.http.client.Response
import play.api.libs.concurrent.Promise
import play.api.libs.json.JsArray
import play.api.mvc._
import play.api.libs.ws._

/**
 * Main controller of the Pizza Service application.
 *
 * @author ob, scs
 */
object Application extends Controller {

  /**
   * Shows the start page of the application.
   *
   * @return main web page
   */
  def index : Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def impressum : Action[AnyContent] = Action {
    Ok(views.html.impressum())
  }

  /**
    * log all user out & Shows the start page of the application.
    *
    * @return main web page (NewSession)
    */
  def logOut = Action { request =>
    Ok(views.html.index()).withNewSession
  }

}
