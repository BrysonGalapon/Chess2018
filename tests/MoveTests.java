import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import src.datatypes.*;

/**
 * Class to test the Move class
 */
public class MoveTests {
  @Test
  public void testConstructorInts1() {
    Move move = new Move(PieceType.KING, 0, 0, 1, 1);
    assertEquals("Incorrect piece type", PieceType.KING, move.getPieceType());
    assertEquals("Incorrect startRow", 0, move.getStartRow());
    assertEquals("Incorrect startCol", 0, move.getStartCol());
    assertEquals("Incorrect endRow", 1, move.getEndRow());
    assertEquals("Incorrect endCol", 1, move.getEndCol());
  }

  @Test
  public void testConstructorInts2() {
    Move move = new Move(PieceType.PAWN, 6, 0, 7, 0);
    assertEquals("Incorrect piece type", PieceType.PAWN, move.getPieceType());
    assertEquals("Incorrect startRow", 6, move.getStartRow());
    assertEquals("Incorrect startCol", 0, move.getStartCol());
    assertEquals("Incorrect endRow", 7, move.getEndRow());
    assertEquals("Incorrect endCol", 0, move.getEndCol());
  }

  @Test
  public void testConstructorInts3() {
    Move move = new Move(PieceType.KNIGHT, 4, 4, 6, 5);
    assertEquals("Incorrect piece type", PieceType.KNIGHT, move.getPieceType());
    assertEquals("Incorrect startRow", 4, move.getStartRow());
    assertEquals("Incorrect startCol", 4, move.getStartCol());
    assertEquals("Incorrect endRow", 6, move.getEndRow());
    assertEquals("Incorrect endCol", 5, move.getEndCol());
  }

  @Test
  public void testConstructorInts4() {
    Move move = new Move(PieceType.BISHOP, 7, 0, 0, 7);
    assertEquals("Incorrect piece type", PieceType.BISHOP, move.getPieceType());
    assertEquals("Incorrect startRow", 7, move.getStartRow());
    assertEquals("Incorrect startCol", 0, move.getStartCol());
    assertEquals("Incorrect endRow", 0, move.getEndRow());
    assertEquals("Incorrect endCol", 7, move.getEndCol());
  }

  @Test
  public void testConstructorInts5() {
    Move move = new Move(PieceType.QUEEN, 7, 7, 7, 2);
    assertEquals("Incorrect piece type", PieceType.QUEEN, move.getPieceType());
    assertEquals("Incorrect startRow", 7, move.getStartRow());
    assertEquals("Incorrect startCol", 7, move.getStartCol());
    assertEquals("Incorrect endRow", 7, move.getEndRow());
    assertEquals("Incorrect endCol", 2, move.getEndCol());
  }

  @Test
  public void testConstructorStrings1() {
    Move move = new Move(PieceType.ROOK, "a4", "e4");
    assertEquals("Incorrect piece type", PieceType.ROOK, move.getPieceType());
    assertEquals("Incorrect startRow", 3, move.getStartRow());
    assertEquals("Incorrect startCol", 0, move.getStartCol());
    assertEquals("Incorrect endRow", 3, move.getEndRow());
    assertEquals("Incorrect endCol", 4, move.getEndCol());
  }

  @Test
  public void testConstructorStrings2() {
    Move move = new Move(PieceType.QUEEN, "h1", "h8");
    assertEquals("Incorrect piece type", PieceType.QUEEN, move.getPieceType());
    assertEquals("Incorrect startRow", 0, move.getStartRow());
    assertEquals("Incorrect startCol", 7, move.getStartCol());
    assertEquals("Incorrect endRow", 7, move.getEndRow());
    assertEquals("Incorrect endCol", 7, move.getEndCol());
  }

  @Test
  public void testConstructorStrings3() {
    Move move = new Move(PieceType.KNIGHT, "b1", "a3");
    assertEquals("Incorrect piece type", PieceType.KNIGHT, move.getPieceType());
    assertEquals("Incorrect startRow", 0, move.getStartRow());
    assertEquals("Incorrect startCol", 1, move.getStartCol());
    assertEquals("Incorrect endRow", 2, move.getEndRow());
    assertEquals("Incorrect endCol", 0, move.getEndCol());
  }

  @Test
  public void testToString1() {
    Move move = new Move(PieceType.KNIGHT, "b1", "a3");
    String expectedString = "Nb1->a3";
    assertEquals("Incorrect move notation", expectedString, move.toString());
  }

  @Test
  public void testToString2() {
    Move move = new Move(PieceType.PAWN, "a7", "b8");
    String expectedString = "Pa7->b8";
    assertEquals("Incorrect move notation", expectedString, move.toString());
  }

  @Test
  public void testToString3() {
    Move move = new Move(PieceType.BISHOP, "f8", "g7");
    String expectedString = "Bf8->g7";
    assertEquals("Incorrect move notation", expectedString, move.toString());
  }

  @Test
  public void testToString4() {
    Move move = new Move(PieceType.QUEEN, "e4", "h8");
    String expectedString = "Qe4->h8";
    assertEquals("Incorrect move notation", expectedString, move.toString());
  }
}

