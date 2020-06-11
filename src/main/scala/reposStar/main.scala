package reposStar

import scala.collection.mutable.ArrayBuffer

object main {

  def main(args: Array[String]): Unit = {
    val userParser = UserInfoParser("alklepin")
    val followers = userParser.getFollowersOfUser
    val ratedFollowersRepos = followers.flatMap(follower => {
      val folParser = UserInfoParser(follower.replaceAll("\"", ""))
      folParser.getReposInfo
    })
    println("Overrated repos of followers:")
    ratedFollowersRepos
      .filter(tup => tup._2 > userParser.getMaxRate)
      .foreach(x => println(x._1))

  }

}
