package articles

case class Article(id: String, name: String, price: BigDecimal)
object Article {
  implicit val jsonFormat = play.api.libs.json.Json.format[Article]
}
