package src.engine;

import src.datatypes.*;
import java.util.*;

/**
 * Engine that plays random moves
 */
public class RandomEngine implements Engine {
  // instance of board to make moves on
  private final Board board;
  private final Color side;
  private final Random generator = new Random();

  /**
   * Create a new random engine
   */
  public RandomEngine(Board board, Color color) {
    this.board = board;
    this.side = color;
  }

  @Override
  public void signalTurn() {
    // get random legal move and play it
    Set<Move> legalMoves = board.legalMoves();
    int i = generator.nextInt(legalMoves.size());
    int counter = 0;
    for (Move move : legalMoves) {
      if (counter == i) {
        board.move(move);
        break;
      }
      counter++;
    }
  }
}
