package examples

import zio.*
import zio.Console.*

object Main {

  @main
  def run: Unit = {

    val runtime = Runtime.default

    val program =
      for {
        _ <- printLine("Hello world!")
      } yield ()

    Unsafe.unsafe { implicit unsafe =>
      runtime.unsafe.run(program).getOrThrowFiberFailure()
    }
  }
}

