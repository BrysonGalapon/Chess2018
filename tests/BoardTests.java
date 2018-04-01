import static org.junit.Assert.*;
import org.junit.Test;

import src.datatypes.*;

/**
 * Class to test the Board class
 */
public class BoardTests {
  @Test
  public void testInitialBoard() {
    String rep = "r  k  b  q  k  b  k  r" + "\n" +
                 "p  p  p  p  p  p  p  p" + "\n" + 
                 "-  -  -  -  -  -  -  -" + "\n" + 
                 "-  -  -  -  -  -  -  -" + "\n" + 
                 "-  -  -  -  -  -  -  -" + "\n" + 
                 "-  -  -  -  -  -  -  -" + "\n" + 
                 "P  P  P  P  P  P  P  P" + "\n" + 
                 "R  K  B  Q  K  B  K  R";

    Board board = new Board();
    assertEquals(board.toString(), rep, "Initial setup of board is incorrect");
  }
}
