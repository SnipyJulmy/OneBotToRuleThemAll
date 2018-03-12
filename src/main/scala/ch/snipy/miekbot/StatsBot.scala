package ch.snipy.miekbot

import info.mukel.telegrambot4s.api.TelegramBot
import info.mukel.telegrambot4s.api.declarative.Commands

trait StatsBot extends TelegramBot with Commands {



  onCommand("/stats") { msg =>

  }
}
