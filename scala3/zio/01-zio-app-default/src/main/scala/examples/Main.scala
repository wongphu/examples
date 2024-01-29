package examples

import zio.*
import zio.Console.*

object Main extends ZIOAppDefault {

  val program =
    for {
      _ <- printLine("Hello world!")
    } yield ()

  def run = program
}

