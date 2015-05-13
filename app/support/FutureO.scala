package support

import scala.concurrent.{Future, ExecutionContext}

case class FutureO[+A](future: Future[Option[A]]) extends AnyVal {
  def flatMap[B](f: A => FutureO[B])(implicit ec: ExecutionContext): FutureO[B] = {
    val newFuture = future.flatMap{
      case Some(a) => f(a).future
      case None => Future.successful(None)
    }
    FutureO(newFuture)
  }

  def map[B](f: A => B)(implicit ec: ExecutionContext): FutureO[B] =
    FutureO(future.map(option => option map f))

  def filter(p: A => Boolean)(implicit ec: ExecutionContext): FutureO[A] =
    FutureO(future.map(_.filter(p)))

  final def withFilter(p: A => Boolean)(implicit executor: ExecutionContext): FutureO[A] =
    filter(p)(executor)

  def getOrElse[AA >: A](default: AA)(implicit executor: ExecutionContext):Future[AA] =
    future.map(_.getOrElse(default))
}
