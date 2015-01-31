Spark: 
 - in memory processing distributed engine, 
 - can write your transformations in python, scala, java, SQL, MLib machine learning, some graphs, implement map & reduce in sdk
 - can run in pseudo-distributed mode on one machine with multiple CPUs
Resilient Distributed Dataset (RDD) - a dataset loaded to spark - abstraction
Action produces value, transformation produces new RDD
Hive is something that converts SQL-like language HiveQL to map-reduce jobs that can run on Hadoop, etc.
Pig is something that converts pig language to map-reduce jobs
Liugi is something for buidling pipelines - tasks and dependencies
Spark supports SQL and HiveQL - HiveQL parser more complete. 
SchemaRDD - you can run SQL on it

Mortar
 - pises si Pig skripty - bere data z s3, pusti ti na nich pig, naloaduje to do redshiftu 
 - resi to za tebe cluster, paralelni loadovani do redshiftu, jde to schedulovat

# run console:
http://spark.apache.org/docs/latest/programming-guide.html

bin/spark-shell --jars my-lib/spark-csv-assembly-0.1.1.jar,my-lib/spark-csv-assembly-0.1.1.jar 

import org.apache.spark.sql.SQLContext
val sqlContext = new SQLContext(sc)

import com.databricks.spark.csv._
val cars = sqlContext.csvFile("bike.csv")

sqlContext.sql("CREATE TEMPORARY TABLE cars USING com.databricks.spark.csv OPTIONS (path \"cars.csv\", header \"true\")")

var h = sqlContext.sql("SELECT * FROM cars")
h.map(t => "Name: " + t(0)).collect().foreach(println)
h.saveAsTextFile("dir")



## Successes:
Monday
 - run spark locally
 - import csv as a "table"
 - run simple sql and save results
 - fail: csv export, not able to build scala-csv

Tuesday:
plan:
 - deploy to EC2, ideally read from S3 - only works in east

# create keypair, needs to be in east .. amazon

# launch cluster
./spark-ec2 --key-pair=spark-east --identity-file=spark-east.pem -s 1 --copy-aws-credentials launch spark-test2
wait 2 hours

./spark-ec2 --key-pair=spark-east --identity-file=spark-east.pem -s 1 --copy-aws-credentials --resume launch spark-test2


# ssh to the cluster
./spark-ec2 --key-pair=spark-east --identity-file=spark-east.pem login spark-test2

# build the app 
sbt assembly

make just one jar:
- create folder project, put assebly.sbt there (see inflatable_warehouse)
- sbt assembly

# get the code to the cluster
scp -i ../ec2/spark-east.pem  target/scala-2.10/inflatable-warehouse_2.10-1.0.jar root@ec2-54-152-167-205.compute-1.amazonaws.com:/root/spark

# submit the app to run 
ssh to the cluster, run
spark/bin/spark-submit --class InflatableWarehouse --master spark://ec2-54-152-167-205.compute-1.amazonaws.com:7077 spark/InflatableWarehouse-assembly-1.0.jar 

# pause cluster
./spark-ec2 stop spark-test2

# restart
./spark-ec2 --identity-file=spark-east.pem  start spark-test2


