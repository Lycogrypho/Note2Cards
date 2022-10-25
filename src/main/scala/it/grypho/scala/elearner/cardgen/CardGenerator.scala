package it.grypho.scala.elearner.cardgen

import it.grypho.scala.elearner.cardgen.Card
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

  /*
  //  //how the parse() method is used
  //
  //
  //  //Step 1 −
  //  //  To use the parse() method of the parser interface
  //  //  , instantiate any of the classes providing the implementation
  //  //  for this interface
  //  //  .
  //  //
  //  //  There are individual parser classes such as PDFParser
  //  //  , OfficeParser
  //  //  , XMLParser
  //  //  , etc.You can use any of these individual document parsers.Alternatively
  //  //  , you can use either CompositeParser or AutoDetectParser that uses all the parser classes internally
  //  //  and extracts the contents of a document using a suitable parser
  //
  //  val parser: Parser = new AutoDetectParser();
  //  //  (or)
  //  //  Parser parser = new CompositeParser();
  //  //  (or)
  //  //  object of any individual parsers given in Tika Library
  //
  //  //  Step 2 −
  //  // Create a handler object.
  //  // Given below are the three content handlers −
  //  // 1) BodyContentHandler
  //  //  This class picks the body part of the XHTML output and writes that content to the output writer or output stream
  //  //  Then it redirects the XHTML content to another content handler instance.
  //  // 2) LinkContentHandler
  //  //  This class detects and picks all the H - Ref tags of the XHTML document and forwards those
  //  //  for the use of tools like web crawlers.
  //  // 3) TeeContentHandler
  //  //  This class helps in using multiple tools simultaneously.
  //  //  Since our target is to extract the text contents from a document
  //
  //  //  - instantiate BodyContentHandler as shown below −
  //  val handler: BodyContentHandler = new BodyContentHandler();
  //
  //  // Step 3 −
  //  // Create the Metadata object as shown below −
  //  val metadata: Metadata = new Metadata();
  //
  //  // Step 4 −
  //  //  Create any of the input stream objects
  //  //, and pass your file that should be extracted to it
  //  FileInputstream
  //
  //  // Instantiate a file object by passing the file path as parameter and pass this
  //  // object to the FileInputStream class constructor
  //  // Note − The path passed to the file object should not contain spaces.
  //  // The problem with these input stream classes is that they don’t support random access reads
  //  //, which is required to process some file formats efficiently.
  //  // To resolve this problem Tika provides TikaInputStream.
  //  val  file: File = new File(filepath)
  //  // FileInputStream inputstream = new FileInputStream(file);
  //  //(or)
  //  val stream: InputStream = TikaInputStream.get(new File(filename));
  //
  //  // Step 5 −
  //  // Create a parse context object as shown below −
  //  val context: ParseContext = new ParseContext();
  //
  //  //Step 6 −
  //  // Instantiate the parser object, invoke the parse method and pass all the objects required
  //  // as shown in the prototype below −
  //  parser.parse(inputstream, handler, metadata, context);
  //
  //  //Given below is the program for content extraction using the parser interface −
  */
  def extractText(filename: String) =
  {
    import org.apache.tika.exception.TikaException
    import org.apache.tika.metadata.Metadata
    import org.apache.tika.parser.{AutoDetectParser, ParseContext, Parser}
    import org.apache.tika.sax.BodyContentHandler
    import org.xml.sax.SAXException

    import java.io.{File, FileInputStream, IOException}

    //Assume sample.txt is in your current directory
    val file: File = new File(filename)

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

  def extractXML(filename: String) =
  {
    import org.apache.tika.exception.TikaException
    import org.apache.tika.metadata.Metadata
    import org.apache.tika.parser.{AutoDetectParser, ParseContext, Parser}
    import org.apache.tika.sax.{BodyContentHandler, ToXMLContentHandler, XHTMLContentHandler}
    import org.xml.sax.SAXException

    import java.io.{File, FileInputStream, IOException}

    //Assume sample.txt is in your current directory
    val file: File = new File(filename)

    //parse method parameters
    val parser      : Parser             = new AutoDetectParser()
    val metadata_xml: Metadata           = new Metadata()
    val handler     : BodyContentHandler = new BodyContentHandler()
    val handler_xml                      = new ToXMLContentHandler() //XHTMLContentHandler(handler, metadata_xml)
    val inputstream : FileInputStream    = new FileInputStream(file)
    //val inputstream2                 = new FileInputStream(file)
    val context     : ParseContext       = new ParseContext()

    import org.apache.tika.parser.html.{HtmlMapper, IdentityHtmlMapper}
    context.set(classOf[HtmlMapper], new IdentityHtmlMapper)


    //parsing the file
    parser.parse(inputstream, handler_xml, metadata_xml, context)
    //val raw = inputstream2.readAllBytes().mkString("")
    (handler_xml, metadata_xml)
  }


  def createCards(content: String) =
  {
    import scala.util.matching.Regex

    val pattern = """<p><b>(.*)</b>(.*)</p>""".r
    {
      for (patternMatch <- pattern.findAllMatchIn(content)) yield
      {
        //println(s"Title: ${patternMatch.group(1)} \tBody: ${patternMatch.group(2)}")
        Card(patternMatch.group(1), patternMatch.group(2))
      }
    }.toList //.asInstanceOf[ObservableBuffer[Card]]

  }

  def generateAllCards(filename: String) =
  {
    //"C:\\Users\\cosimoattanasi\\Desktop\\Chimica.odt"
    val (hex, mex) = extractXML(filename)

    val c: List[Card] = CardGenerator.createCards(hex.toString)
    //val d             = c.mkString("\r\n") //map(a => s"$a")
    //println(d)
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
       case _      => Seq("")


     val writer = new PrintWriter(new File(fileName))
    //     for (line <- content)
    //     {
    //       //println(line + "\r\n")
    //       writer.write(line + "\r\n")
    //     }
     writer.write(content.mkString(head, sep, tail))
     writer.close

     //println(s"$fileName - $fileType")
   }

  def fileGeneratorSingle(cards: Seq[Card], fileName: String, fileType: String = "csv") =
  {
    import java.io._

    val content: Seq[(String, String)] = fileType match
      case "txt" => {
        for (card <- cards) yield (card.title, card.toHTML())
      }
      case "tsv" => {
        for (card <- cards) yield (card.title, card.toTSV)
      }
      case "csv" => {
        for (card <- cards) yield (card.title, card.toCSV)
      }
      case "JSON" => {
        for (card <- cards) yield (card.title, card.toJSON(false))
      }
      case _     => Seq(("",""))


    for (line <- content)
    {
      val title = line._1
      val writer = new PrintWriter(new File(s"fileName - $title.$fileType"))
      writer.write(line._2)
      writer.close
    }

    //println(s"$fileName - $fileType")
  }



}
