package examples

import zio.*
import zio.Console.*

object Main {

  @main
  def run: Unit = {

    val runtime = Runtime.default

    val echo =
      for {
        s <- readLine("enter a string: ")
        _ <- printLine(s)
      } yield s 

    val program = echo.repeat(Schedule.recurUntil(_.isEmpty))

    Unsafe.unsafe { implicit unsafe =>
      runtime.unsafe.run(program).getOrThrowFiberFailure()
    }
  }
}

