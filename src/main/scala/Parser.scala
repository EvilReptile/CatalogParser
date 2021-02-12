import play.api.libs.json.{Format, JsValue, Json}

import java.io.{File, PrintWriter}
import scala.io.Source

object Parser {
    
    /**
     * Для корректной работы програмы нужно заполнить поля
     *
     * @param catalogCode код каталога, будет учитываться в JSON и имени файла
     * @param catalogName название каталога на русском для JSON
     * @param inputUrl    путь и имя файла .txt с данными каталога
     * @param outputUrl   путь и имя файла .json с JSON каталогом
     *
     */
    def parser(inputUrl: String, outputUrl: String, catalogCode: String, catalogName: String): Unit = {
        val items = Source
            .fromFile(s"$inputUrl.txt")
            .getLines
            .map { line =>
                val list = line.split("\t")
                println("line code" + list(0))
                CatalogObject(list(0), catalogCode, Json.parse(list(3)))
            }.toSeq
        
        val res = Catalog(catalogCode, Json.parse(s"{\"ru \": \"$catalogName\"}"), items)
        
        val pw = new PrintWriter(new File(s"$catalogCode.json"))
        pw.write(Json.toJson(res).toString())
        pw.close
    }
}

case class Catalog(code: String, titles: JsValue, items: Seq[CatalogObject])

object Catalog {
    implicit val format: Format[Catalog] = Json.format[Catalog]
}


case class CatalogObject(code: String, catalogCode: String, titles: JsValue, archived: Boolean = false, weight: Int = 99, data: JsValue = Json.parse("{}"))

object CatalogObject {
    implicit val format: Format[CatalogObject] = Json.format[CatalogObject]
}