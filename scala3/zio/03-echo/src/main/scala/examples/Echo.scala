package examples

import zio.*
import zio.Console.*

object Main {

  @main
  def run: Unit = {

    val runtime = Runtime.default

    val program =
      for {
        s <- readLine("enter a string: ")
        _ <- printLine(s)
      } yield ()

    Unsafe.unsafe { implicit unsafe =>
      runtime.unsafe.run(program).getOrThrowFiberFailure()
    }
  }
}

