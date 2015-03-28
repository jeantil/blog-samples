package mvc

import javassist.NotFoundException

import articles.ArticleRepository.ArticleNotFound
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{Result, Results}

import scala.concurrent.Future

object ResultMapper extends Results {

  import play.api.libs.json.Writes

  def jsonOk[A:Writes]: A => Result = (subject: A)=> Ok(Json.toJson(subject))

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

  def notFoundHandler(noneMsg: => String = "NotFound"): PartialFunction[Throwable, Result] = {
    case notFound: NotFoundException=> jsonNotfound(noneMsg)
  }
  val internalServerErrorHandler: PartialFunction[Throwable, Result] = {
    case e: Exception=> jsonInternalServerError(e.getMessage, e)
  }

  def toJsonResult[A](subjectFuture: Future[A])
                     (onError: PartialFunction[Throwable, Result] = notFoundHandler() )
                     (implicit writer: Writes[A]): Future[Result] = {
    val defaultHandler = notFoundHandler() orElse internalServerErrorHandler
    subjectFuture.map(jsonOk).recover(onError orElse defaultHandler)
  }
}
