import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.util.ByteString

import info.mukel.telegrambot4s.api.TelegramBot

import spray.json._

import scala.concurrent.Future

trait HttpRequest extends TelegramBot {

  def requestJson(url: String): Future[JsValue] = {
    for {
      response <- Http().singleRequest(HttpRequest(uri = Uri(url)))
      if response.status.isSuccess()
      json <- Unmarshal(response).to[String]
      ast = json.parseJson
    } yield ast
  }

  def requestImage(url: String): Future[ByteString] = {
    for {
      res <- Http().singleRequest(HttpRequest(uri = Uri(url)))
      if res.status.isSuccess()
      bytes <- Unmarshal(res).to[ByteString]
    } yield bytes
  }
}
