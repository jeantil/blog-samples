package articles

import scala.concurrent.Future

trait ArticleRepository {
  def findById(id: String): Future[Option[Article]]
}

class FakeArticleRepository extends ArticleRepository {

  def findById(id: String): Future[Option[Article]] = {
    id match {
      case "0"      => Future.successful( Some(Article("0", "good article", 10.0)) )
      case id @ "1" => Future.successful{ None }
      case "2"      => Future.failed(new java.io.IOException("Connection lost !!") )
    }
  }
}

