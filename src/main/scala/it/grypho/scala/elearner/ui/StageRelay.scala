package it.grypho.scala.elearner.ui

//import scalafx.application.JFXApp3.Stage
import scalafx.stage.Stage

object StageRelay
{
  var stage: Option[Stage] = None

  def register(s: Stage): Unit =
    stage = Some(s)

  def getStage: Stage = stage match
    case None => new Stage
    case Some(s) => s

  var regexString = ""

  def setRegexString(s: String) =
    regexString = s
  def getRegexString = regexString
}
