package mvc


import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.{Result, Results}

import scala.concurrent.Future
import scalaz.OptionT

object ResultMapper extends Results {

  import play.api.libs.json.Writes
  import scalaz.std.scalaFuture._

  def jsonOk[A:Writes]: A => Result = (subject: A)=> Ok(Json.toJson(subject))

  def jsonNotFound(msg: => String) = NotFound(Json.obj("reason" -> msg))

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

  val internalServerErrorHandler: PartialFunction[Throwable, Result] = {
    case e: Exception=> jsonInternalServerError(e.getMessage, e)
  }

  def toJsonResult[A](subjectFuture: OptionT[Future,A])
                     (onNotFound : => Result,
                      onError:PartialFunction[Throwable, Result]=internalServerErrorHandler)
                     (implicit writer: Writes[A]): Future[Result] = {
    subjectFuture.map(jsonOk).getOrElse(onNotFound).recover(onError)
  }

}
