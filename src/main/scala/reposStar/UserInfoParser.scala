package reposStar

import scalaj.http.Http
import ujson.Value
import scala.collection.mutable.ArrayBuffer

case class UserInfoParser(user: String) {
  def getRequest(reqString: String): Value ={
    val resp = Http(reqString).asString.body
    ujson.read(resp)
  }

  def getFollowersOfUser: ArrayBuffer[String]  ={
    val reqString = "https://api.github.com/users/%s/followers".format(user)
    val data = getRequest(reqString)
    try {
      data.arr.map(x => x("login").toString())
    }
    catch {
      case e: ujson.Value.InvalidData => throw new IllegalArgumentException("User is not found")
    }
  }

  def getReposInfo: ArrayBuffer[(String, Integer)] = {
    val reqString = "https://api.github.com/users/%s/repos".format((user))
    val json = getRequest(reqString)
    try {
      json.arr
        .filter(repo => !repo("private").bool)
        .map { x => (x("full_name").toString()
          .replaceAll("\"", ""),
          x("stargazers_count").toString().toInt) }
    }
    catch {
      case e: ujson.Value.InvalidData => throw new IllegalArgumentException("User is not found")
    }
  }

  def getMaxRate: Integer = {
    val maxRatedRepoTuple = this.getReposInfo.maxBy(tup => tup._2)
    maxRatedRepoTuple._2
  }
}
