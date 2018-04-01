package src.datatypes;

// imports
import java.util.*;

/**
 * Represents a mutable board on which pieces may be placed and
 *  moved
 */
public class Board {
  // list of all the moves played
  private final List<Move> moveList;
  // board containing chess pieces
  private final Piece[][] board;

  // number of columns on the chess board
  private final int NUM_COLS=8;
  // number of rows on the chess board
  private final int NUM_ROWS=8;

  /*
   * Create a new Board
   */
  public Board() {
    // create moves list
    moveList = new LinkedList<Move>();
    // create board
    board = new Piece[NUM_ROWS][NUM_COLS];

    // populate pawns
    for (int col=0; col<NUM_COLS; col++) {
      // populate white pawns on 2nd row
      board[1][col] = Piece.pawn(Color.WHITE);
      // populate black pawns on 7th row
      board[6][col] = Piece.pawn(Color.BLACK);
    }

    // populate rooks
    board[0][0] = Piece.rook(Color.WHITE); // a1 rook
    board[0][7] = Piece.rook(Color.WHITE); // h1 rook
    board[7][0] = Piece.rook(Color.BLACK); // a8 rook
    board[7][7] = Piece.rook(Color.BLACK); // h8 rook

    // populate knights
    board[0][1] = Piece.knight(Color.WHITE); // b1 knight
    board[0][6] = Piece.knight(Color.WHITE); // g1 knight
    board[7][1] = Piece.knight(Color.BLACK); // b8 knight
    board[7][6] = Piece.knight(Color.BLACK); // g8 knight

    // populate bishops
    board[0][2] = Piece.bishop(Color.WHITE); // c1 bishop
    board[0][5] = Piece.bishop(Color.WHITE); // f1 bishop
    board[7][2] = Piece.bishop(Color.BLACK); // c8 bishop
    board[7][5] = Piece.bishop(Color.BLACK); // f8 bishop

    // populate queens
    board[0][3] = Piece.queen(Color.WHITE); // d1 queen
    board[7][3] = Piece.queen(Color.BLACK); // d8 queen

    // populate kings
    board[0][4] = Piece.king(Color.WHITE); // e1 king
    board[7][4] = Piece.king(Color.BLACK); // e8 king
  }

  /**
   * Removes piece from square and returns it if it exists
   * @param row row of square to remove piece from
   * @param col column of square to remove piece from
   * @return the piece on that square, if it exists.
   *         if the piece doesn't exist, return null
   */
  public Piece clearSquare(int row, int col) {
    Piece piece = board[row][col];
    if (piece == null) {return null;}

    board[row][col] = null;
    return piece;
  }

  /**
   * Obtain the piece at a given square, without removing it
   * @param row row of square to get piece from
   * @param col column of square to get piece from
   * @return the piece on that square, if it exists.
   *         if the piece doesn't exist, return null
   */
  public Piece getPiece(int row, int col) {
    Piece piece = board[row][col];
    if (piece == null) {return null;}
    return piece.copy();
  }

  /**
   * Check if a square contains a piece on it
   * @param row 0-indexed row of square query
   * @param col 0-indexed column of square query
   */
  public boolean containsPiece(int row, int col) {
    return board[row][col] != null;
  }

  /**
   * Make a move on this board - does nothing if move is illegal
   */
  public void move(Move move) {
    // TODO
    this.moveList.add(move);
  }

  /**
   * Obtain a list of all moves played
   * @return a list of all the moves played on this board
   */
  public List<Move> getMoveList() {
    return new ArrayList<Move>(this.moveList);
  }

  /**
   * Obtain the last move played on this board
   *  - returns null if no moves have been played
   */
  public Move getLastMove() {
    int movelist_length = this.moveList.size();
    if (movelist_length == 0) {return null;}
    return this.moveList.get(movelist_length-1);
  }

  /**
   * Get string representation of the pieces on this board
   * @return string representation of the pieces on this board, where
   *          Pawn = P or p
   *          Rook = R or r
   *          Knight = N or n
   *          Bishop = B or b
   *          Queen = Q or q
   *          King = K or k
   *
   *          - white pieces are capital letters
   *          - black pieces are lowercase letters
   *          - squares with no pieces contain a "-"
   *          - squares are horizontally separated by 2 spaces
   *          - at the end of a row, the last character should
   *              be immediately followed by a '\n' character
   */
  @Override
  public String toString() {
    String rep = "";

    // create string starting from top row to bottom,
    //  going left-to-right in the process
    for (int row=NUM_ROWS-1; row>=0; row--) {
      for (int col=0; col<NUM_COLS; col++) {
        if (this.containsPiece(row,col)) {
          // insert piece string representation on square
          Piece piece = board[row][col];
          rep += String.format("%s  ", piece.toString());
        } else {
          // insert a '-' if no piece on that square
          rep += String.format("%s  ", "-");
        }
      }
      // remove the trialing 2 spaces at end of row
      rep = rep.substring(0, rep.length()-2);
      
      // insert a break after every row, except for the last one
      if (row != 0) {
        rep += "\n";
      }
    }

    return rep;
  }
}


