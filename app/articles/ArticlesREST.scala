package articles

import play.api.mvc.{Action, Controller}

class ArticlesREST(val articleRepository: ArticleRepository) extends Controller {
  def get(id: String) = Action.async { implicit request =>
    val articleOptionFuture = articleRepository.findById(id)
    mvc.ResultMapper.toJsonResult(articleOptionFuture, s"no article for $id")
  }
}
object ArticlesREST extends ArticlesREST(new FakeArticleRepository())
