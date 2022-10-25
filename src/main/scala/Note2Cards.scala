

import it.grypho.scala.elearner.cardgen.CardGenerator
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.Scene
//import scalafx.scene.paint.Color._

import scalafx.geometry.{Insets, Orientation}
//import scalafx.scene.layout.VBox
//import scalafx.scene.layout.TilePane
import scalafx.scene.layout.BorderPane
//import scalafx.collections.ObservableBuffer

import it.grypho.scala.elearner.ui.UICommons._
import it.grypho.scala.elearner.ui.UIComponents._
import it.grypho.scala.elearner.ui.StageProvider



object Note2Cards extends JFXApp3
{
  override def start(): Unit =
  {
    stage = new JFXApp3.PrimaryStage
    {
      StageProvider.register(this)
      
      title.value = "Note2Cards Converter"
      width = stageMinWidth
      height = 800
      scene = new Scene
      {
        fill = gradientB
        //        content = new TilePane
        //        {
        //          padding = Insets(insetLarge)
        //          orientation = Orientation.Vertical
        //          hgap = spacingLarge
        //          vgap = spacingLarge
        //          children = List(checks, selector, buttonPane, previewer)
        //        }

        content = new BorderPane
        {
          minWidth = stageMinWidth - (4*spacingLarge)
          //maxWidth = stage.getWidth() - (4*spacingLarge)
          maxHeight = 768
          padding = Insets(insetLarge)
          top    = selector
          left   = checks
          center = previewer
          right  = results
          bottom = statusBar
        }

      }
    }
  }
}