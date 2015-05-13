package articles

import support.{FutureO, NotFoundException}

import scala.concurrent.Future

trait ArticleRepository {
  def findById(id: String): FutureO[Article]
}

object ArticleRepository{
  case class ArticleNotFound(id:String) extends NotFoundException
}

class FakeArticleRepository extends ArticleRepository {

  override def findById(id: String): FutureO[Article] = {
    val articleFO: Future[Option[Article]] = id match {
      case "0"      => Future.successful( Option(Article("0", "good article", 10.0) ))
      case id @ "1" => Future.successful(None)
      case "2"      => Future.failed(new java.io.IOException("Connection lost !!") )
    }
    FutureO(articleFO)
  }
}

