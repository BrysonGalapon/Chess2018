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
    assertFalse("Different positions not compressing to different value", !rep1.equals(rep2));
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

    assertEquals("1 legal move in position setup", 0, legalMoves.size());

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

    Move move1 = new Move(PieceType.KING, "e4", "e5");
    Move move2 = new Move(PieceType.KING, "e4", "f5");
    Move move3 = new Move(PieceType.KING, "e4", "f4");
    Move move4 = new Move(PieceType.KING, "e4", "f3");
    Move move5 = new Move(PieceType.KING, "e4", "e3");
    Move move6 = new Move(PieceType.KING, "e4", "d3");
    Move move7 = new Move(PieceType.KING, "e4", "d4");
    Move move8 = new Move(PieceType.KING, "e4", "d5", true);
    Set<Move> expectedLegalMoves = new HashSet<>(Arrays.asList(move1, move2, move3, move4, move5, move6, move7, move8));

    assertEquals("Only king moves with a capture on d5 are the set of legal moves", expectedLegalMoves, legalMoves);
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

    assertEquals("Only 6 legal moves (5 king moves + castling) are allowed in position setup", 6, legalMoves.size());

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

    assertEquals("Only 7 legal moves (5 king moves + castling) are allowed in position setup", 7, legalMoves.size());

    Move castleKingside = new Move(PieceType.KING, "e8", "g8");
    Move castleQueenside = new Move(PieceType.KING, "e1", "c8");
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
    Move enPassent = new Move(PieceType.PAWN, "c5", "d6");

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
    Move enPassent = new Move(PieceType.PAWN, "h4", "g3");

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

    Move enPassent = new Move(PieceType.PAWN, "h5", "g6");
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

    assertEquals("Only 13 legal moves (5 king moves + 4 pawn capture+ 4 knight moves) are allowed in position setup", 13, legalMoves.size());

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

    assertEquals("Only 13 legal moves (4 king moves + 9 rook moves) are allowed in position setup", 13, legalMoves.size());

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
  public void testLegalMovesEnPassentNotValid() {
    Move lastMove = new Move(PieceType.KING, "d2", "e1");
    
    List<Move> moveList = new ArrayList<Move>(Arrays.asList());
    List<Piece> capturedPieces = new ArrayList<Piece>();
    String boardStr = "-  -  -  -  k  -  -  -" + "\n" +
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "P  p  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  -  -  -  -" + "\n" + 
                      "-  -  -  -  K  -  -  -";

    Board board = new Board(boardStr, Color.BLACK, moveList, capturedPieces);
 
    Set<Move> legalMoves = board.legalMoves();

    assertEquals("Only 13 legal moves (3 king moves + 10 rook moves) are allowed in position setup", 13, legalMoves.size());

    Move enPassent = new Move(PieceType.PAWN, "a5", "a6");

    assertFalse("En passent should be illegal", legalMoves.contains(enPassent));
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

    assertFalse("Castling kingside shouldn't be a move", !legalMoves.contains(castle));
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
  public void testUndoLastMoveCastlingWhite() {
    String boardStr = "k  -  -  -  -  -  -  -" + "\n" +
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


  ////////////////////////////////////////////////////
  //              PERFORMANCE TESTS                 //
  ////////////////////////////////////////////////////

  //@Test
  //public void testLegalMovesTiming90Percent() {
  //  Board board = new Board();

  //  long start_first_call = System.currentTimeMillis();
  //  board.legalMoves();
  //  long end_first_call = System.currentTimeMillis();

  //  long start_second_call = System.currentTimeMillis();
  //  board.legalMoves();
  //  long end_second_call = System.currentTimeMillis();

  //  long first_call_length = end_first_call-start_first_call;
  //  long second_call_length = end_second_call-start_second_call;
  //  assertTrue(String.format("second call (%d ms) isn't faster than first call (%d ms) by 90 percent", second_call_length, first_call_length), second_call_length < 0.90*first_call_length);
  //}

  //@Test
  //public void testLegalMovesTiming50Percent() {
  //  Board board = new Board();

  //  long start_first_call = System.currentTimeMillis();
  //  board.legalMoves();
  //  long end_first_call = System.currentTimeMillis();

  //  long start_second_call = System.currentTimeMillis();
  //  board.legalMoves();
  //  long end_second_call = System.currentTimeMillis();

  //  long first_call_length = end_first_call-start_first_call;
  //  long second_call_length = end_second_call-start_second_call;
  //  assertTrue(String.format("second call (%d ms) isn't faster than first call (%d ms) by 50 percent", second_call_length, first_call_length), second_call_length < 0.50*first_call_length);
  //}

  //@Test
  //public void testLegalMovesTiming10Percent() {
  //  Board board = new Board();

  //  long start_first_call = System.currentTimeMillis();
  //  board.legalMoves();
  //  long end_first_call = System.currentTimeMillis();

  //  long start_second_call = System.currentTimeMillis();
  //  board.legalMoves();
  //  long end_second_call = System.currentTimeMillis();

  //  long first_call_length = end_first_call-start_first_call;
  //  long second_call_length = end_second_call-start_second_call;
  //  assertTrue(String.format("second call (%d ms) isn't faster than first call (%d ms) by 10 percent", second_call_length, first_call_length), second_call_length < 0.10*first_call_length);
  //}

  //@Test
  //public void testLegalMovesTiming05Percent() {
  //  Board board = new Board();

  //  long start_first_call = System.currentTimeMillis();
  //  board.legalMoves();
  //  long end_first_call = System.currentTimeMillis();

  //  long start_second_call = System.currentTimeMillis();
  //  board.legalMoves();
  //  long end_second_call = System.currentTimeMillis();

  //  long first_call_length = end_first_call-start_first_call;
  //  long second_call_length = end_second_call-start_second_call;
  //  assertTrue(String.format("second call (%d ms) isn't faster than first call (%d ms) by 5 percent", second_call_length, first_call_length), second_call_length < 0.05*first_call_length);
  //}

}
