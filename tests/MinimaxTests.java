import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import src.datatypes.*;
import src.engine.*;

/**
 * Class to test the Minimax Engine
 */
public class MinimaxTests {
  @Test
  public void testHeuristicSymmetry() {
    String boardStr1 = "r  n  -  -  -  k  -  -" + "\n" +
                       "-  -  -  -  -  -  -  -" + "\n" + 
                       "-  -  -  b  -  -  -  -" + "\n" + 
                       "-  -  -  p  -  -  -  -" + "\n" + 
                       "-  -  -  P  -  -  -  R" + "\n" + 
                       "-  -  -  -  -  -  -  q" + "\n" + 
                       "Q  -  -  B  -  -  -  -" + "\n" + 
                       "-  K  -  -  -  -  -  -";
    Board board1 = new Board(boardStr1, Color.BLACK);

    String boardStr2 = "-  -  -  -  -  -  k  -" + "\n" +
                       "-  -  -  -  b  -  -  q" + "\n" + 
                       "Q  -  -  -  -  -  -  -" + "\n" + 
                       "r  -  -  -  p  -  -  -" + "\n" + 
                       "-  -  -  -  P  -  -  -" + "\n" + 
                       "-  -  -  -  B  -  -  -" + "\n" + 
                       "-  -  -  -  -  -  -  -" + "\n" + 
                       "-  -  K  -  -  -  N  R";
    Board board2 = new Board(boardStr2, Color.WHITE);

    MiniMaxEngine engine1 = new MiniMaxEngine(board1, Color.BLACK);
    MiniMaxEngine engine2 = new MiniMaxEngine(board2, Color.WHITE);

    double h1 = engine1.heuristic(board1);
    double h2 = engine2.heuristic(board2);

    assertEquals("Expected heuristic to evaluate position to be just as good for black as it is for white", h1, -1*h2, 0.1);
  }

  @Test
  public void testHeuristicGoodTrade() {
    String boardStr1 = "r  n  -  -  -  k  -  -" + "\n" +
                       "-  -  -  -  -  -  -  -" + "\n" + 
                       "-  -  -  b  -  -  -  -" + "\n" + 
                       "-  -  -  p  -  -  -  -" + "\n" + 
                       "-  -  -  P  -  -  -  R" + "\n" + 
                       "-  -  -  -  -  -  -  q" + "\n" + 
                       "Q  -  -  B  -  -  -  -" + "\n" + 
                       "-  K  -  -  -  -  -  -";
    Board board1 = new Board(boardStr1, Color.BLACK);

    String boardStr2 = "-  n  -  -  -  k  -  -" + "\n" +
                       "-  -  -  -  -  -  -  -" + "\n" + 
                       "-  -  -  b  -  -  -  -" + "\n" + 
                       "-  -  -  p  -  -  -  -" + "\n" + 
                       "-  -  -  P  -  -  -  R" + "\n" + 
                       "-  -  -  -  -  -  -  q" + "\n" + 
                       "K  -  -  B  -  -  -  -" + "\n" + 
                       "-  -  -  -  -  -  -  -";
    Board board2 = new Board(boardStr2, Color.BLACK);

    MiniMaxEngine engine = new MiniMaxEngine(board1, Color.BLACK);

    double h1 = engine.heuristic(board1);
    double h2 = engine.heuristic(board2);

    assertTrue("Expected heuristic to evaluate rook trade for queen as beneficial for black", h2 < h1);
  }

