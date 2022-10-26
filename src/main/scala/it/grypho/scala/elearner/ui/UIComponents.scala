package it.grypho.scala.elearner.ui

import scalafx.Includes.*
import scalafx.scene.control.{ListView, SelectionMode, TextField}
import scalafx.stage.{FileChooser, Stage}
import it.grypho.scala.elearner.ui.UICommons.*
import it.grypho.scala.elearner.cardgen.Card
import it.grypho.scala.elearner.cardgen.CardGenerator.{createCards, generateAllCards, fileGenerator}
import it.grypho.scala.elearner.ui.StageProvider
import javafx.stage.DirectoryChooser

object UIComponents
{

  import scalafx.geometry.Insets
  import scalafx.scene.control.{CheckBox, Button, ChoiceBox, Label, ContentDisplay}
  import scalafx.scene.layout.{Priority, VBox, HBox}
  import scalafx.collections.ObservableBuffer
  import scalafx.event.ActionEvent


  //------ MicroComponents -------

  val textPreview = new TextField
  {
    text = "Text Preview Here"
    minWidth = centerWidth - (2 * spacingSmall)
    minHeight = 200
  }

  val titlePreview = new TextField
  {
    text = "Title Preview Here"
    minWidth = centerWidth - (2 * spacingSmall)
    minHeight = 20
  }

  val selectSrcFile = new TextField
  {
    promptText = "Select file to convert"
    minWidth = inputFieldWidth
    maxWidth = inputFieldWidth * 2
  }

  val seletSrcButton = new Button
  {
    text = "..."
    defaultButton = true
    maxWidth = Double.MaxValue
    style = "-fx-base: grey"
    onAction = (e: ActionEvent) =>
    {
      selectSrcFile.setText(openFileChooser(StageProvider.getStage))
      statusBar.setText("file sorgente impostato")
    }
  }

  val selectDestDir = new TextField
  {
    promptText = "Select destination folder"
    minWidth = inputFieldWidth
    maxWidth = inputFieldWidth * 2
  }

  val seletDestButton = new Button
  {
    text = "..."
    defaultButton = true
    maxWidth = Double.MaxValue
    style = "-fx-base: grey"
    onAction = (e: ActionEvent) =>
    {
      selectDestDir.setText(openDirChooser(StageProvider.getStage))
      statusBar.setText("cartella di destinazione impostata")
    }

  }

  val resultList = new ListView[Card]
  {
    minWidth = listWidth
    maxWidth = listWidth
    maxHeight = 450
    items = ObservableBuffer( )
    selectionModel().selectionMode = SelectionMode.Multiple
    //wrapText = false
  }

  val statusBar = new TextField
  {
    promptText = "Pronto"
    minWidth = stageMinWidth - (2 * spacingSmall)

  }

  val checkBoxCSV = new CheckBox
  {
    text = "Genera CSV file"
    selected = true
    disable = false
  }

  val checkBoxTSV = new CheckBox
  {
    text = "Genera TSV file"
    //selected = true
    disable = false
  }

  val checkBoxJSON = new CheckBox
  {
    text = "Genera JSON file"
    //allowIndeterminate = true
    //indeterminate = false
    disable = false
  }

  val checkBoxTW = new CheckBox
  {
    text = "Genera TiddlyWiki Tiddlers"
    //allowIndeterminate = true
    //indeterminate = false
    disable = false
  }


  //------ MacroComponents -------

  def checks = new VBox
  {
    vgrow = Priority.Always
    hgrow = Priority.Always
    spacing = spacingSmall
    padding = Insets(insetSmall)
    children = List(
      new Button
      {
        text = "Leggi Sorgente"
        maxWidth = Double.MaxValue
        style = "-fx-base: cyan"
        //onAction = (e: ActionEvent) => extractNGenerate("C:\\Users\\cosimoattanasi\\Desktop\\Chimica.odt")
        onAction = (e: ActionEvent) =>
        {

          statusBar.setText("Generazione cards iniziata")
          // cleanup of the existing list
          resultList.setItems(ObservableBuffer( ))

          generateAllCards(selectSrcFile.getText)
            .reverse   // last items are listed first
            .foreach(card => resultList.setItems(resultList.getItems += card))
          statusBar.setText("Generazione cards completata")
        }

      },

      checkBoxCSV,

      checkBoxTSV,

      checkBoxJSON,

      checkBoxTW,

      new Button
      {
        text = "Esporta"
        defaultButton = true
        maxWidth = Double.MaxValue
        style = "-fx-base: blue"
        onAction = (e: ActionEvent) =>
        {
          val fileName = selectSrcFile.getText.split('\\').last.split('.').head

          if (checkBoxCSV.isSelected) fileGenerator(
            resultList.getItems.toList,
            s"${selectDestDir.getText}\\$fileName.csv",
            "csv"
            )

          if (checkBoxTSV.isSelected) fileGenerator(
            resultList.getItems.toList,
            s"${selectDestDir.getText}\\$fileName.tsv",
            "tsv"
            )

          if (checkBoxJSON.isSelected) fileGenerator(
            resultList.getItems.toList,
            s"${selectDestDir.getText}\\$fileName.json",
            "JSON"
            )

          if (checkBoxTW.isSelected) fileGenerator(
            resultList.getItems.toList,
            s"${selectDestDir.getText}\\$fileName.tx.json",
            "TW"
            )
        }

        statusBar.setText("Esportazione cards completata")

      }
    )
  }

  def previewer = new VBox
  {
    vgrow = Priority.Sometimes
    hgrow = Priority.Always
    minWidth = centerWidth
    spacing = spacingSmall
    padding = Insets(insetSmall)
    children = List(
      new Label("Titolo"),
      titlePreview,
      new Label("Corpo"),
      textPreview
      //resultList
    )
  }

  def selectionSrc = new HBox
  {
    vgrow = Priority.Always
    hgrow = Priority.Never
    spacing = 0
    padding = Insets(0)
    children = List(
      new Label
      {
        text = "File sorgente"
        minWidth = buttonWidth
        maxWidth = buttonWidth
      },
      selectSrcFile,
      seletSrcButton
    )
  }

  def selectionDst = new HBox
  {
    vgrow = Priority.Always
    hgrow = Priority.Never
    spacing = 0
    padding = Insets(0)
    children = List(
      new Label
      {
        text = "Cartella di destinazione"
        minWidth = buttonWidth
        maxWidth = buttonWidth
      },
      selectDestDir,
      seletDestButton
    )
  }

  def selector = new VBox
  {
    vgrow = Priority.Always
    hgrow = Priority.Always
    spacing = spacingLarge
    padding = Insets(insetSmall)
    children = List(
      selectionSrc,
      selectionDst
    )
  }

  def results = new VBox
  {
    vgrow = Priority.Always
    hgrow = Priority.Always
    spacing = spacingLarge
    padding = Insets(insetSmall)
    children = List(
      new Label("Risultati"),
      resultList
    )
  }


  //------ Actions -------

  def openFileChooser(stg: Stage): String =
  {
    val fileChooser = new FileChooser()
    fileChooser.showOpenDialog(stg).getAbsolutePath
  }

  def openDirChooser(stg: Stage): String =
  {
    val dirChooser = new DirectoryChooser()
    dirChooser.showDialog(stg).getAbsolutePath
  }

}