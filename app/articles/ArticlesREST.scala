package articles

import articles.ArticleRepository.ArticleNotFound
import play.api.mvc.{Action, Controller}

class ArticlesREST(val articleRepository: ArticleRepository) extends Controller {
  def get(id: String) = Action.async { implicit request =>
    val articleFuture = articleRepository.findById(id)
    mvc.ResultMapper.toJsonResult(articleFuture){
      case ArticleNotFound(articleId)=>
        mvc.ResultMapper.jsonNotfound(s"no article for $articleId")
    }
  }
}
object ArticlesREST extends ArticlesREST(new FakeArticleRepository())
