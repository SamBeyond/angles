package io.ssc.angles.pipeline.explorers

import java.io.PrintWriter
import java.net.URI
import java.nio.file.{Files, Paths}
import java.util.Locale

import io.ssc.angles.pipeline.data.Storage
import org.slf4j.LoggerFactory

/**
 * Created by xolor on 11.02.15.
 */
object BuildExplorerGraph extends App {
  
  val uriToString = (uri: URI) => uri.toString
  val logger = LoggerFactory.getLogger(BuildExplorerGraph.getClass)
  
  logger.info("Fetching explorer/uri-pairs...")  
  val rawPairs : List[(String, String)] = Storage.allTweetURIPairs()
  
  logger.info("Got {} pairs", rawPairs.size)  
  val workingList :List[ClusterableTweet] = rawPairs.map( a => new ClusterableTweet(a._1, List(URI.create(a._2)))).toList
  
  val graphGenerator = new GraphGenerator
  
  logger.info("Invoking graph builder...")
  var startTime = System.currentTimeMillis()
  val graph = graphGenerator.execute(workingList, uriToString, graphGenerator.COSINE_SIMILARITY)
  var endTime = System.currentTimeMillis()
  
  //println(graph)
  logger.info("Graph generation finished within {} ms!", endTime - startTime)
  
  // write csv output
  var path = Paths.get("graph.csv")
  var writer = new PrintWriter(  Files.newBufferedWriter(path))
  
  writer.println("Source,Target,Weight,Type")
  
  graph.toList.foreach {
    case ((left, right), weight) => writer.format("\"%s\",\"%s\",%s,undirected\n", left, right, "%f".formatLocal(Locale.US, weight))
  }
  writer.flush()
  writer.close()

}
