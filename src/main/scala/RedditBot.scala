import akka.http.javadsl.model.headers.Authorization
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import akka.http.scaladsl.model._
import info.mukel.telegrambot4s.api.TelegramBot

import scala.io.Source

trait RedditBot extends TelegramBot {

  lazy val clientId: String = "MiekBot"
  lazy val secretId: String = sys.env.getOrElse(
    "MIEK_BOT_REDDIT",
    Source.fromFile("bot_reddit.token").getLines().mkString
  )

  val authToken = HttpRequest(
    HttpMethods.POST,
    Uri("https://www.reddit.com/api/v1/authorize"),
    List(headers.Authorization(BasicHttpCredentials(clientId, secretId))),

  )

}
