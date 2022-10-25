package it.grypho.scala.elearner.ui

//import scalafx.application.JFXApp3.Stage
import scalafx.stage.Stage

object StageProvider
{
  var stage: Option[Stage] = None

  def register(s: Stage): Unit =
  {
    stage = Some(s)
  }

  def getStage: Stage = stage match
    case None => new Stage
    case Some(s) => s

}
