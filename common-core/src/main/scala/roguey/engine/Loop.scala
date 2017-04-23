package roguey.engine

import java.util.concurrent.Executors
import concurrent.{ExecutionContext, Await, Future}
import concurrent.duration._

import annotation.tailrec

trait Loop {

  import Loop._

  private val msPerUpdate: Int = 60

  private var last = System.nanoTime

  @tailrec
  final def loop(inputProcessor: InputProcessor, updater: Updater): Unit = {
    var lag: Long = 0
    val current   = System.nanoTime
    val elapsed   = current - last
    last = current
    lag += elapsed

    inputProcessor()

    while (lag >= msPerUpdate) {
      updater(msPerUpdate)
      lag -= msPerUpdate
    }

    Thread.sleep(1000)

    loop(inputProcessor, updater)
  }

}

object Loop extends Loop {
  type InputProcessor = () => Unit
  type Updater        = Int => Unit

  implicit val context = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

  def run(inputProcessor: InputProcessor, updater: Updater): Future[Unit] = {
    Future {
      loop(inputProcessor, updater)
    }
  }

}
