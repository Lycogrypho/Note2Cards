package it.grypho.scala.elearner.cardgen

import it.grypho.scala.elearner.cardgen.Card
import it.grypho.scala.elearner.ui.StageRelay
import scalafx.collections.ObservableBuffer

import scala.util.Try

object CardGenerator
{
  //extracting text from a file using Tika facade

  import org.apache.tika.Tika
  import org.apache.tika.exception.TikaException
  import org.xml.sax.SAXException

  import java.io.{File, IOException}

  def tikaFacadeExtractor =
  {
    //Assume sample.txt is in your current directory
    val file = new File("sample.txt")

    //Instantiating Tika facade class
    val tika = new Tika()

    val fileContent = tika.parseToString(file)
    System.out.println("Extracted Content: " + fileContent)
  }
  
  def extractText(fileName: String) =
  {
    import org.apache.tika.exception.TikaException
    import org.apache.tika.metadata.Metadata
    import org.apache.tika.parser.{AutoDetectParser, ParseContext, Parser}
    import org.apache.tika.sax.BodyContentHandler
    import org.xml.sax.SAXException

    import java.io.{File, FileInputStream, IOException}

    //Assume sample.txt is in your current directory
    val file: File = new File(fileName)

    //parse method parameters
    val parser     : Parser             = new AutoDetectParser()
    val metadata   : Metadata           = new Metadata()
    val handler    : BodyContentHandler = new BodyContentHandler()
    val inputstream: FileInputStream    = new FileInputStream(file)
    val context    : ParseContext       = new ParseContext()

    //    import org.apache.tika.parser.html.HtmlMapper
    //    import org.apache.tika.parser.html.IdentityHtmlMapper
    //    context.set(classOf[HtmlMapper], new IdentityHtmlMapper)

    //parsing the file
    parser.parse(inputstream, handler, metadata, context)

    (handler, metadata)
  }

  def extractXML(fileName: String) =
  {
    import org.apache.tika.exception.TikaException
    import org.apache.tika.metadata.Metadata
    import org.apache.tika.parser.{AutoDetectParser, ParseContext, Parser}
    import org.apache.tika.sax.{BodyContentHandler, ToXMLContentHandler, XHTMLContentHandler}
    import org.xml.sax.SAXException

    import java.io.{File, FileInputStream, IOException}

    //Assume sample.txt is in your current directory
    val file: File = new File(fileName)

    //parse method parameters
    val parser      : Parser             = new AutoDetectParser()
    val metadata_xml: Metadata           = new Metadata()
    val handler     : BodyContentHandler = new BodyContentHandler()
    val handler_xml                      = new ToXMLContentHandler() //XHTMLContentHandler(handler, metadata_xml)
    val inputstream : FileInputStream    = new FileInputStream(file)
    val context     : ParseContext       = new ParseContext()

    import org.apache.tika.parser.html.{HtmlMapper, IdentityHtmlMapper}
    context.set(classOf[HtmlMapper], new IdentityHtmlMapper)


    //parsing the file
    parser.parse(inputstream, handler_xml, metadata_xml, context)

    (handler_xml, metadata_xml)
  }
  
  def createCards(content: String) =
  {
    import scala.util.matching.Regex

    val pattern = StageRelay.regexString.stripMargin.r

    {
      for (patternMatch <- pattern.findAllMatchIn(content) ) yield
        Card(patternMatch.group(1), patternMatch.group(2))
    }.toList

  }

  def generateAllCards(fileName: String) =
  {
    val (hex, mex) = extractXML(fileName)

    val saveIntermediate = true

    if (saveIntermediate)
    {
      import java.io._

      val writer = new PrintWriter(new File(fileName + "-raw.xml"))
      writer.write(hex.toString)
      writer.close
    }
    val c: List[Card] = CardGenerator.createCards(hex.toString)
    c
  }

  def fileGenerator(cards: Seq[Card], fileName: String, fileType: String = "csv") =
  {
     import java.io._

     var head = ""
     var sep  = "\r\n"
     var tail = ""

     val content: Seq[String] = fileType match
       case "txt"  =>  for (card <- cards) yield card.toHTML()
       case "tsv"  =>  for (card <- cards) yield card.toTSV
       case "csv"  => for (card <- cards) yield card.toCSV
       case "JSON" =>
         head = "{\r\n[\r\n"
         sep  = ",\r\n"
         tail = "\r\n]\r\n}"
         for (card <- cards) yield card.toJSON(true)
       case "TW"   =>
         head = "[\r\n"
         sep = ",\r\n"
         tail = "\r\n]"
         for (card <- cards) yield card.toTW(fileName.split('\\').last.split('.').head)
       case _      => Seq("")

     val writer = new PrintWriter(new File(fileName))
     writer.write(content.mkString(head, sep, tail))
     writer.close

  }

  def fileGeneratorSingle(cards: Seq[Card], fileName: String, fileType: String = "csv") =
  {
    import java.io._

    val content: Seq[(String, String)] = fileType.toUpperCase match
      case "txt"  => for (card <- cards) yield (card.title.value, card.toHTML())
      case "tsv"  => for (card <- cards) yield (card.title.value, card.toTSV)
      case "csv"  => for (card <- cards) yield (card.title.value, card.toCSV)
      case "JSON" => for (card <- cards) yield (card.title.value, card.toJSON(false))
      case "TW"   => for (card <- cards) yield (card.title.value, card.toTW(fileName.split('\\').last.split('.').head))
      case _      => Seq(("",""))

    for (line <- content)
    {
      val title = line._1
      val writer = new PrintWriter(new File(s"fileName - $title.$fileType"))
      writer.write(line._2)
      writer.close
    }

  }
  
}
