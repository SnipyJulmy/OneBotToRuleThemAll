import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.util.ByteString
import info.mukel.telegrambot4s.api.declarative.{Commands, RegexCommands}
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.methods._
import info.mukel.telegrambot4s.models._
import spray.json._

import scala.util.{Failure, Random, Success}

object MiekBot extends TelegramBot with Commands with Polling with HttpRequest with RegexCommands {

  // TODO hide this (^^)
  lazy val token: String = ""

  lazy val excuses: Vector[String] = Vector(
    "Le budget est faux",
    "C'est pour le travail",
    "Je ne suis que le concierge"
  )

  lazy val validHttpCode: Set[Int] =
    (100 to 103).toSet ++
      (200 to 208).toSet ++
      (400 to 418).toSet ++
      (421 to 426).toSet ++
      (500 to 511).toSet ++
      (520 to 527).toSet ++
      Set(210, 226, 428, 429, 431, 449, 450, 451, 456)

  onRegex("""^.*[jJ][pP][pP].*$""".r) { implicit msg =>
    args => {
      reply("JPP aussi")
    }
  }

  onRegex("""//""".r) { implicit msg =>
    args => {
      reply {
        """
          |LBEF
        """.stripMargin
      }
    }
  }

  onCommand("/hello") { implicit msg =>
    reply("Hello tout le monde, salut, pénis !")
  }

  onCommand("/chaud") { implicit msg =>
    reply("Chaud !")
  }

  onCommand("/scuse") { implicit msg =>
    reply {
      excuses(Random.nextInt(excuses.length))
    }
  }

  onCommand("/cat") { implicit msg =>
    withArgs { args =>
      args.seq match {
        case Seq() => reply("No argument specified, add an http code")
        case Seq(x) =>

          if (validHttpCode.contains(x.toInt)) {
            val url = s"https://http.cat/$x"
            for {
              res <- Http().singleRequest(HttpRequest(uri = Uri(url)))
              if res.status.isSuccess()
              bytes <- Unmarshal(res).to[ByteString]
            } {
              val photo = InputFile(s"$x.jpg", bytes)
              request(SendChatAction(msg.source, ChatAction.UploadPhoto))
              request(SendPhoto(msg.source, photo))
            }
          } else {
            reply {
              s"""
                 |invalid http code, try one of the following :
                 |${validHttpCode.toList.sorted mkString " "}
              """.stripMargin
            }
          }
        case Seq(x, xs@_*) => reply("too many arguments !")
      }
    }
  }

  onCommand("/miek") { implicit msg =>

    val imgUrl = "https://78.media.tumblr.com/d7f455d1743d77d1506c069ee2c87f22/tumblr_p3cl6uh5cg1qj6sk2o1_400.gif"

    requestImage(imgUrl).onComplete {
      case Failure(exception) =>
        reply {
          s"""
             |error :
             |${exception.getMessage}
             ${exception.getCause}
          """.stripMargin
        }
      case Success(byteString) =>
        val photo = InputFile("concierge.gif", byteString)
        request(SendChatAction(msg.source, ChatAction.UploadPhoto))
        request(SendSticker(msg.source, photo)).onComplete { _ =>
          reply("Je ne suis que le concierge")
        }
    }
  }

  onCommand("/fuckoff") { implicit msg =>
    withArgs { args =>
      args.length match {
        case 0 =>
          reply {
            """
              |error on /fuckoff, receive 0 args
            """.stripMargin
          }
        case 1 =>
          reply {
            """
              |Not enough argument, some of there are here :
              |TODO
              |
              |check https://www.foaas.com/
            """.stripMargin
          }
        case _ => {
          val baseUrl = "https://www.foaas.com/"
          val url = baseUrl + args.tail.mkString("/")
          requestJson(url)
        }
      }
    }
  }

  onCommand("/jokes") { implicit msg =>
    val url = "https://08ad1pao69.execute-api.us-east-1.amazonaws.com/dev/random_joke"
    requestJson(url).onComplete {
      case Failure(exception) =>
        reply {
          s"""
             |error :
             |${exception.getMessage}
             ${exception.getCause}
          """.stripMargin
        }
      case Success(value) =>
        val data = value.asJsObject
        val setup = data.fields("setup").prettyPrint
        val punchline = data.fields("punchline").prettyPrint
        // TODO typeset the \n from the receive string
        reply {
          s"""
             |${setup.substring(1, setup.length - 1).split("\\n").mkString}

             |${punchline.substring(1, punchline.length - 1).split("\\n").mkString}
          """.stripMargin
        }
    }
  }

  onCommand("/math") { implicit msg =>
    val url = "http://numbersapi.com/random"
    for {
      response <- Http().singleRequest(HttpRequest(uri = Uri(url)))
      if response.status.isSuccess()
      text <- Unmarshal(response).to[String]
    } {
      reply(text)
    }
  }

  /**
    * [[https://whatdoestrumpthink.com/api-docs/index.html]]
    */
  onCommand("/trump") { implicit msg =>
    val url = "https://api.whatdoestrumpthink.com/api/v1/quotes/random"
    for {
      response <- Http().singleRequest(HttpRequest(uri = Uri(url)))
      if response.status.isSuccess()
      json <- Unmarshal(response).to[String]
      ast = json.parseJson
    } {
      reply(ast.asJsObject.fields("message").prettyPrint)
    }
  }

  onCommand("/sylvainsepougne") { implicit msg =>
    reply {
      """
        |   ,.   ,.
        |   \.\ /,/
        |    Y Y f
        |    |. .|
        |    ("_, l
        |     ,- , \
        |    (_)(_) Y,.
        |     _j _j |,'
        |    (_,(__,'
        |
      """.stripMargin
    }
  }
}
