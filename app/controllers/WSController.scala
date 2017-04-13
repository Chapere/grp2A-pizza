package controllers

import play.api.Play.current
import play.api.libs.json.{JsObject, JsValue}
import play.api.mvc._
import play.api.libs.ws.WS
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Main controller of the Pizza Service application.
 *
 * @author ob, scs
 */
object WSController extends Controller {


  private def parseGoogleJsonNumber(json: JsValue): Int = {
    val rows = (json \ "rows").as[List[JsObject]]
    val jsonDistance = (rows.head \ "elements").as[List[JsObject]].head
    (jsonDistance \ "distance" \ "value").asOpt[Int].get
  }

  def getDistance(adress: String, plz: String, city: String, email: String, password: String): Future[Double] = {
    val apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json"
    val apiKey = "AIzaSyC6VCdDfHl2b9yRYnJnMq2PgSjPoXlEXow"
    val futureDistance = WS.url(apiUrl +
      "?origins=" + models.Company.adress.replaceAll(" ", "") + "+" + models.Company.plz  + "+" + models.Company.city  + "+" +
      "&destinations=" + adress.replaceAll(" ", "")  + "+" + plz + "+" + city + "&mode=car&language=de-DE&key=" + apiKey).get
    futureDistance map { response =>
      val distanceNumber: Int = parseGoogleJsonNumber(response.json)
        val updateDistance = services.UserService.updateDistanceData(email, password, distanceNumber)
        updateDistance

    }

  }


}
