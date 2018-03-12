package ch.snipy.miekbot

import net.dean.jraw.RedditClient
import net.dean.jraw.http.{OkHttpNetworkAdapter, UserAgent}
import net.dean.jraw.oauth.{Credentials, OAuthHelper}

import scala.io.Source

trait RedditApi {

  private val userName: String = "SnipyJulmy"
  private val clientId: String = "L3gS__LLfMALkQ"
  private val appId: String = "ch.snipy.miekbot"
  private val version: String = "v0.1"
  private val botName: String = "MiekBot"

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

  private val agent = new UserAgent(
    botName,
    appId,
    version,
    userName
  )

  private val adapter = new OkHttpNetworkAdapter(agent)

  val reddit: RedditClient = OAuthHelper.automatic(adapter, credentials)
}