  @Test
  public void testHeuristicBadTrade() {
    String boardStr1 = "r  n  -  -  -  k  -  -" + "\n" +
                       "-  b  -  -  -  -  -  -" + "\n" + 
                       "-  -  -  b  -  -  -  -" + "\n" + 
                       "-  -  -  p  -  -  -  -" + "\n" + 
                       "-  -  -  P  -  -  -  -" + "\n" + 
                       "-  -  -  -  -  -  -  q" + "\n" + 
                       "Q  -  -  -  -  -  -  -" + "\n" + 
                       "-  K  -  -  -  -  -  -";
    Board board1 = new Board(boardStr1, Color.WHITE);

    String boardStr2 = "b  n  -  -  -  k  -  -" + "\n" +
                       "-  -  -  -  -  -  -  -" + "\n" + 
                       "-  -  -  b  -  -  -  -" + "\n" + 
                       "-  -  -  p  -  -  -  -" + "\n" + 
                       "-  -  -  P  -  -  -  -" + "\n" + 
                       "-  -  -  -  -  -  -  q" + "\n" + 
                       "-  -  -  -  -  -  -  -" + "\n" + 
                       "-  K  -  -  -  -  -  -";
    Board board2 = new Board(boardStr2, Color.WHITE);

    MiniMaxEngine engine = new MiniMaxEngine(board1, Color.WHITE);

    double h1 = engine.heuristic(board1);
    double h2 = engine.heuristic(board2);

    assertTrue("Expected heuristic to evaluate rook trade for queen as beneficial for black", h2 < h1);
  }

  @Test
  public void testHeuristicBadSacrifice() {
    String boardStr1 = "r  n  -  -  -  k  -  -" + "\n" +
                       "-  b  -  -  -  -  -  -" + "\n" + 
                       "-  -  -  b  -  -  -  -" + "\n" + 
                       "-  -  -  -  -  -  -  -" + "\n" + 
                       "-  -  -  P  -  -  -  -" + "\n" + 
                       "-  -  -  -  -  -  -  q" + "\n" + 
                       "Q  -  -  -  -  -  -  -" + "\n" + 
                       "-  K  -  -  -  -  -  -";
    Board board1 = new Board(boardStr1, Color.WHITE);

    String boardStr2 = "r  n  -  -  -  -  k  -" + "\n" +
                       "-  b  -  -  -  -  -  -" + "\n" + 
                       "-  -  -  b  -  -  -  -" + "\n" + 
                       "-  -  -  -  -  -  -  -" + "\n" + 
                       "-  -  -  P  -  -  -  -" + "\n" + 
                       "-  -  -  -  -  -  -  q" + "\n" + 
                       "-  -  -  -  -  -  -  -" + "\n" + 
                       "-  K  -  -  -  -  -  -";
    Board board2 = new Board(boardStr2, Color.WHITE);

    MiniMaxEngine engine = new MiniMaxEngine(board1, Color.WHITE);

    double h1 = engine.heuristic(board1);
    double h2 = engine.heuristic(board2);

    assertTrue("Expected heuristic to evaluate queen sacrifice as beneficial for black", h2 < h1);
  }

  @Test
  public void testHeuristicUpMaterial3() {
    String boardStr = "r  -  -  -  -  k  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  K  -  -  -  -  -  Q";
    Board board = new Board(boardStr, Color.WHITE);

    MiniMaxEngine engine = new MiniMaxEngine(board, Color.BLACK);

    double h = engine.heuristic(board);

    assertTrue("Expected heuristic to say white is winning", h > 0);
  }

  @Test
  public void testHeuristicUpMaterial2() {
    String boardStr = "r  -  -  -  -  k  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  K  -  -  -  -  -  Q";
    Board board = new Board(boardStr, Color.WHITE);

    MiniMaxEngine engine = new MiniMaxEngine(board, Color.WHITE);

    double h = engine.heuristic(board);

    assertTrue("Expected heuristic to say white is winning", h > 0);
  }

  @Test
  public void testHeuristicUpMaterial1() {
    String boardStr = "r  -  -  -  -  k  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  K  -  -  -  -  -  -";
    Board board = new Board(boardStr, Color.BLACK);

    MiniMaxEngine engine = new MiniMaxEngine(board, Color.BLACK);

    double h = engine.heuristic(board);

    assertTrue("Expected heuristic to say black is winning", h < 0);
  }



}

