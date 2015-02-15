import articles.Article
import play.api.libs.json.Json
import play.api.test.{WithApplication, FakeRequest, PlaySpecification, FakeApplication}

class ArticleRESTSpec extends PlaySpecification{
  val app = FakeApplication()
  "GET /articles/:id" should{
    "return 200 and existing article as json" in new WithApplication(app) {
      val Some(result)= route(FakeRequest(GET , "/articles/0"))
      status(result) should beEqualTo(OK)
      contentType(result) should beSome("application/json")
      contentAsJson(result) should beEqualTo(Json.toJson(Article("0", "good article", 10.0)))
    }
    "return 404 for missing article" in new WithApplication(app) {
      val Some(result)= route(FakeRequest(GET , "/articles/1"))
      status(result) should beEqualTo(NOT_FOUND)
      contentType(result) should beSome("application/json")
    }
    "return 500 when the repository is unavailable" in new WithApplication(app) {
      val Some(result)= route(FakeRequest(GET , "/articles/2"))
      status(result) should beEqualTo(INTERNAL_SERVER_ERROR)
      contentType(result) should beSome("application/json")
    }
  }

}
