package ch.snipy.miekbot

import java.util
import java.util.Collections
import java.util.stream.Collectors

import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.api.TelegramBot
import info.mukel.telegrambot4s.methods.{ChatAction, SendChatAction, SendPhoto, SendSticker}
import info.mukel.telegrambot4s.models.InputFile
import net.dean.jraw.models.{Submission, SubredditSort, TimePeriod}

import scala.util.{Failure, Random, Success}

abstract class NsfwBot extends TelegramBot with Commands with RedditApi with Requests {

  private val LIMIT = 100

  private lazy val assPosts: util.List[Submission] = reddit.subreddit("EarthPorn").posts()
    .sorting(SubredditSort.TOP)
    .limit(LIMIT)
    .timePeriod(TimePeriod.ALL)
    .build()
    .next().stream().collect(Collectors.toList[Submission])

  private def buildImageUrl(sub: Submission): String = {
    if (sub.getUrl.contains("imgur.com"))
      if (sub.getUrl.contains("jpg"))
        s"https://i.imgur.com/${sub.getUrl.split("/").last}"
      else
        s"https://i.imgur.com/${sub.getUrl.split("/").last}.jpg"
    else
      sub.getUrl
  }

  onCommand("/ass") { implicit msg =>

    val sub = assPosts.get(Random.nextInt(LIMIT))
    val url = buildImageUrl(sub)

    println(url)

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
        val photo = InputFile(sub.getFullName + ".jpg", byteString)
        request(SendChatAction(msg.source, ChatAction.UploadPhoto))
        request(SendPhoto(msg.source, photo))
    }
  }

  onCommand("/boobs") { msg =>

  }

  onCommand("/nsfw") { msg =>

  }

  onCommand("/auranesexcite") { msg =>

  }

  onCommand("/remisenvoieenlair") { msg =>

  }

  onCommand("/emmasetouche") { msg =>

  }

  onCommand("/justinsebranle") { msg =>

  }

  onCommand("/morgansepelelejonc") { msg =>

  }

  onCommand("/thomassefrotte") { msg =>

  }
  onCommand("/jeannemouille") { msg =>

  }

  onCommand("/nathajute") { msg =>

  }

  onCommand("/sylvainsepougne") { msg =>

  }
}
