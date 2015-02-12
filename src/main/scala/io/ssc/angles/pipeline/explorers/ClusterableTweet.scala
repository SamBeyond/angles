package io.ssc.angles.pipeline.explorers

/**
 * Created by xolor on 11.02.15.
 */
trait ClusterableTweet {

  def getTweetId : String
  
  def getExplorerId : String
  
  def getURIs : List[String]

}
