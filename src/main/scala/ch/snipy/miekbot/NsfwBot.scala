package ch.snipy.miekbot

import ch.snipy.miekbot.Debug._
import info.mukel.telegrambot4s.api.TelegramBot
import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models.{InputFile, Message}
import net.dean.jraw.models.Submission

import scala.util.{Failure, Random, Success}

trait NsfwBot extends TelegramBot with Commands with RedditApi with Requests {

  lazy val assPosts: Vector[Submission] = posts("EarthPorn")
  lazy val boobPosts: Vector[Submission] = posts("spaceporn")


  def sendImage(url: String, imageName: String = "default")(implicit msg: Message): Unit = {

    debug(url)

    requestImage(url).onComplete {
      case Failure(exception) =>
        reply {
          s"""
             |error :
             |${exception.getMessage}
             |${exception.getCause}
          """.stripMargin
        }
      case Success(byteString) =>
        val photo = InputFile(imageName + ".jpg", byteString)
        request(SendChatAction(msg.source, ChatAction.UploadPhoto))
        request(SendPhoto(msg.source, photo))
    }
  }

  def buildImageUrl(sub: Submission): String = {
    if (sub.getUrl.contains("imgur.com"))
      if (sub.getUrl.contains("jpg"))
        s"https://i.imgur.com/${sub.getUrl.split("/").last}"
      else
        s"https://i.imgur.com/${sub.getUrl.split("/").last}.jpg"
    else
      sub.getUrl
  }

  onCommand("/ass") { implicit msg =>
    val sub = assPosts.rndPick
    val url = buildImageUrl(sub)
    sendImage(url, sub.getFullName)(msg)
  }

  onCommand("/boobs") { implicit msg =>
    val sub = boobPosts.rndPick
    val url = buildImageUrl(sub)
    sendImage(url, sub.getFullName)(msg)
  }

  onCommand("/nsfw") {
    msg =>

  }

  onCommand("/auranesexcite") {
    msg =>

  }

  onCommand("/remisenvoieenlair") {
    msg =>

  }

  onCommand("/emmasetouche") {
    msg =>

  }

  onCommand("/justinsebranle") {
    msg =>

  }

  onCommand("/morgansepelelejonc") {
    msg =>

  }

  onCommand("/thomassefrotte") {
    msg =>

  }

  onCommand("/jeannemouille") {
    msg =>

  }

  onCommand("/nathajute") {
    msg =>

  }

  onCommand("/sylvainsepougne") {
    msg =>

  }

  implicit class RandomPick(subs: Vector[Submission]) {
    private val itr: Int = Random.nextInt(LIMIT)
    debug(s"rndPick : $itr, subs.length : ${subs.length}")
    def rndPick: Submission = subs(itr)
  }
}
