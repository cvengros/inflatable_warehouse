/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import scala.io.Source._

import com.databricks.spark.csv._

object InflatableWarehouse {
  def main(args: Array[String]) {
    // usual bullshit
    val conf = new SparkConf().setAppName("InflatableWarehouse")
    val sc = new SparkContext(conf)

    // sql stuff
    val sqlContext = new SQLContext(sc)

    // create table from csv
    sqlContext.sql("CREATE TEMPORARY TABLE bike USING com.databricks.spark.csv OPTIONS (path \"s3n://pcv-spark-test/data/bike.csv\", header \"true\")")

    // get a select to string, a bit hackish
    val path = "s3n://pcv-spark-test/data/etl.sql"
    val f = sc.textFile(path)
    val sql_string = f.map(line => line).reduce((a, b) => a + "\n" + b)

    // do a select
    var h = sqlContext.sql(sql_string)

    // print it and save it
    h.map(t => "Name: " + t(0)).collect().foreach(println)
    h.saveAsTextFile("s3n://pcv-spark-test/data/" + (System.currentTimeMillis / 1000).toString)
  }
}