package src.datatypes;

// imports
import java.util.*;

/**
 * Represents a mutable board on which pieces may be placed and
 *  moved
 */
public class Board {
  // list of all the moves played, in order
  private final List<Move> moveList;
  // list of all captured pieces, in order
  private final List<Piece> capturedPieces;
  // side who is currently to move
  private Color turn;
  // board containing chess pieces
  private final Piece[][] board;

  // maps compressed string to UNMODIFIABLE sets of legal moves
  private final Map<String, Set<Move>> legalMoveHistory;

  // number of columns on the chess board
  private final int NUM_COLS=8;
  // number of rows on the chess board
  private final int NUM_ROWS=8;

  /**
   * Create a new Board
   */
  public Board() {
    // create moves list
    this.moveList = new LinkedList<Move>();
    // create captured pieces list
    this.capturedPieces = new LinkedList<Piece>();
    // create board
    this.board = new Piece[NUM_ROWS][NUM_COLS];
    // create legal move history
    this.legalMoveHistory = new HashMap<String, Set<Move>>();

    // set first turn to white
    this.turn = Color.WHITE;

    // populate pawns
    for (int col=0; col<NUM_COLS; col++) {
      // populate white pawns on 2nd row
      this.board[1][col] = Piece.pawn(Color.WHITE);
      // populate black pawns on 7th row
      this.board[6][col] = Piece.pawn(Color.BLACK);
    }

    // populate rooks
    this.board[0][0] = Piece.rook(Color.WHITE); // a1 rook
    this.board[0][7] = Piece.rook(Color.WHITE); // h1 rook
    this.board[7][0] = Piece.rook(Color.BLACK); // a8 rook
    this.board[7][7] = Piece.rook(Color.BLACK); // h8 rook

    // populate knights
    this.board[0][1] = Piece.knight(Color.WHITE); // b1 knight
    this.board[0][6] = Piece.knight(Color.WHITE); // g1 knight
    this.board[7][1] = Piece.knight(Color.BLACK); // b8 knight
    this.board[7][6] = Piece.knight(Color.BLACK); // g8 knight

    // populate bishops
    this.board[0][2] = Piece.bishop(Color.WHITE); // c1 bishop
    this.board[0][5] = Piece.bishop(Color.WHITE); // f1 bishop
    this.board[7][2] = Piece.bishop(Color.BLACK); // c8 bishop
    this.board[7][5] = Piece.bishop(Color.BLACK); // f8 bishop

    // populate queens
    this.board[0][3] = Piece.queen(Color.WHITE); // d1 queen
    this.board[7][3] = Piece.queen(Color.BLACK); // d8 queen

    // populate kings
    this.board[0][4] = Piece.king(Color.WHITE); // e1 king
    this.board[7][4] = Piece.king(Color.BLACK); // e8 king
  }

