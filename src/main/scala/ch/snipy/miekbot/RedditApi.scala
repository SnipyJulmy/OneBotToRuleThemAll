package ch.snipy.miekbot

import java.util.stream.Collectors

import net.dean.jraw.RedditClient
import net.dean.jraw.http.{OkHttpNetworkAdapter, UserAgent}
import net.dean.jraw.models.{Submission, SubredditSort, TimePeriod}
import net.dean.jraw.oauth.{Credentials, OAuthHelper}

import scala.collection.JavaConverters._
import scala.io.Source

trait RedditApi {

  private lazy val secretId: String = sys.env.getOrElse(
    "MIEK_BOT_REDDIT",
    Source.fromFile("bot_reddit.token").getLines().mkString
  )
  private lazy val password: String = sys.env.getOrElse(
    "REDDIT_PSW",
    Source.fromFile("reddit.psw").getLines().mkString
  )
  private lazy val credentials: Credentials = Credentials.script(
    userName,
    password,
    clientId,
    secretId
  )

  private val LIMIT = 100
  private val userName: String = "SnipyJulmy"
  private val clientId: String = "L3gS__LLfMALkQ"
  private val appId: String = "ch.snipy.miekbot"
  private val version: String = "v0.1"
  private val botName: String = "MiekBot"
  private val agent = new UserAgent(
    botName,
    appId,
    version,
    userName
  )

  private val adapter = new OkHttpNetworkAdapter(agent)

  protected val reddit: RedditClient = OAuthHelper.automatic(adapter, credentials)

  protected def posts(subRedditIdentifier: String): Vector[Submission] = {
    reddit.subreddit(subRedditIdentifier).posts()
      .sorting(SubredditSort.TOP)
      .limit(LIMIT)
      .timePeriod(TimePeriod.ALL)
      .build()
      .next().stream().collect(Collectors.toList[Submission])
      .asScala.toVector
  }
}
