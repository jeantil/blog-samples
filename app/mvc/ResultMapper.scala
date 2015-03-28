package mvc

import articles.ArticleRepository.ArticleNotFound
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{Result, Results}

import scala.concurrent.Future

object ResultMapper extends Results {

  import play.api.libs.json.Writes

  def jsonOk[A](subject: A)(implicit writer: Writes[A]) = Ok(Json.toJson(subject))

  def jsonNotfound(msg: String) = NotFound(Json.obj("reason" -> msg))

  def exception2Location(exception: Exception): String =
    Option(exception.getStackTrace)
      .flatMap(_.headOption)
      .map(_.toString)
      .getOrElse("unknown")

  def jsonInternalServerError(msg: String, cause: Exception) = {
    val jsonMsg = Json.obj(
      "reason" -> msg,
      "location" -> exception2Location(cause)
    )
    InternalServerError(jsonMsg)
  }

  def toJsonResult[A](subjectFuture: Future[A], noneMsg: => String = "NotFound")
                     (implicit writer: Writes[A]): Future[Result] = {
    subjectFuture.map {
      case subject => jsonOk(subject)
    }.recover {
      case ArticleNotFound(id) => jsonNotfound(noneMsg)
      case e: Exception => jsonInternalServerError(e.getMessage, e)
    }
  }
}
