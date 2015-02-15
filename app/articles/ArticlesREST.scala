package articles

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits._

class ArticlesREST(val articleRepository: ArticleRepository) extends Controller {
  def get (id: String) = Action.async { implicit request =>
    val articleOptionFuture = articleRepository.findById (id)
    articleOptionFuture.map (articleOption =>
      articleOption.map (article =>
        Ok (Json.toJson (article) )
      ).getOrElse (NotFound (Json.obj ("reason" -> s"no article for $id") ) )
    )
  }
}
object ArticlesREST extends ArticlesREST(new FakeArticleRepository())
