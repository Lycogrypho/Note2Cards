package it.grypho.scala.elearner.cardgen

case class Card(rawTitle: String, rawBody: String)
{
  val title = purge(rawTitle)
  val body = purge(rawBody)

  def purge(s: String) = s.replace("\t", "  ").replace("\r\n", "<BR/>")

  override def toString: String = s"$title\r\n$body"

  //def toHTML: String = s"\"<html><header><title>$title</title></header><body>$title</body></html>\", \"<html><header><title>$title</title></header><body>$body</body></html>\""

  //def toHTML(separator: String = ",") = s"\"<html><header><title>$title</title></header><body>$title</body></html>\"$separator\"<html><header><title>$title</title></header><body>$body</body></html>\""
  def toHTML(separator: String = ",") = s"\"<html><header></header><body>$title</body></html>\"$separator\"<html><header><title>$title</title></header><body>$body</body></html>\""

  def toCSV: String = toHTML(",")

  def toTSV: String = toHTML("\t")

  def toJSON(includeCardName: Boolean) =
    s"""${if (includeCardName) s"\"$title\": " else ""}{
         "title": "$title",
         "body": "$body"
     }
     """.stripMargin

  def toTW(tags: String = "") =
    {
      println(title)
      val timestamp = "20221025065135356"
      s"""
          {
            "created": "$timestamp",
            "creator": "Notes2Cards",
            "text": "$body",
            "title": "$title",
            "tags": "$tags",
            "modified": "$timestamp",
            "modifier": "Notes2Cards",
            "tmap.id": "53ba1ab2-a8e6-47d3-9d3a-396c75abb8f0"
          }
       """.stripMargin

    }

}