  /**
   * Create new Board
   * @param boardStr representation of position as described
   *                in the @see toString specification
   * @param turn the player (white or black) to next to move
   */
  public Board(String boardStr, Color turn) {
    // create moves list
    this.moveList = new LinkedList<Move>();
    // create captured pieces list
    this.capturedPieces = new LinkedList<Piece>();
    // create board
    this.board = new Piece[NUM_ROWS][NUM_COLS];
    // create legal move history
    this.legalMoveHistory = new HashMap<String, Set<Move>>();

    // set first turn to white
    this.turn = turn;

    // split, delimiting by double-spaces and newlines
    String[] tokens = boardStr.split("  |\n");

    int row = 7;
    int col = 0;
    for (String token : tokens) {
      if (token.equals("-")) {
        // empty square, so do nothing
      } else {
        switch(token) {
          case "P":
            this.board[row][col] = Piece.pawn(Color.WHITE);
            break;
          case "R":
            this.board[row][col] = Piece.rook(Color.WHITE);
            break;
          case "N":
            this.board[row][col] = Piece.knight(Color.WHITE);
            break;
          case "B":
            this.board[row][col] = Piece.bishop(Color.WHITE);
            break;
          case "Q":
            this.board[row][col] = Piece.queen(Color.WHITE);
            break;
          case "K":
            this.board[row][col] = Piece.king(Color.WHITE);
            break;
          case "p":
            this.board[row][col] = Piece.pawn(Color.BLACK);
            break;
          case "r":
            this.board[row][col] = Piece.rook(Color.BLACK);
            break;
          case "n":
            this.board[row][col] = Piece.knight(Color.BLACK);
            break;
          case "b":
            this.board[row][col] = Piece.bishop(Color.BLACK);
            break;
          case "q":
            this.board[row][col] = Piece.queen(Color.BLACK);
            break;
          case "k":
            this.board[row][col] = Piece.king(Color.BLACK);
            break;
          default:
            throw new Error(String.format("Unhandled token type: %s", token));
        }
      }

      col++;
      if (col == 8) {
        row--;
        col = 0;
      }
    }
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
   * Obtain which side is currently to move
   * @return the side currently to move
   */
  public Color getTurn() {
    return this.turn;
  }

  /**
   * Toggle the side to move from white to black, or black to white
   */
  public void toggleTurn() {
    // TODO
  }

  /**
   * Undoes the last move played on this board - does nothing
   *  if there are no more moves to undo
   */
  public void undoLastMove() {
    // TODO
  }

  /**
   * Make a move on this board - does nothing if move is illegal
   */
  public void move(Move move) {
    // TODO
    this.moveList.add(move);
  }

  /**
   * Obtain the set of legal moves for this position
   * @return the set of legal moves for the current side
   *          to move
   */
  public Set<Move> legalMoves() {
    String compressedBoard = this.compressBoard();
    if (this.legalMoveHistory.containsKey(compressedBoard)) {
      // return unmodifiable set of legal moves
      return this.legalMoveHistory.get(compressedBoard);
    }

    Set<Move> legalMoveSet = new HashSet<>();
    // TODO 
    // Compute set of legal moves

    // add unmodifiable version of legal moves to hashmap for future calls
    this.legalMoveHistory.put(compressedBoard, Collections.unmodifiableSet(legalMoveSet));
    return legalMoveSet;
  }

  /**
   * Obtain the list of pieces that have been captured
   * @return the list of pieces that have been captured, in order
   */
  public List<Piece> getCapturedPieces() {
    List<Piece> capturedPiecesCopy = new LinkedList<Piece>();
    for (Piece capturedPiece : this.capturedPieces) {
      capturedPiecesCopy.add(capturedPiece.copy());
    }
    return capturedPiecesCopy;
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
   * Compresses the board state into a string s such that
   *  only equivalent positions gets compressed to s
   *    - two positions are equivalent if:
   *      - same player to move
   *      - same set of legal moves
   *      - same pieces of same color on same squares
   *  @return the string s that the board gets compressed to
   */
  public String compressBoard() {
    String s = "";
    // encode position
    for (int row=0; row<NUM_ROWS; row++) {
      for (int col=0; col<NUM_COLS; col++) {
        if (this.containsPiece(row,col)) {
          Piece piece = board[row][col];
          PieceType type = piece.getType();
          if (!type.equals(PieceType.ROOK) && !type.equals(PieceType.KING)) {
            // piece on this square, but not a rook or king
            s += String.format("%s", piece.toString());
          } else {
            // piece on this square, but rook or king, so have to
            //  include whether or not it moved
            s += String.format("%s%s", piece.toString(), (piece.hasMoved() ? "v":"x"));
          }
        } else {
          // no piece, insert a - in place of empty square
          s += String.format("%s", "-");
        }
      }
    }

    // encode player turn
    switch(this.getTurn()) {
      case WHITE:
        s += "w";
        break;
      case BLACK:
        s += "l";
        break;
      default:
        throw new Error("Unexpected Color");
    }

    // encode last move played
    Move lastMove = this.getLastMove();
    if (lastMove == null) {
      s += "/";
    } else {
      s += lastMove.toString();
    }

    return s;
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

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Board)) {return false;}
    Board otherBoard = (Board) other;
    // TODO board is equivalent if same player to move
    //        same pieces of same color occupy squares
    //        same legal moves for all pieces
    return false;
  }
}

