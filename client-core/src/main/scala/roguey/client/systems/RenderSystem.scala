package roguey.client.systems

import entitysystem._

import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame

import com.googlecode.lanterna.graphics.TextGraphics
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal

class RenderSystem extends System {

  private val screen = {
    val defaultTerminalFactory = new DefaultTerminalFactory()
    val term                   = defaultTerminalFactory.createTerminalEmulator
    new TerminalScreen(term)
  }

  screen.startScreen()

  private val graphics = screen.newTextGraphics()

  def process(delta: Float, entities: Entities, events: Seq[Event]): Seq[Event] = {
    val input = screen.pollInput()
    if (input != null) {
      screen.clear()
      graphics.putString(10, 10, input.getCharacter.toString)
    }
    screen.refresh()
    Nil
  }

  val s = "Hello World!";

  screen.refresh()

  screen.readInput()
}
