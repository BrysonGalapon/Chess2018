package src.engine;

import src.datatypes.*;

public interface Engine {
  /**
   * Get an engine that will play random legal moves
   */
  public static Engine randomEngine(Board board) {
    return new RandomEngine(board);
  }

  /**
   * Get an engine that calculates moves using minimax algorithm
   */
  public static Engine miniMaxEngine(Board board) {
    return new MiniMaxEngine(board);
  }

  /**
   * Signals to the engine that it is its turn,
   *  and the engine will make a move on that board
   */
  public void signalTurn();
}
