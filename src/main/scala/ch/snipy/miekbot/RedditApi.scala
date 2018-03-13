package ch.snipy.miekbot

import java.util.stream.Collectors

import net.dean.jraw.RedditClient
import net.dean.jraw.http.{OkHttpNetworkAdapter, UserAgent}
import net.dean.jraw.models.{Submission, SubredditSort, TimePeriod}
import net.dean.jraw.oauth.{Credentials, OAuthHelper}

import scala.collection.JavaConverters._
import scala.io.Source

trait RedditApi {

  lazy val secretId: String = sys.env.getOrElse(
    "MIEK_BOT_REDDIT",
    Source.fromFile("bot_reddit.token").getLines().mkString
  )
  lazy val password: String = sys.env.getOrElse(
    "REDDIT_PSW",
    Source.fromFile("reddit.psw").getLines().mkString
  )
  lazy val credentials: Credentials = Credentials.script(
    userName,
    password,
    clientId,
    secretId
  )

  val LIMIT = 300

  val userName: String = "SnipyJulmy"
  val clientId: String = "L3gS__LLfMALkQ"
  val appId: String = "ch.snipy.miekbot"
  val version: String = "v0.1"
  val botName: String = "MiekBot"
  val agent = new UserAgent(
    botName,
    appId,
    version,
    userName
  )

  val adapter = new OkHttpNetworkAdapter(agent)
  val reddit: RedditClient = OAuthHelper.automatic(adapter, credentials)

  def posts(subRedditIdentifier: String): Vector[Submission] = {
    reddit.subreddit(subRedditIdentifier)
      .posts()
      .sorting(SubredditSort.TOP)
      .limit(LIMIT)
      .timePeriod(TimePeriod.ALL)
      .build()
      .next().stream().collect(Collectors.toList[Submission])
      .asScala.toVector
  }
}