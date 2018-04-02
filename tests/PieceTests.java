import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import src.datatypes.*;

/**
 * Class to test the Piece class
 */
public class PieceTests {
  @Test
  public void testHasMovedMoved() {
    Piece piece = Piece.rook(Color.WHITE);
    piece.setMoved();
    assertTrue("Piece should have been moved", piece.hasMoved());
  }

  @Test
  public void testHasMovedNotMoved() {
    Piece piece = Piece.bishop(Color.WHITE);
    assertFalse("Piece shouldn't have been moved on initialization", piece.hasMoved());
  }

  @Test
  public void testSetMoved() {
    Piece piece = Piece.king(Color.BLACK);
    piece.setMoved();
    assertTrue("Piece should have been moved", piece.hasMoved());
  }

  @Test
  public void testSetMovedTwice() {
    Piece piece = Piece.knight(Color.BLACK);
    piece.setMoved();
    assertTrue("Piece should have been moved", piece.hasMoved());
    piece.setMoved();
    assertTrue("Piece still should have been moved", piece.hasMoved());
  }

  @Test
  public void testGetColorMovePiece() {
    Piece piece = Piece.queen(Color.WHITE);
    assertEquals("Incorrect piece color", Color.WHITE, piece.getColor());
    piece.setMoved();
    assertEquals("Moving piece shouldn't change its color", Color.WHITE, piece.getColor());
  }

  @Test
  public void testGetColor() {
    Piece piece = Piece.queen(Color.BLACK);
    assertEquals("Incorrect piece color", Color.BLACK, piece.getColor());
  }

  @Test
  public void testGetType1() {
    Piece piece = Piece.pawn(Color.WHITE);
    assertEquals("Incorrect piece type", PieceType.PAWN, piece.getType());
  }

  @Test
  public void testGetType2() {
    Piece piece = Piece.rook(Color.BLACK);
    assertEquals("Incorrect piece type", PieceType.ROOK, piece.getType());
  }

  @Test
  public void testGetType3() {
    Piece piece = Piece.king(Color.BLACK);
    assertEquals("Incorrect piece type", PieceType.KING, piece.getType());
  }

  @Test
  public void testGetType4() {
    Piece piece = Piece.knight(Color.WHITE);
    assertEquals("Incorrect piece type", PieceType.KNIGHT, piece.getType());
    piece.setMoved();
    assertEquals("Moving piece shouldn't change its type", PieceType.KNIGHT, piece.getType());
  }

  @Test
  public void testCopyHasMoved() {
    Piece piece = Piece.queen(Color.WHITE);
    Piece pieceCopy = piece.copy();

    assertEquals("Copy should have same moved state as original", piece.hasMoved(), pieceCopy.hasMoved());
  }

  @Test
  public void testCopySetMoved() {
    Piece piece = Piece.king(Color.WHITE);
    piece.setMoved();
    Piece pieceCopy = piece.copy();

    assertEquals("Copy should have same moved state as original", piece.hasMoved(), pieceCopy.hasMoved());
  }

  @Test
  public void testCopySetMovedAfterCopy() {
    Piece piece = Piece.bishop(Color.BLACK);
    Piece pieceCopy = piece.copy();
    piece.setMoved();

    assertEquals("Copy should have different moved state as original", piece.hasMoved(), !pieceCopy.hasMoved());
  }
}
