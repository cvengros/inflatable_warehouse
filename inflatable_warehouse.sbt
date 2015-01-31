name := "InflatableWarehouse"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.2.0" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.2.0" % "provided"

libraryDependencies += "com.databricks" % "spark-csv_2.10" % "0.1"

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.1.2"