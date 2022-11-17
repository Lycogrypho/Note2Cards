package it.grypho.scala.elearner.ui

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.stage.{DirectoryChooser, FileChooser, Stage}
import scalafx.scene.control.{ListView, SelectionMode, TableColumn, TableView, TextArea, TextField}
import scalafx.scene.control.{Button, CheckBox, ChoiceBox, ContentDisplay, Label, ToggleGroup, ToggleButton}
import scalafx.scene.layout.{HBox, Priority, VBox}
import scalafx.scene.input.{KeyEvent, MouseEvent}
import scalafx.scene.control.cell.TextFieldTableCell
import scalafx.event.ActionEvent
import scalafx.collections.ObservableBuffer
import javafx.scene.control.{ToggleButton => JfxToggleBtn}
import it.grypho.scala.elearner.ui.UICommons._
import it.grypho.scala.elearner.cardgen.Card
import it.grypho.scala.elearner.cardgen.CardGenerator.{createCards, fileGenerator, generateAllCards}
import it.grypho.scala.elearner.ui.StageRelay



object UIComponents
{

  //------ MicroComponents -------

  val titlePreview = new TextField
  {
    text = "Title Preview Here"
    minWidth = centerWidth - (2 * spacingSmall)
    minHeight = 20
  }

  val textPreview  = new TextArea
  {
    text = "Text Preview Here"
    minWidth = centerWidth - (2 * spacingSmall)
    minHeight = 200

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
      selectSrcFile.setText(openFileChooser(StageRelay.getStage))
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
      selectDestDir.setText(openDirChooser(StageRelay.getStage))
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
    editable = true
    //wrapText = false
    onMouseClicked = (e: MouseEvent) =>
    {
      titlePreview.setText(e.toString)
      textPreview.setText(items.getValue.toString)
    }

  }

  val resultTable = new TableView[Card]()
  {
    editable = true
    columns ++= List(
      new TableColumn[Card, String]
      {
        text = "Titolo"
        cellValueFactory = _.value.title
        cellFactory = TextFieldTableCell.forTableColumn[Card]()
        prefWidth = 180
      },
      new TableColumn[Card, String]()
      {
        text = "Testo"
        cellValueFactory = _.value.body
        cellFactory = TextFieldTableCell.forTableColumn[Card]()
        prefWidth = 180
      }
    )
    tableMenuButtonVisible = true

    selectionModel().selectionMode = SelectionMode.Multiple

    def actionForSelection(e: MouseEvent|KeyEvent) =
    {
      val selectedCard = selectionModel().getSelectedItem()

      titlePreview.setText(selectedCard.title.getValue)
      textPreview.setText(selectedCard.body.getValue)
      e match
      {
        case ev: KeyEvent => statusBar.text = ev.toString
        case _ => statusBar.text = "Action for Selection"

      }
    }

    onMouseClicked = (e: MouseEvent) => actionForSelection(e)
    onKeyReleased  = (e: KeyEvent)   => actionForSelection(e)

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

  val createButton = new Button
  {
    text = "Esporta"
    defaultButton = true
    maxWidth = Double.MaxValue
    style = "-fx-base: blue"
    onAction = (e: ActionEvent) =>
    {
      val fileName          = selectSrcFile.getText.split('\\').last.split('.').head
      val selectedCardsList = resultTable.selectionModel().getSelectedItems().toList //resultList.getItems.toList,

      if (checkBoxCSV.isSelected) fileGenerator(
        selectedCardsList,
        s"${selectDestDir.getText}\\$fileName.csv",
        "csv"
        )

      if (checkBoxTSV.isSelected) fileGenerator(
        selectedCardsList,
        s"${selectDestDir.getText}\\$fileName.tsv",
        "tsv"
        )

      if (checkBoxJSON.isSelected) fileGenerator(
        selectedCardsList,
        s"${selectDestDir.getText}\\$fileName.json",
        "JSON"
        )

      if (checkBoxTW.isSelected) fileGenerator(
        selectedCardsList,
        s"${selectDestDir.getText}\\$fileName.tx.json",
        "TW"
        )
    }

    statusBar.setText("Esportazione cards completata")

  }

  val loadButton = new Button
  {
    text = "Leggi Sorgente"
    maxWidth = Double.MaxValue
    style = "-fx-base: cyan"
    //onAction = (e: ActionEvent) => extractNGenerate("C:\\Users\\cosimoattanasi\\Desktop\\Chimica.odt")
    onAction = (e: ActionEvent) =>
    {

      statusBar.setText("Generazione cards iniziata")
      // cleanup of the existing list
      //resultList.setItems(ObservableBuffer( ))
      resultTable.setItems(ObservableBuffer())

      generateAllCards(selectSrcFile.getText)
        .reverse // last items are listed first
        //.foreach(card => resultList.setItems(resultList.getItems += card))
        .foreach(card => resultTable.setItems(resultTable.getItems += card))
      statusBar.setText("Generazione cards completata nella tabella")

    }

  }

  // Radio Button Toggle Group
  val tog: ToggleGroup = new ToggleGroup {
    selectedToggle.onChange((_, oldValue, newValue) =>
        {
          val selectedTxt = newValue.asInstanceOf[JfxToggleBtn].getText

          statusBar.text = s"Selezionato \"$selectedTxt\" come tipologia di estrazione"

          val rXString = selectedTxt match
            case "Tipo 1 (double paragraph)" => """(?s)(?i)<p />.?<p><b>(.*?)</b></p>(.*?</p>)(?:.?<p />.?<p />)"""
            case "Tipo 2 (single paragraph)" => """(?s)<p><b>(.*?)</b>(.*?)</p>.?<p />"""

          StageRelay.setRegexString(rXString)
          statusBar.text = s"Selezionato \"$rXString\" come espressione regolare di estrazione"

        }
      )
  }
  //------ MacroComponents -------

  def checks = new VBox
  {
    vgrow = Priority.Always
    hgrow = Priority.Always
    spacing = spacingSmall
    padding = Insets(insetSmall)
    children = List(
      regExSelect,
      checkBoxCSV,
      checkBoxTSV,
      checkBoxJSON,
      checkBoxTW
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
      seletSrcButton,
      loadButton
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
      seletDestButton,
      createButton
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
      resultTable //resultList
    )
  }


  def regExSelect = new VBox
  {
    vgrow = Priority.Always
    hgrow = Priority.Always
    spacing = spacingSmall
    padding = Insets(insetSmall)
    children = List(
                      new Label("Selettore"),
                      new ToggleButton
                          {
                            text = "Tipo 1 (double paragraph)"
                            selected = true
                            toggleGroup = tog
                          },
                      new ToggleButton
                        {
                          text = "Tipo 2 (single paragraph)"
                          selected = false
                          toggleGroup = tog
                        },
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