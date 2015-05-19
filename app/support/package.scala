
import scala.concurrent.Future
import scala.language.implicitConversions
import scalaz.OptionT

package object support {
  type FutureO[A] = OptionT[Future,A]
}
