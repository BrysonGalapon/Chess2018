import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
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
}
