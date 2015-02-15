package articles

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._

class ArticlesREST(val articleRepository: ArticleRepository) extends Controller {

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


  def get (id: String) = Action.async { implicit request =>
    val articleOptionFuture = articleRepository.findById (id)
    articleOptionFuture.map (articleOption =>
      articleOption.map (article =>
        Ok (Json.toJson (article) )
      ).getOrElse (NotFound (Json.obj ("reason" -> s"no article for $id") ) )
    ).recover{ case e:Exception => jsonInternalServerError(e.getMessage, e)}
  }
}
object ArticlesREST extends ArticlesREST(new FakeArticleRepository())
