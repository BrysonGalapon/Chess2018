import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import java.io.*;

import src.datatypes.*;

/**
 * Class to test the Board class
 */
public class BoardTests {
  @Test
  public void testInitialBoardPieces() {
    String expected = "r  n  b  q  k  b  n  r" + "\n" +
                      "p  p  p  p  p  p  p  p" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  P  P  P  P  P  P  P" + "\n" + 
                      "R  N  B  Q  K  B  N  R";

    Board board = new Board();
    assertEquals("Initial setup of board is incorrect", expected, board.toString());
  }

  @Test
  public void testInitialBoardMoveList() {
    int expectedLength = 0;
    Board board = new Board();
    List<Move> moveList = board.getMoveList();
    assertEquals("Expected 0-length move list on startup", expectedLength, moveList.size());

    moveList.add(new Move(PieceType.PAWN, "e2", "e4"));
    List<Move> moveList2 = board.getMoveList();
    assertEquals("Broken rep-invariant: internal movelist is returned in the getMoveList() method", expectedLength, moveList2.size());
  }

  @Test
  public void testLegalMovesTiming90Percent() {
    Board board = new Board();

    long start_first_call = System.currentTimeMillis();
    board.legalMoves();
    long end_first_call = System.currentTimeMillis();

    long start_second_call = System.currentTimeMillis();
    board.legalMoves();
    long end_second_call = System.currentTimeMillis();

    long first_call_length = end_first_call-start_first_call;
    long second_call_length = end_second_call-start_second_call;
    assertTrue(String.format("second call (%d ms) isn't faster than first call (%d ms) by 90 percent", second_call_length, first_call_length), second_call_length < 0.90*first_call_length);
  }

  @Test
  public void testLegalMovesTiming50Percent() {
    Board board = new Board();

    long start_first_call = System.currentTimeMillis();
    board.legalMoves();
    long end_first_call = System.currentTimeMillis();

    long start_second_call = System.currentTimeMillis();
    board.legalMoves();
    long end_second_call = System.currentTimeMillis();

    long first_call_length = end_first_call-start_first_call;
    long second_call_length = end_second_call-start_second_call;
    assertTrue(String.format("second call (%d ms) isn't faster than first call (%d ms) by 50 percent", second_call_length, first_call_length), second_call_length < 0.50*first_call_length);
  }

  @Test
  public void testLegalMovesTiming10Percent() {
    Board board = new Board();

    long start_first_call = System.currentTimeMillis();
    board.legalMoves();
    long end_first_call = System.currentTimeMillis();

    long start_second_call = System.currentTimeMillis();
    board.legalMoves();
    long end_second_call = System.currentTimeMillis();

    long first_call_length = end_first_call-start_first_call;
    long second_call_length = end_second_call-start_second_call;
    assertTrue(String.format("second call (%d ms) isn't faster than first call (%d ms) by 10 percent", second_call_length, first_call_length), second_call_length < 0.10*first_call_length);
  }

  @Test
  public void testLegalMovesTiming05Percent() {
    Board board = new Board();

    long start_first_call = System.currentTimeMillis();
    board.legalMoves();
    long end_first_call = System.currentTimeMillis();

    long start_second_call = System.currentTimeMillis();
    board.legalMoves();
    long end_second_call = System.currentTimeMillis();

    long first_call_length = end_first_call-start_first_call;
    long second_call_length = end_second_call-start_second_call;
    assertTrue(String.format("second call (%d ms) isn't faster than first call (%d ms) by 5 percent", second_call_length, first_call_length), second_call_length < 0.05*first_call_length);
  }

  @Test
  public void testCompressBoard() {
    Board board = new Board();
    System.out.println(board.compressBoard());
  }
}
