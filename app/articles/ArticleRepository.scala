package articles

import support.BusinessException

import scala.concurrent.Future

trait ArticleRepository {
  def findById(id: String): Future[Article]
}
object ArticleRepository{
  case class ArticleNotFound(id:String) extends BusinessException
}

class FakeArticleRepository extends ArticleRepository {

  def findById(id: String): Future[Article] = {
    id match {
      case "0"      => Future.successful( Article("0", "good article", 10.0) )
      case id @ "1" => Future.failed(ArticleRepository.ArticleNotFound(id))
      case "2"      => Future.failed(new java.io.IOException("Connection lost !!") )
    }
  }
}

