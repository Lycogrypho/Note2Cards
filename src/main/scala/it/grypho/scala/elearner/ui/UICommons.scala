package it.grypho.scala.elearner.ui

import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color.{DodgerBlue, Cyan, SeaGreen, DarkRed, Red, White, DarkGrey, Green}
import scalafx.scene.paint.{LinearGradient, Stops}

/* 
 * UICommons
 * stores all common paints and effects
 * used by UI elements 
 * 
 */

object UICommons
{
  //----- sizes ------

  val insetLarge = 7
  val insetSmall = 5
  val spacingLarge = 5
  val spacingSmall = 3

  val buttonWidth     = 300
  val listWidth       = 400
  val inputFieldWidth = 600
  val centerWidth     = 350

  val stageMinWidth = buttonWidth + listWidth + centerWidth + 2 * (insetLarge + spacingLarge) //inputFieldWidth + spacingLarge

  //----- Colors ------
  val gradientB = new LinearGradient(
    endX = 0,
    stops = Stops(Cyan, DodgerBlue)
    )

  val gradientG = new LinearGradient(
    endX = 0,
    stops = Stops(Green, SeaGreen)
    )

  val gradientR = new LinearGradient(
    endX = 0,
    stops = Stops(Red, DarkRed)
    )

  val gradientW = new LinearGradient(
    endX = 0,
    stops = Stops(White, DarkGrey)
    )
  val ds1       = new DropShadow
  {
    color = DodgerBlue
    radius = 10
    spread = 0.25
  }

  val ds2 = new DropShadow
  {
    color = DodgerBlue
    radius = 25
    spread = 0.25
  }


}