/*
 *    Copyright 2017 Jean Helou
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

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
