package src.engine;

import src.datatypes.*;
import java.util.*;

/**
 * Engine 1 - plays random moves
 */
public class Engine1 {
  // instance of board to make moves on
  private final Board board;
  private final Random generator = new Random();

  public Engine1(Board board) {
    this.board = board;
  }

  /**
   * Signal to engine that it is its turn to move
   *  - makes a move on the board
   */
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
