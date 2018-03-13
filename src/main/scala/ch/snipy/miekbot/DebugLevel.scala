package ch.snipy.miekbot

sealed trait DebugLevel

case object None extends DebugLevel

case object Low extends DebugLevel

case object Dev extends DebugLevel

case object Warn extends DebugLevel

case object Full extends DebugLevel

// TODO improve debug
// TODO import ScalaColor
object Debug {

  private val level: DebugLevel = Full

  def debug(msg: String): Unit = level match {
    case Dev | Full => println(msg)
    case _ =>
  }
}
