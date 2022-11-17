package it.grypho.scala.elearner.cardgen

import scalafx.beans.property.StringProperty

case class Card(rawTitle: String, rawBody: String)
{
  //  val title = purge(rawTitle)
  //  val body = purge(rawBody)
  val title = new StringProperty(this, "Title", purge(rawTitle))
  val body  = new StringProperty(this, "Body", purge(rawBody))

  title.onChange { (_, oldValue, newValue) => println(s"Value changed from `$oldValue` to `$newValue`") }
  body.onChange { (_, oldValue, newValue) => println(s"Value changed from `$oldValue` to `$newValue`") }
  def purge(s: String) = s.replace("\t", "  ").replace("\r\n", "<BR/>")

  override def toString: String = s"${title.value}\r\n${body.value}"

  //def toHTML: String = s"\"<html><header><title>$title</title></header><body>$title</body></html>\", \"<html><header><title>$title</title></header><body>$body</body></html>\""

  //def toHTML(separator: String = ",") = s"\"<html><header><title>$title</title></header><body>$title</body></html>\"$separator\"<html><header><title>$title</title></header><body>$body</body></html>\""
  def toHTML(separator: String = ",") = {
    s"\"<html><header></header><body>${title.value}</body></html>\"$separator\"<html><header><title>${title.value}</title></header><body>${body.value}</body></html>\""
  }

  def toCSV: String = toHTML(",")

  def toTSV: String = toHTML("\t")

  def toJSON(includeCardName: Boolean) = {
    s"""${
      if (includeCardName) s"\"${title.value}\": "
      else ""
    }{
         "title": "${title.value}",
         "body": "${body.value}"
     }
     """.stripMargin
  }

  def toTW(tags: String = "") =
    {
      println(title)
      val timestamp = "20221025065135356"
      s"""
          {
            "created": "$timestamp",
            "creator": "Notes2Cards",
            "text": "${body.value}",
            "title": "${title.value}",
            "tags": "$tags",
            "modified": "$timestamp",
            "modifier": "Notes2Cards",
            "tmap.id": "53ba1ab2-a8e6-47d3-9d3a-396c75abb8f0"
          }
       """.stripMargin

    }

}
