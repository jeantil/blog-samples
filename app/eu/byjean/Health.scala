package eu.byjean

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{JsString, JsValue, Writes, Json}
import play.api.mvc.{Action, Controller}

class Health extends Controller{
  val isoDateTimeWrites = new Writes[org.joda.time.DateTime] {
    def writes(d: org.joda.time.DateTime): JsValue = JsString(d.toString(ISODateTimeFormat.dateTime()))
  }
  def check=Action { request =>
    val json = Json.obj(
      "version" -> BuildInfo.version,
      "timestamp" -> Json.toJson(DateTime.now())(isoDateTimeWrites),
      "reverse" -> routes.Health.check().absoluteURL(secure = true)(request)
    )
    Ok(json)
  }
}
