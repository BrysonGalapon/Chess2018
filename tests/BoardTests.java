import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;

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
  public void testCompressBoardSamePosition1() {
    Move move = new Move(PieceType.PAWN, "c2", "c4");
    List<Move> moveList = new ArrayList<>(Arrays.asList(move));
    List<Piece> capturedPieces = new ArrayList<>();

    String boardStr = "r  n  b  q  k  b  n  r" + "\n" +
                      "p  p  p  p  p  p  p  p" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  P  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  P  -  P  P  P  P  P" + "\n" + 
                      "R  N  B  Q  K  B  N  R";

    Board board1 = new Board(boardStr, Color.BLACK, moveList, capturedPieces);

    Board board2 = new Board();
    board2.move(move);

    String rep1 = board1.compressBoard();
    String rep2 = board2.compressBoard();
    assertEquals("Equivalent positions not compressing to same value", rep1, rep2);
  }

  @Test
  public void testCompressBoardSamePosition2() {
    Move move1 = new Move(PieceType.PAWN, "e2", "e4");
    Move move2 = new Move(PieceType.PAWN, "e7", "e5");

    Board board1 = new Board();

    board1.move(move1);
    board1.move(move2);

    String boardStr = "r  n  b  q  k  b  n  r" + "\n" +
                      "p  p  p  p  -  p  p  p" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  p  -  -  -" + "\n" + 
                      "-  -  -  -  P  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  P  P  P  -  P  P  P" + "\n" + 
                      "R  N  B  Q  K  B  N  R";
    List<Move> moveList = new ArrayList<>(Arrays.asList(move1, move2));
    List<Piece> capturedPieces = new ArrayList<>();
    Board board2 = new Board(boardStr, Color.WHITE, moveList, capturedPieces);

    String rep1 = board1.compressBoard();
    String rep2 = board2.compressBoard();
    assertEquals("Equivalent positions not compressing to same value", rep1, rep2);
  }

  @Test
  public void testCompressBoardDifferentPosition2() {
    Move move = new Move(PieceType.PAWN, "e2", "e4");

    Board board1 = new Board();
    board1.move(move);

    Board board2 = new Board();

    String rep1 = board1.compressBoard();
    String rep2 = board2.compressBoard();
    assertFalse("Different positions not compressing to different value", rep1.equals(rep2));
  }

  @Test
  public void testBoardConstructorInitialPosition() {
    String boardStr = "r  n  b  q  k  b  n  r" + "\n" +
                      "p  p  p  p  p  p  p  p" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  P  P  P  P  P  P  P" + "\n" + 
                      "R  N  B  Q  K  B  N  R";

    Board board = new Board(boardStr, Color.WHITE);

    String rep = board.toString();
    assertEquals("Incorrect parse of input board", boardStr, rep);
  }

  @Test
  public void testBoardConstructorEmptyPosition() {
    String boardStr = "-  -  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.WHITE);

    String rep = board.toString();
    assertEquals("Incorrect parse of input board", boardStr, rep);
  }

  @Test
  public void testBoardConstructorRandomPosition1() {
    String boardStr = "-  -  -  -  -  k  -  -" + "\n" +
                      "-  -  -  -  -  Q  -  -" + "\n" + 
                      "-  -  -  -  -  K  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.BLACK);

    String rep = board.toString();
    assertEquals("Incorrect parse of input board", boardStr, rep);
  }

  @Test
  public void testLegalMovesCheckmate1() {
    String boardStr = "-  -  -  -  -  k  -  -" + "\n" +
                      "-  -  -  -  -  Q  -  -" + "\n" + 
                      "-  -  -  -  -  K  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.BLACK);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Expected 0 legal moves in checkmate", 0, legalMoves.size());
  }

  @Test
  public void testLegalMovesCheckmate2() {
    String boardStr = "-  k  -  -  -  -  -  r" + "\n" +
                      "-  b  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  q  K  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.WHITE);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Expected 0 legal moves in checkmate", 0, legalMoves.size());
  }

  @Test
  public void testLegalMovesCheckmate3() {
    String boardStr = "-  k  -  -  -  -  r  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  K" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.WHITE);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Expected 0 legal moves in checkmate", 0, legalMoves.size());
  }

  @Test
  public void testLegalMovesCheckmate4() {
    String boardStr = "Q  -  k  -  -  -  -  -" + "\n" +
                      "-  -  p  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  R  -  -  -  K";

    Board board = new Board(boardStr, Color.BLACK);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Expected 0 legal moves in checkmate", 0, legalMoves.size());
  }

  @Test
  public void testLegalMoves1MoveOutOfCheckmate() {
    String boardStr = "Q  -  k  -  -  -  -  -" + "\n" +
                      "-  -  P  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  R  -  -  -  K";

    Board board = new Board(boardStr, Color.BLACK);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Expected 1 legal moves to avoid checkmate", 1, legalMoves.size());
  }

  @Test
  public void testLegalMovesInitialPosition() {
    Board board = new Board();

    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Expected 20 initial moves for white", 20, legalMoves.size());

    // toggle the turn
    board.toggleTurn();
    legalMoves = board.legalMoves();

    assertEquals("Expected 20 initial moves for black", 20, legalMoves.size());
  }

  @Test
  public void testLegalMoves0Moves() {
    String boardStr = "-  -  -  -  -  k  -  -" + "\n" +
                      "-  -  -  -  -  Q  -  -" + "\n" + 
                      "-  -  -  -  -  K  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.BLACK);

    assertEquals("0 legal moves in checkmate", 0, board.legalMoves().size());
  }

  @Test
  public void testLegalMoves1Moves() {
    String boardStr = "-  -  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  K  -  -" + "\n" + 
                      "-  -  -  -  -  -  Q  -" + "\n" + 
                      "-  -  -  -  -  -  -  k" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.BLACK);
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("1 legal move in position setup", 1, legalMoves.size());

    Move onlyLegalMove = new Move(PieceType.KING, "h4", "h3");
    assertTrue("Kh3 is only legal move", legalMoves.contains(onlyLegalMove));
  }

  @Test
  public void testLegalMovesMoreThan1LegalMoves() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  q  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.WHITE);
    Set<Move> legalMoves = board.legalMoves();

    Move move1 = new Move(PieceType.KING, "e4", "e3");
    Move move2 = new Move(PieceType.KING, "e4", "f4");
    Move move3 = new Move(PieceType.KING, "e4", "d5", true);
    Set<Move> expectedLegalMoves = new HashSet<>(Arrays.asList(move1, move2, move3));

    assertEquals("Only king moves with a capture on d5 are the set of legal moves", expectedLegalMoves, legalMoves);
  }

  @Test
  public void testLegalMovesOutOfCheck1Move() {
    String boardStr = "-  -  -  -  r  r  -  k" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  b  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    Board board = new Board(boardStr, Color.WHITE);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Only 1 king move possible", 1, legalMoves.size());

    Move move = new Move(PieceType.KING, "e1", "d2");
    assertTrue("Only Kd2 is possible", legalMoves.contains(move));
  }

  @Test
  public void testLegalMovesOutOfCheckBlock() {
    String boardStr = "-  -  -  r  r  r  -  k" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  R  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    Board board = new Board(boardStr, Color.WHITE);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Only 1 rook block possible", 1, legalMoves.size());

    Move move = new Move(PieceType.ROOK, "c2", "e2");
    assertTrue("Only Re2 is possible", legalMoves.contains(move));
  }

  @Test
  public void testLegalMovesOutOfCheckCapture() {
    String boardStr = "-  -  -  -  -  -  -  k" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  R  Q" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  q  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  K";

    Board board = new Board(boardStr, Color.BLACK);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Only 1 move", 1, legalMoves.size());

    Move move = new Move(PieceType.QUEEN, "f4", "h6", true);
    assertTrue("Only Qxh6 is possible", legalMoves.contains(move));
  }

  @Test
  public void testLegalMovesOutOfCheckPawnBlock() {
    String boardStr = "-  -  -  -  -  R  -  -" + "\n" +
                      "-  -  -  -  -  -  p  k" + "\n" + 
                      "-  -  -  -  -  R  -  -" + "\n" + 
                      "-  -  -  -  -  B  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  K";

    Board board = new Board(boardStr, Color.BLACK);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Only 1 move", 1, legalMoves.size());

    Move move = new Move(PieceType.PAWN, "g7", "g6");
    assertTrue("Only g6 is possible", legalMoves.contains(move));
  }

  @Test
  public void testLegalMovesCantMovePinnedPiece() {
    String boardStr = "-  -  R  -  q  k  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  Q  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  K";

    Board board = new Board(boardStr, Color.BLACK);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("Only 1 move", 1, legalMoves.size());

    Move move = new Move(PieceType.KING, "f8", "g8");
    assertTrue("Only Kg8 is possible", legalMoves.contains(move));
  }

  @Test
  public void testLegalMovesCheckmate() {
    String boardStr = "k  r  -  -  -  -  -  -" + "\n" +
                      "p  p  N  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  K";

    Board board = new Board(boardStr, Color.BLACK);
    Set<Move> legalMoves = board.legalMoves();
    assertEquals("No legal moves", 0, legalMoves.size());
  }

  @Test
  public void testLegalMovesCastlingWhite() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  R";

    Board board = new Board(boardStr, Color.WHITE);
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 15 legal moves (5 king moves + castling + 9 rook moves) are allowed in position setup", 15, legalMoves.size());

    Move castleMove = new Move(PieceType.KING, "e1", "g1");
    assertTrue("castling Kingside should be valid move", legalMoves.contains(castleMove));
  }

  @Test
  public void testLegalMovesCastlingBlack() {
    String boardStr = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  R";

    Board board = new Board(boardStr, Color.BLACK);
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 26 legal moves (5 king moves + 2 castling + 19 rook moves) are allowed in position setup", 26, legalMoves.size());

    Move castleKingside = new Move(PieceType.KING, "e8", "g8");
    Move castleQueenside = new Move(PieceType.KING, "e8", "c8");
    assertTrue("castling Kingside should be valid move", legalMoves.contains(castleKingside));
    assertTrue("castling Queenside should be valid move", legalMoves.contains(castleQueenside));
  }

  @Test
  public void testLegalMovesEnPassentWhite() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  P  p  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "K  -  -  -  -  -  -  -";

    Move lastMove = new Move(PieceType.PAWN, "d7", "d5");
    List<Move> moveList = new ArrayList<>(Arrays.asList(lastMove));
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 5 legal moves (3 king moves + pawn forward + en passent) are allowed in position setup", 5, legalMoves.size());

    Move kingMove1 = new Move(PieceType.KING, "a1", "a2");
    Move kingMove2 = new Move(PieceType.KING, "a1", "b2");
    Move kingMove3 = new Move(PieceType.KING, "a1", "b1");
    Move pawnForward = new Move(PieceType.PAWN, "c5", "c6");
    Move enPassent = new Move(PieceType.PAWN, "c5", "d6", true);

    Set<Move> expectedMoves = new HashSet<Move>(Arrays.asList(kingMove1, kingMove2, kingMove3, pawnForward, enPassent));
    assertEquals("Expected 3 king moves + 2 pawn moves including en passent", expectedMoves, legalMoves);
  }

  @Test
  public void testLegalMovesEnPassentBlack() {
    String boardStr = "k  -  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  P  p" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "K  -  -  -  -  -  -  -";

    Move lastMove = new Move(PieceType.PAWN, "g2", "g4");
    List<Move> moveList = new ArrayList<>(Arrays.asList(lastMove));
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedList);
 
    Set<Move> legalMoves = board.legalMoves();
    
    assertEquals("Only 5 legal moves (3 king moves + pawn forward + en passent) are allowed in position setup", 5, legalMoves.size());

    Move kingMove1 = new Move(PieceType.KING, "a8", "a7");
    Move kingMove2 = new Move(PieceType.KING, "a8", "b8");
    Move kingMove3 = new Move(PieceType.KING, "a8", "b7");
    Move pawnForward = new Move(PieceType.PAWN, "h4", "h3");
    Move enPassent = new Move(PieceType.PAWN, "h4", "g3", true);

    Set<Move> expectedMoves = new HashSet<Move>(Arrays.asList(kingMove1, kingMove2, kingMove3, pawnForward, enPassent));
    assertEquals("Expected 3 king moves + 2 pawn moves including en passent", expectedMoves, legalMoves);
  }

  @Test
  public void testLegalMovesPromotionAndEnPassent() {
    String boardStr = "k  -  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  P  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  p  P" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "K  -  -  -  -  -  -  -";

    Move lastMove = new Move(PieceType.PAWN, "g7", "g5");
    List<Move> moveList = new ArrayList<>(Arrays.asList(lastMove));
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);
 
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 9 legal moves (3 king moves + 1 pawn forward + 1 en passent + 4 pawn promotions) are allowed in position setup", 9, legalMoves.size());

    Move enPassent = new Move(PieceType.PAWN, "h5", "g6", true);
    Move promote = new Move(PieceType.PAWN, "f7", "f8", false, PieceType.BISHOP);
    assertTrue("hxg6 enPassent expected to be move", legalMoves.contains(enPassent));
    assertTrue("promotion expected to be move", legalMoves.contains(promote));
  }

  @Test
  public void testLegalMovesPromotionCapture() {
    String boardStr = "k  -  -  -  r  N  R  r" + "\n" +
                      "-  -  -  -  -  -  P  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  K  -";

    Board board = new Board(boardStr, Color.WHITE);
 
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 12 legal moves (3 king moves + 4 pawn capture+ 4 knight moves + 1 Rook move) are allowed in position setup", 12, legalMoves.size());

    Move promote = new Move(PieceType.PAWN, "g7", "h8", true, PieceType.KNIGHT);
    assertTrue("Expected gxh8 promotion to be a move", legalMoves.contains(promote));
  }

  @Test
  public void testLegalMovesPromotionBlack() {
    String boardStr = "k  -  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  p  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  K  -";

    Board board = new Board(boardStr, Color.BLACK);
 
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 7 legal moves (3 king moves + 4 pawn promotions) are allowed in position setup", 7, legalMoves.size());

    Move promote = new Move(PieceType.PAWN, "b2", "b1", false, PieceType.QUEEN);
    assertTrue("Expected b1 promotion to be a move", legalMoves.contains(promote));
  }

  @Test
  public void testLegalMovesCastleOutOfCheck() {
    String boardStr = "-  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  B  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  K  -";

    Board board = new Board(boardStr, Color.BLACK);
 
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 4 legal moves (4 king moves) are allowed in position setup", 4, legalMoves.size());

    Move castle = new Move(PieceType.KING, "e8", "g8");

    assertFalse("Castling kingside shouldn't be a move", legalMoves.contains(castle));
  }

  @Test
  public void testLegalMovesCastleThroughCheck() {
    String boardStr = "-  -  -  r  k  -  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  -";

    Board board = new Board(boardStr, Color.WHITE);
 
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 13 legal moves (3 king moves + 10 rook moves) are allowed in position setup", 13, legalMoves.size());

    Move castle = new Move(PieceType.KING, "e1", "c1");

    assertFalse("Castling kingside shouldn't be a move", legalMoves.contains(castle));
  }

  @Test
  public void testLegalMovesEnPassentNotValid1() {
    Move lastMove = new Move(PieceType.KING, "d7", "e8");
    
    List<Move> moveList = new ArrayList<Move>(Arrays.asList(lastMove));
    List<Piece> capturedPieces = new ArrayList<Piece>();
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  p  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedPieces);
 
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 13 legal moves (5 king moves + 1 pawn move) are allowed in position setup", 6, legalMoves.size());

    Move enPassent = new Move(PieceType.PAWN, "a5", "a6", true);

    assertFalse("En passent should be illegal", legalMoves.contains(enPassent));
  }

  @Test
  public void testLegalMovesEnPassentNotValid2() {
    Move lastMove = new Move(PieceType.PAWN, "b6", "b5");
    
    List<Move> moveList = new ArrayList<Move>(Arrays.asList(lastMove));
    List<Piece> capturedPieces = new ArrayList<Piece>();
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  p  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedPieces);
 
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 6 legal moves (5 king moves + 1 pawn moves) are allowed in position setup", 6, legalMoves.size());

    Move enPassent = new Move(PieceType.PAWN, "a5", "b6", true);

    assertFalse("En passent should be illegal", legalMoves.contains(enPassent));
  }

  @Test
  public void testLegalMovesCastlePiecesInWay() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  N  R";

    Board board = new Board(boardStr, Color.WHITE);
 
    Set<Move> legalMoves = board.legalMoves();

    Move castle = new Move(PieceType.KING, "e1", "g1");

    assertFalse("Castling kingside shouldn't be a move", legalMoves.contains(castle));
  }

  @Test
  public void testLegalMovesCastlePiecesInWay2() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "R  b  -  -  K  -  -  -";

    Board board = new Board(boardStr, Color.WHITE);
 
    Set<Move> legalMoves = board.legalMoves();

    Move castle = new Move(PieceType.KING, "e1", "c1");

    assertFalse("Castling queenside shouldn't be a move", legalMoves.contains(castle));
  }

  @Test
  public void testLegalMovesCastlePiecesInWay3() {
    String boardStr = "-  -  -  -  k  b  n  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    Board board = new Board(boardStr, Color.BLACK);
 
    Set<Move> legalMoves = board.legalMoves();

    Move castle = new Move(PieceType.KING, "e8", "c8");

    assertFalse("Castling kingside shouldn't be a move", legalMoves.contains(castle));
  }

  @Test
  public void testLegalMovesCastleIntoCheck() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  b" + "\n" + 
                      "-  -  -  -  K  -  -  R";

    Board board = new Board(boardStr, Color.WHITE);
 
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 8 legal moves (5 king moves + 3 rook moves) are allowed in position setup", 8, legalMoves.size());

    Move castle = new Move(PieceType.KING, "e1", "g1");

    assertFalse("Castling kingside shouldn't be a move", legalMoves.contains(castle));
  }

  @Test
  public void testLegalMovesCantCastleWhite() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  B  N  R";

    Board board = new Board(boardStr, Color.WHITE);
 
    Set<Move> legalMoves = board.legalMoves();

    Move castle = new Move(PieceType.KING, "e1", "g1");

    assertFalse("Castling kingside shouldn't be a move", legalMoves.contains(castle));
  }

  @Test
  public void testLegalMovesCantCastleBlack() {
    String boardStr = "r  q  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    Board board = new Board(boardStr, Color.BLACK);
 
    Set<Move> legalMoves = board.legalMoves();

    Move castle = new Move(PieceType.KING, "e8", "c8");

    assertFalse("Castling queenside shouldn't be a move", legalMoves.contains(castle));
  }

  @Test
  public void testUndoLastMovePawn() {
    Board board = new Board();
    String rep1 = board.compressBoard();

    Move move1 = new Move(PieceType.PAWN, "a2", "a4");
    board.move(move1);
    String rep2 = board.compressBoard();

    assertFalse("Board shouldn't be the same after first move", rep1.equals(rep2));

    board.undoLastMove();
    String rep3 = board.compressBoard();


    assertTrue("Board should be the same after first move was undoed", rep1.equals(rep3));
  }

  @Test
  public void testUndoLastMovePiece() {
    Board board = new Board();

    Move move1 = new Move(PieceType.PAWN, "a2", "a4");
    board.move(move1);
    String rep1 = board.compressBoard();

    Move move2 = new Move(PieceType.KNIGHT, "b8", "c6");

    board.move(move2);
    board.undoLastMove();

    String rep2 = board.compressBoard();

    assertTrue("Board should be the same after piece move was undoed", rep1.equals(rep2));
  }

  @Test
  public void testUndoLastMoveMovedRook() {
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);
    String rep1 = board.compressBoard();

    Move rookMove = new Move(PieceType.ROOK, "a1", "a4");

    board.move(rookMove);
    board.undoLastMove();

    String rep2 = board.compressBoard();
 
    assertEquals("Expected same position after undoing rook move", rep1, rep2);
  }

  @Test
  public void testUndoLastMoveMovedRookToAlmostSamePosition() {
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);
    String rep1 = board.compressBoard();

    Move rookMove1 = new Move(PieceType.ROOK, "a1", "a4");
    Move rookMove2 = new Move(PieceType.ROOK, "a4", "a1");

    Move kingMove1 = new Move(PieceType.KING, "b8", "c8");
    Move kingMove2 = new Move(PieceType.KING, "c8", "b8");

    // make 8 moves going back and forth
    board.move(rookMove1);
    board.move(kingMove1);
    board.move(rookMove2);
    board.move(kingMove2);
    board.move(rookMove1);
    board.move(kingMove1);
    board.move(rookMove2);
    board.move(kingMove2);

    // undo last 8 moves
    board.undoLastMove();
    board.undoLastMove();
    board.undoLastMove();
    board.undoLastMove();
    board.undoLastMove();
    board.undoLastMove();
    board.undoLastMove();
    board.undoLastMove();

    String rep2 = board.compressBoard();
 
    assertEquals("Expected same position after undoing all moves", rep1, rep2);
  }

  @Test
  public void testUndoLastMoveMovedRookMultipleTimes() {
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);
    String rep1 = board.compressBoard();

    Move rookMove1 = new Move(PieceType.ROOK, "a1", "a4");
    Move rookMove2 = new Move(PieceType.ROOK, "a4", "a1");

    Move kingMove1 = new Move(PieceType.KING, "b8", "c8");
    Move kingMove2 = new Move(PieceType.KING, "c8", "b8");

    // make 8 moves going back and forth
    board.move(rookMove1);
    board.move(kingMove1);
    board.move(rookMove2);
    board.move(kingMove2);
    board.move(rookMove1);
    board.move(kingMove1);
    board.move(rookMove2);

    // only undo last 4 moves
    board.undoLastMove();
    board.undoLastMove();
    board.undoLastMove();
    board.undoLastMove();

    String rep2 = board.compressBoard();
 
    assertFalse("Expected different position after only undoing 4 moves", rep1.equals(rep2));
  }


  @Test
  public void testUndoLastMoveMovedKing() {
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);
    String rep1 = board.compressBoard();

    Move kingMove = new Move(PieceType.KING, "e1", "e2");

    board.move(kingMove);
    board.undoLastMove();

    String rep2 = board.compressBoard();
 
    assertEquals("Expected same position after undoing king move", rep1, rep2);
  }

  @Test
  public void testUndoLastMoveCastlingWhite() {
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);
    String rep1 = board.compressBoard();

    Move castling = new Move(PieceType.KING, "e1", "c1");

    board.move(castling);
    board.undoLastMove();

    String rep2 = board.compressBoard();
 
    assertEquals("Expected same position after undoing castling", rep1, rep2);
  }

  @Test
  public void testUndoLastMovePromote() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "P  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  K";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);
    String rep1 = board.compressBoard();

    Move promote = new Move(PieceType.PAWN, "a7", "a8", false, PieceType.ROOK);

    board.move(promote);
    board.undoLastMove();

    String rep2 = board.compressBoard();
 
    assertEquals("Expected same position after undoing promotion", rep1, rep2);
  }

  @Test
  public void testUndoLastMovePromoteCapture() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  p  -  -  -  -  -" + "\n" + 
                      "-  R  -  -  -  -  -  K";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedList);
    String rep1 = board.compressBoard();

    Move promote = new Move(PieceType.PAWN, "c2", "b1", true, PieceType.BISHOP);

    board.move(promote);
    board.undoLastMove();

    String rep2 = board.compressBoard();
 
    assertEquals("Expected same position after undoing promotion", rep1, rep2);
  }

  @Test
  public void testUndoLastMoveCastlingBlack() {
    String boardStr = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);
    String rep1 = board.compressBoard();

    Move castling = new Move(PieceType.KING, "e1", "c1");

    board.move(castling);
    board.undoLastMove();

    String rep2 = board.compressBoard();
 
    assertEquals("Expected same position after undoing castling", rep1, rep2);
  }

  @Test
  public void testMovePawn1Step() {
    String boardStr = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    String expected = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pawnMove = new Move(PieceType.PAWN, "a2", "a3");

    board.move(pawnMove);
 
    assertEquals("Expected position after pawn move", expected, board.toString());
  }

  @Test
  public void testMovePawn2Steps() {
    String boardStr = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  p  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    String expected = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  p  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedList);

    Move pawnMove = new Move(PieceType.PAWN, "f7", "f5");

    board.move(pawnMove);
 
    assertEquals("Expected position after pawn move", expected, board.toString());
  }

  @Test
  public void testMovePieceQueen() {
    String boardStr = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  q  -  -  p  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    String expected = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  p  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  -  -  -  -  -  -  q" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedList);

    Move queenMove = new Move(PieceType.QUEEN, "c7", "h2");

    board.move(queenMove);
 
    assertEquals("Expected position after Qh2", expected, board.toString());
  }

  @Test
  public void testMovePieceKnight() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  N  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    String expected = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  N  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pieceMove = new Move(PieceType.KNIGHT, "b3", "c5");

    board.move(pieceMove);
 
    assertEquals("Expected position after Nc5", expected, board.toString());
  }

  @Test
  public void testMovePieceRook() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  q  -  -  -  R" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    String expected = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  R  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";


    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pieceMove = new Move(PieceType.ROOK, "h2", "d2", true);

    board.move(pieceMove);
 
    assertEquals("Expected position after Rxd2", expected, board.toString());
  }

  @Test
  public void testMovePieceCapture() {
    String boardStr = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  p  -  -" + "\n" + 
                      "-  -  b  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  R";

    String expected = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  p  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  -  -  -  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  b";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedList);

    Move pieceMove = new Move(PieceType.BISHOP, "c6", "h1", true);

    board.move(pieceMove);
 
    assertEquals("Expected position after Bxh1", expected, board.toString());
  }

  @Test
  public void testMoveEnPassent() {
    String boardStr = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  P  p  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  Q  K  -  -  R";

    String expected = "r  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  P  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  Q  K  -  -  R";


    Move lastMove = new Move(PieceType.PAWN, "d7", "d5");
    List<Move> moveList = new ArrayList<>(Arrays.asList(lastMove));
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move enPassent = new Move(PieceType.PAWN, "c5", "d6", true);

    board.move(enPassent);
 
    assertEquals("Expected enpassent to work", expected, board.toString());
  }

  @Test
  public void testMovePromotion1() {
    String boardStr = "r  -  -  -  k  q  -  r" + "\n" +
                      "-  -  -  -  -  -  P  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  K  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    String expected = "r  -  -  -  k  q  Q  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  K  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pieceMove = new Move(PieceType.PAWN, "g7", "g8", false, PieceType.QUEEN);

    board.move(pieceMove);
 
    assertEquals("Expected promotion to queen to work", expected, board.toString());
  }

  @Test
  public void testMovePromotionCapture() {
    String boardStr = "r  -  -  -  k  q  -  r" + "\n" +
                      "-  -  -  -  -  -  P  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  K  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    String expected = "r  -  -  -  k  R  -  r" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  K  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pieceMove = new Move(PieceType.PAWN, "g7", "f8", true, PieceType.ROOK);

    board.move(pieceMove);
 
    assertEquals("Expected promotion to rook to work", expected, board.toString());
  }

  @Test
  public void testMovePromotionCaptureBlack() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  K  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  p  -" + "\n" + 
                      "-  -  -  -  -  -  -  N";

    String expected = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  K  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  b";


    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedList);

    Move pieceMove = new Move(PieceType.PAWN, "g2", "h1", true, PieceType.BISHOP);

    board.move(pieceMove);
 
    assertEquals("Expected promotion capture to work", expected, board.toString());
  }

  @Test
  public void testMoveCastlingKingside() {
    String boardStr = "-  -  -  -  k  -  -  r" + "\n" +
                      "-  -  -  -  -  p  p  p" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    String expected = "-  -  -  -  -  r  k  -" + "\n" +
                      "-  -  -  -  -  p  p  p" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";


    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedList);

    Move pieceMove = new Move(PieceType.KING, "e8", "g8");

    board.move(pieceMove);
 
    assertEquals("Expected castling to work", expected, board.toString());
  }

  @Test
  public void testMoveCastlingQueenside() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  P  P  P  -  -  -  -" + "\n" + 
                      "R  -  -  -  K  -  -  -";

    String expected = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  P  P  P  -  -  -  -" + "\n" + 
                      "-  -  K  R  -  -  -  -";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pieceMove = new Move(PieceType.KING, "e1", "c1");

    board.move(pieceMove);
 
    assertEquals("Expected castling to work", expected, board.toString());
  }

  @Test
  public void testMoveInvalidMovePawnMovesTwoSteps() {
    Board board = new Board();

    Move pawnMove1 = new Move(PieceType.PAWN, "c2", "c3");
    Move blackMove = new Move(PieceType.PAWN, "a7", "a6");
    Move pawnMove2 = new Move(PieceType.PAWN, "c3", "c5");

    board.move(pawnMove1);
    board.move(blackMove);

    String rep1 = board.compressBoard();

    board.move(pawnMove2);

    String rep2 = board.compressBoard();
 
    assertEquals("Pawn can't move two steps after it moved already", rep1, rep2);
  }

  @Test
  public void testMoveInvalidMovePieceJumps() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  R  -  -  -  -" + "\n" + 
                      "-  -  B  -  -  -  -  -" + "\n" + 
                      "-  -  K  -  -  -  -  -";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pieceMove = new Move(PieceType.BISHOP, "c2", "e4");

    board.move(pieceMove);
 
    assertEquals("Bishop can't jump over pieces", boardStr, board.toString());
  }

  @Test
  public void testMoveInvalidMovePawnJump() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  p  -  -  -  -  -" + "\n" + 
                      "-  -  B  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  K  -  -  -  -  -";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedList);

    Move pieceMove = new Move(PieceType.PAWN, "c7", "c5");

    board.move(pieceMove);
 
    assertEquals("Pawn can't jump over pieces", boardStr, board.toString());
  }

  @Test
  public void testMoveInvalidMoveCantCaptureOwn() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  B  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  R  -  -  -  -  -" + "\n" + 
                      "-  -  K  -  -  -  -  -";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pieceMove1 = new Move(PieceType.ROOK, "c2", "c4");
    Move pieceMove2 = new Move(PieceType.ROOK, "c2", "c4", true);

    board.move(pieceMove1);
    assertEquals("Rook can't move on same color piece", boardStr, board.toString());

    board.move(pieceMove2);
    assertEquals("Rook can't capture same color piece", boardStr, board.toString());
  }

  @Test
  public void testMoveInvalidMoveCantCaptureBeyond() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  q  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  P  -  -  -" + "\n" + 
                      "-  -  -  B  -  -  -  -" + "\n" + 
                      "-  -  K  -  -  -  -  -";

    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pieceMove = new Move(PieceType.BISHOP, "d2", "g5", true);

    board.move(pieceMove);
    assertEquals("Can't capture beyond same color piece", boardStr, board.toString());
  }

  @Test
  public void testMoveDoubleCheck() {
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  N  -  -  -" + "\n" + 
                      "-  -  -  -  Q  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  K  -  -  -  -  -";

    String expected = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  N  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  Q  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  K  -  -  -  -  -";


    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.WHITE, moveList, capturedList);

    Move pieceMove = new Move(PieceType.KNIGHT, "e6", "g7");

    board.move(pieceMove);
    assertEquals("Expected double check to be legal", expected, board.toString());
  }

  @Test
  public void testMoveCounterCheck() {
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  r  -  p  -  -  -  K" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  Q  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    String expected = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  r  -  -  -  -  -  K" + "\n" + 
                      "-  -  -  p  -  -  -  -" + "\n" + 
                      "-  -  -  -  Q  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";


    List<Move> moveList = new ArrayList<>();
    List<Piece> capturedList = new ArrayList<>();

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedList);

    Move pieceMove = new Move(PieceType.PAWN, "d7", "d6");

    board.move(pieceMove);
    assertEquals("Expected counter-check to be legal", expected, board.toString());
  }

  @Test
  public void testGetTurnInit() {
    Board board = new Board();
    assertEquals("Expected initial turn to be white", Color.WHITE, board.getTurn());
  }

  @Test
  public void testGetTurnConstructorWhite() {
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  r  -  p  -  -  -  K" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  Q  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.WHITE, new ArrayList<Move>(), new ArrayList<Piece>());
    assertEquals("Expected turn to be white given input", Color.WHITE, board.getTurn());
  }

  @Test
  public void testGetTurnConstructorBlack() {
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  r  -  p  -  -  -  K" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  Q  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.BLACK, new ArrayList<Move>(), new ArrayList<Piece>());
    assertEquals("Expected turn to be black given input", Color.BLACK, board.getTurn());
  }

  @Test
  public void testGetTurn1Move() {
    Board board = new Board();

    Move move1 = new Move(PieceType.PAWN, "b2","b4");

    board.move(move1);

    assertEquals("Expected turn to be black after first move", Color.BLACK, board.getTurn());
  }

  @Test
  public void testGetTurn2Moves() {
    Board board = new Board();

    Move move1 = new Move(PieceType.PAWN, "b2","b4");
    Move move2 = new Move(PieceType.PAWN, "a7","a5");

    board.move(move1);
    board.move(move2);

    assertEquals("Expected turn to be white after two moves", Color.WHITE, board.getTurn());
  }

  @Test
  public void testGetTurn2MovesWithUndo() {
    Board board = new Board();

    Move move1 = new Move(PieceType.PAWN, "b2","b4");
    Move move2 = new Move(PieceType.PAWN, "a7","a5");

    board.move(move1);
    board.move(move2);
    board.undoLastMove();

    assertEquals("Expected undo-ing last move to toggle turn", Color.BLACK, board.getTurn());
  }

  @Test
  public void testGetTurnMultipleMoves() {
    Board board = new Board();

    Move move1 = new Move(PieceType.PAWN, "b2","b4");
    Move move2 = new Move(PieceType.PAWN, "a7","a5");
    Move move3 = new Move(PieceType.KNIGHT, "b1","c3");
    Move move4 = new Move(PieceType.KNIGHT, "g8","h6");
    Move move5 = new Move(PieceType.KNIGHT, "g1","f3");

    board.move(move1);
    board.move(move2);
    board.move(move3);
    board.move(move4);
    board.move(move5);

    assertEquals("Expected turn to be black after five moves", Color.BLACK, board.getTurn());
  }

  @Test
  public void testGetMoveList0Moves() {
    Board board = new Board();
    List<Move> moveList = board.getMoveList();
    assertEquals("Expected nothing on initial movelist", 0, moveList.size());
  }

  @Test
  public void testGetMoveListConstructorEmpty() {
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  r  -  p  -  -  -  K" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  Q  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.BLACK, new ArrayList<Move>(), new ArrayList<Piece>());
    List<Move> moveList = board.getMoveList();

    assertEquals("Expected nothing since initial movelist was empty", 0, moveList.size());
  }

  @Test
  public void testGetMoveListConstructorNonEmpty() {
    Move previousMove1 = new Move(PieceType.KING, "a8", "b8");
    Move previousMove2 = new Move(PieceType.QUEEN, "e1", "e5");
    List<Move> previousMoveList = new ArrayList<Move>(Arrays.asList(previousMove1, previousMove2));
    String boardStr = "-  k  -  -  -  -  -  -" + "\n" +
                      "-  r  -  p  -  -  -  K" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  Q  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -";

    Board board = new Board(boardStr, Color.BLACK, previousMoveList, new ArrayList<Piece>());
    List<Move> moveList = board.getMoveList();
    assertEquals("Expected input movelist", previousMoveList, moveList);
  }

  @Test
  public void testGetMoveList1Move() {
    Board board = new Board();

    Move move1 = new Move(PieceType.PAWN, "e2", "e4");

    board.move(move1);

    List<Move> moveList = board.getMoveList();
    List<Move> expectedList = new ArrayList<>(Arrays.asList(move1));
    
    assertEquals("Expected 4 input moves to be in movelist", expectedList, moveList);
  }

  @Test
  public void testGetMoveListMultipleMoves() {
    Board board = new Board();

    Move move1 = new Move(PieceType.PAWN, "e2", "e4");
    Move move2 = new Move(PieceType.PAWN, "e7", "e5");
    Move move3 = new Move(PieceType.KNIGHT, "g1", "f3");
    Move move4 = new Move(PieceType.KNIGHT, "b8", "c6");

    board.move(move1);
    board.move(move2);
    board.move(move3);
    board.move(move4);

    List<Move> moveList = board.getMoveList();
    List<Move> expectedList = new ArrayList<>(Arrays.asList(move1, move2, move3, move4));
    
    assertEquals("Expected 4 input moves to be in movelist", expectedList, moveList);
  }

  @Test
  public void testGetLastMoveAfterUndo() {
    Board board = new Board();

    Move move1 = new Move(PieceType.PAWN, "e2", "e4");
    Move move2 = new Move(PieceType.PAWN, "e7", "e5");
    Move move3 = new Move(PieceType.KNIGHT, "g1", "f3");
    Move move4 = new Move(PieceType.KNIGHT, "b8", "c6");

    board.move(move1);
    board.move(move2);
    board.move(move3);
    board.move(move4);

    board.undoLastMove();
    board.undoLastMove();
    
    assertEquals("Expected move 2 to be last moved played after 2 undoes", move2, board.getLastMove());
  }

  @Test
  public void testGetLastMoveInit() {
    Board board = new Board();

    assertEquals("Expected no 'last move' on init", null, board.getLastMove());
  }

  ////////////////////////////////////////////////////
  //              PERFORMANCE TESTS                 //
  ////////////////////////////////////////////////////

  @Ignore("Performance test")
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

  @Ignore("Performance test")
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

  @Ignore("Performance test")
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

  @Ignore("Performance test")
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
}
