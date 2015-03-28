package support

import scala.util.control.NoStackTrace

trait BusinessException extends RuntimeException with NoStackTrace

trait NotFoundException extends BusinessException
