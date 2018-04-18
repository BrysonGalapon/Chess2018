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
  // list of all the pieces (pawns) that have been promoted, in order
  private final List<Piece> promotedPieces = new ArrayList<>();
  // board containing chess pieces
  private final Piece[][] board;

  // maps compressed string to UNMODIFIABLE sets of legal moves
  private final Map<String, Set<Move>> legalMoveHistory;

  // number of columns on the chess board
  private final int NUM_COLS=8;
  // number of rows on the chess board
  private final int NUM_ROWS=8;
  // square where black king is initially located
  private final Tuple<Integer, Integer> blackKingInitSq=new Tuple<>(7,4);
  // square where white king is initially located
  private final Tuple<Integer, Integer> whiteKingInitSq=new Tuple<>(0,4);

  // white kingside castle squares 
  private final List<Tuple<Integer,Integer>> whiteKCastleSq=new ArrayList<>();
  // white queenside castle squares
  private final List<Tuple<Integer,Integer>> whiteQCastleSq=new ArrayList<>();
  // black kingside castle squares
  private final List<Tuple<Integer,Integer>> blackKCastleSq=new ArrayList<>();
  // black queenside castle squares
  private final List<Tuple<Integer,Integer>> blackQCastleSq=new ArrayList<>();

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

    // populate white kingside castle squares
    whiteKCastleSq.add(new Tuple<>(0,5));
    whiteKCastleSq.add(new Tuple<>(0,6));
    // populate white queenside castle squares
    whiteQCastleSq.add(new Tuple<>(0,3));
    whiteQCastleSq.add(new Tuple<>(0,2));
    // populate black kingside castle squares
    blackKCastleSq.add(new Tuple<>(7,5));
    blackKCastleSq.add(new Tuple<>(7,6));
    // populate black queenside castle squares
    blackQCastleSq.add(new Tuple<>(7,3));
    blackQCastleSq.add(new Tuple<>(7,2));

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
   * Create new Board without a move or capture history
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

    // populate white kingside castle squares
    whiteKCastleSq.add(new Tuple<>(0,5));
    whiteKCastleSq.add(new Tuple<>(0,6));
    // populate white queenside castle squares
    whiteQCastleSq.add(new Tuple<>(0,3));
    whiteQCastleSq.add(new Tuple<>(0,2));
    // populate black kingside castle squares
    blackKCastleSq.add(new Tuple<>(7,5));
    blackKCastleSq.add(new Tuple<>(7,6));
    // populate black queenside castle squares
    blackQCastleSq.add(new Tuple<>(7,3));
    blackQCastleSq.add(new Tuple<>(7,2));

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
        this.board[row][col] = token2Piece(token);
      }
      col++;
      if (col == 8) {
        row--;
        col = 0;
      }
    }
  }

  /**
   * Create new Board with a move and capture history
   * @param boardStr representation of position as described
   *                in the @see toString specification
   * @param moveHistory the list of moves (in order) that were played on this board
   * @param capturedPieces the list of pieces (in order) that were captured
   * @param turn the player (white or black) to next to move
   */
  public Board(String boardStr, Color turn, List<Move> moveHistory, List<Piece> capturedPieces) {
    // create moves list
    this.moveList = new LinkedList<Move>(moveHistory);
    // create captured pieces list
    this.capturedPieces = new LinkedList<Piece>(capturedPieces);
    // create board
    this.board = new Piece[NUM_ROWS][NUM_COLS];
    // create legal move history
    this.legalMoveHistory = new HashMap<String, Set<Move>>();

    // populate white kingside castle squares
    whiteKCastleSq.add(new Tuple<>(0,5));
    whiteKCastleSq.add(new Tuple<>(0,6));
    // populate white queenside castle squares
    whiteQCastleSq.add(new Tuple<>(0,3));
    whiteQCastleSq.add(new Tuple<>(0,2));
    // populate black kingside castle squares
    blackKCastleSq.add(new Tuple<>(7,5));
    blackKCastleSq.add(new Tuple<>(7,6));
    // populate black queenside castle squares
    blackQCastleSq.add(new Tuple<>(7,3));
    blackQCastleSq.add(new Tuple<>(7,2));

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
        this.board[row][col] = token2Piece(token);
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
   *         if a piece doesn't exist, return null
   */
  private Piece clearSquare(int row, int col) {
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
    return piece;
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
   * Undoes the last move played on this board - does nothing
   *  if there are no more moves to undo
   */
  public void undoLastMove() {
    Move move = getLastMove();
 
    // don't do anything if no last move
    if (move == null) {return;}

    // undo the move by type of move
    
    int moveListSize = this.moveList.size();
    boolean canCheckEnPassent = moveListSize >= 2;
    boolean enPassentMove = false;
    if (canCheckEnPassent) {
      // check en passent
      Move moveBeforeMove = this.moveList.get(moveListSize-2);
      if (isEnPassent(move, moveBeforeMove)) {
        enPassentMove = true;
        // get last piece that was captured
        Piece capturedPiece = popLastCapturedPiece();

        int startRow = move.getStartRow();
        int startCol = move.getStartCol();
        int endCol = move.getEndCol();

        // get pawn that did en passent
        Piece pawn = clearSquare(move.getEndRow(), endCol);
        addPiece(pawn, startRow, startCol);
        pawn.indicateBackward();

        // figure out which side captured piece should be placed
        boolean capturedRight = (endCol-startCol) > 0;
        if (capturedRight) {
          addPiece(capturedPiece, startRow, startCol+1);
        } else {
          addPiece(capturedPiece, startRow, startCol-1);
        }
      }
    }

    if (!enPassentMove) {
      if (move.isCastleMove()) {

        // pick up the king
        int endRow = move.getEndRow();
        int endCol = move.getEndCol();
        Piece king = clearSquare(endRow, endCol);

        // pick up the rook
        int rookEndCol = (endCol == 2) ? 3 : 5;
        int rookStartCol = (endCol == 2) ? 0 : 7;
        Piece rook = clearSquare(endRow, rookEndCol);

        // place the king back
        addPiece(king, move.getStartRow(), move.getStartCol());
        king.indicateBackward();

        // place the rook back
        addPiece(rook, endRow, rookStartCol);
        rook.indicateBackward();

      } else if (move.isPromotion()) {

        int endRow = move.getEndRow();
        int endCol = move.getEndCol();

        // delete the promoted piece
        clearSquare(endRow, endCol);

        // put pawn back on start square
        Piece pawn = popLastPromotedPiece();
        addPiece(pawn, move.getStartRow(), move.getStartCol());
        pawn.indicateBackward();

        // if capturing a piece, place captured piece back
        if (move.isCapture()) {
          Piece capturedPiece = popLastCapturedPiece();
          addPiece(capturedPiece, endRow, endCol);
        } 

      } else {
        int endRow = move.getEndRow();
        int endCol = move.getEndCol();
        // pick up moved piece
        if (!containsPiece(endRow, endCol)) {
        }
        Piece movingPiece = clearSquare(endRow, endCol);

        // if move was a capture, place captured piece back
        if (move.isCapture()) {
          Piece clearedPiece = popLastCapturedPiece();
          addPiece(clearedPiece, endRow, endCol);
        }

        // place moved piece back
        addPiece(movingPiece, move.getStartRow(), move.getStartCol());
        movingPiece.indicateBackward();
      }
    }

    // remove last move from the move history
    this.moveList.remove(moveList.size()-1);
    // change the turn back
    this.toggleTurn();
  }

  /**
   * Make a move on this board - does nothing if move is illegal
   *  - if move is legal, toggles player turn
   * @param move move to be played on this board
   */
  public void move(Move move) {
    if (!legalMoves().contains(move)) {
      // don't do anything if move isn't legal
      // NOTE: might choose to remove this check when
      //        code that is using board class is verified
      //        to not give illegal moves - this will
      //        improve performance
      return;
    }

    // pass on move after verifying that move is legal
    moveNoCheck(move);
  }

  ///**
  // * Make a move on this board - does nothing if move is illegal
  // *  - if move is legal, toggles player turn
  // * @param startSq start square for move
  // * @param endSq end square for move
  // */
  //public void move(String startSq, String endSq) {
  //  // TODO
  //  Move move = new Move(startSq, endSq);
  //}

  /**
   * Make a move on this board, without checking if move is illegal
   *  - if move is legal, toggles player turn
   *  @param move move to be played on this board
   */
  private void moveNoCheck(Move move) {
    // determine whether move is one of:
    //  - en passent
    //  - castle
    //  - promotion
    // otherwise, the move is a regular type of move

    if (isEnPassent(move, getLastMove())) {
      int endCol = move.getEndCol();
      int startRow = move.getStartRow();
      int startCol = move.getStartCol();

      // get the pawn
      Piece pawn = clearSquare(startRow, startCol);
      // place it on target square
      addPiece(pawn, move.getEndRow(), endCol); 
      pawn.indicateMoved();

      // figure out which side captured piece is on
      boolean captureRight = (endCol-startCol) > 0;
      Piece capturedPiece;
      if (captureRight) {
          capturedPiece = clearSquare(startRow, startCol+1);
      } else {
          capturedPiece = clearSquare(startRow, startCol-1);
      }

      // add that piece to the captured list
      this.capturedPieces.add(capturedPiece);

    } else if (move.isCastleMove()) {
      // move the king to target square
      int endRow = move.getEndRow();
      int endCol = move.getEndCol();
      Piece king = clearSquare(move.getStartRow(), move.getStartCol());

      addPiece(king, endRow, endCol);
      // update the position of the king
      king.indicateMoved();

      // move appropriate rook to other side of king
      int rookEndCol = (endCol == 2) ? 3 : 5;
      int rookStartCol = (endCol == 2) ? 0 : 7;

      // remove rook from starting square
      //  - note that rook starts on same row as the king being
      //    moved
      Piece rook = clearSquare(endRow, rookStartCol);

      // move rook to other side of king
      addPiece(rook, endRow, rookEndCol);
      rook.indicateMoved();

    } else if (move.isPromotion()) {
      // create the piece to promote to
      Piece promotedPiece = Piece.newPiece(move.getPromotion(), getTurn());

      // clear the square piece is being moved to
      int endRow = move.getEndRow();
      int endCol = move.getEndCol();
      // if that square contained a piece, add it to captured list
      if (move.isCapture()) {
        Piece clearedPiece = clearSquare(endRow, endCol);
        this.capturedPieces.add(clearedPiece);
      }

      // clear the square containing the pawn
      Piece pawn = clearSquare(move.getStartRow(), move.getStartCol());
      pawn.indicateMoved();

      // record the pawn that promoted
      this.promotedPieces.add(pawn);

      // place piece at target square
      addPiece(promotedPiece, endRow, endCol);


    } else {
      // clear the square piece is being moved to
      int endRow = move.getEndRow();
      int endCol = move.getEndCol();
      // if that square contained a piece, add it to captured list
      if (move.isCapture()) {
        Piece clearedPiece = clearSquare(endRow, endCol);
        this.capturedPieces.add(clearedPiece);
      }

      // clear the square containing the piece being moved
      Piece movingPiece = clearSquare(move.getStartRow(), move.getStartCol());
      // place the piece at target square
      addPiece(movingPiece, endRow, endCol);
      movingPiece.indicateMoved();
   }

    // update the moveList to record the move just played
    this.moveList.add(move);
    // toggle the turn of the current player
    this.toggleTurn();
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

    boolean kingInCheck = inCheck();

    // get the squares that are under control by opposite team
    Set<Tuple<Integer, Integer>> threatSquares = threatenedSquares();
    // get squares that opposite team is pinning
    Set<Tuple<Integer, Integer>> pinSquares = pinnedSquares();

    // compute set of legal moves
    Piece piece;
    Set<Move> legalMoveSet = new HashSet<>();
    for (int row=0; row<NUM_ROWS; row++) {
      for (int col=0; col<NUM_COLS; col++) {
        // don't do anything if there's no piece
        if (!containsPiece(row,col)) {continue;}

        piece = getPiece(row,col);
        // don't do anything if piece is of opposite color
        if (!piece.getColor().equals(getTurn())) {continue;}

        // piece can't do anything if it is pinned
        if (pinSquares.contains(new Tuple<Integer,Integer>(row,col))) {continue;}

        // add the legal moves of that piece
        switch(piece.getType()) {
          case KING:
            legalMovesKing(row, col, kingInCheck, legalMoveSet, threatSquares);
            break;
          case QUEEN:
            legalMovesQueen(row, col, kingInCheck, legalMoveSet);
            break;
          case PAWN:
            legalMovesPawn(row, col, kingInCheck, legalMoveSet);
            break;
          case ROOK:
            legalMovesRook(row, col, kingInCheck, legalMoveSet);
            break;
          case KNIGHT:
            legalMovesKnight(row, col, kingInCheck, legalMoveSet);
            break;
          case BISHOP:
            legalMovesBishop(row, col, kingInCheck, legalMoveSet);
            break;
          default:
            throw new Error("Unhandled Type Input");
        }
      }
    }

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
    // board is equivalent if same player to move
    //   same pieces of same color occupy squares
    //   same legal moves for all pieces
    return this.compressBoard().equals(otherBoard.compressBoard());
  }

  /**
   * Converts a token describing a piece into the an equivalent piece
   * @param token the token as generated by @see toString for a piece
   * @return a Piece that represents that token
   */
  private Piece token2Piece (String token) {
    switch(token) {
      case "P":
        return Piece.pawn(Color.WHITE);
      case "R":
        return Piece.rook(Color.WHITE);
      case "N":
        return Piece.knight(Color.WHITE);
      case "B":
        return Piece.bishop(Color.WHITE);
      case "Q":
        return Piece.queen(Color.WHITE);
      case "K":
        return Piece.king(Color.WHITE);
      case "p":
        return Piece.pawn(Color.BLACK);
      case "r":
        return Piece.rook(Color.BLACK);
      case "n":
        return Piece.knight(Color.BLACK);
      case "b":
        return Piece.bishop(Color.BLACK);
      case "q":
        return Piece.queen(Color.BLACK);
      case "k":
        return Piece.king(Color.BLACK);
      default:
        throw new Error(String.format("Unhandled token type: %s", token));
    }
  }

  /**
   * Checks if a given move played
   *  is an en passent move
   *  @param move the move to be played 
   *  @param lastMove the last move played on this board
   *  @return true iff when moved played will
   *            result in en passent on the current board state
   */
  private boolean isEnPassent(Move move, Move lastMove) {
    // check if piece being moved is a pawn and is a capture
    if (!move.getPieceType().equals(PieceType.PAWN) || !move.isCapture()) {return false;}
    // check if any moves has been played
    if (lastMove == null) {return false;}
    // check if the last move was a pawn move
    if (!lastMove.getPieceType().equals(PieceType.PAWN)) {return false;}
    // check if the last move's pawn moved two steps
    int lastMoveEndRow = lastMove.getEndRow();
    int lastMoveStartRow = lastMove.getStartRow();
    if (Math.abs(lastMoveEndRow-lastMoveStartRow) != 2) {return false;}
    // check if last move's pawn move landed adjacent to starting
    //  square of move
    if ((lastMoveEndRow != move.getStartRow()) || move.getEndCol() != lastMove.getEndCol()) {return false;}

    return true;
  }

  /**
   * Places a piece onto the board at a specified row and column
   * @param piece the piece to place
   * @param row row to place the place piece at
   * @param col column to place the place piece at
   */
  private void addPiece(Piece piece, int row, int col) {
    this.board[row][col] = piece;
  }

  /**
   * Toggle the side to move from white to black, or black to white
   */
  public void toggleTurn() {
    this.turn = oppositeColor(this.turn);
  }

  /**
   * Add to set of moves that a king can make on this board if
   *  it is on a certain square
   * @param row row at which king is located
   * @param col column at which king is located
   * @param signalCheck whether or not position is in check
   * @param legalMoves set of moves to add legal king moves to
   * @param threatSquares a set of squares that is controlled by the opponent
   */
  private void legalMovesKing(int row, int col, boolean signalCheck, Set<Move> legalMoves, Set<Tuple<Integer, Integer>> threatSquares) {
    int square_row;
    int square_col;
    // look at all squares adjacent to king
    for (int xOffset=-1; xOffset<=1; xOffset++) {
      for (int yOffset=-1; yOffset<=1; yOffset++) {
        // don't look at same square as king
        if (xOffset == 0 && yOffset == 0) {continue;}

        square_row = row+yOffset;
        square_col = col+xOffset;
        // don't look at squares outside bounds of board
        if (square_row < 0 || square_row > 7) {continue;}
        if (square_col < 0 || square_col > 7) {continue;}
        
        // don't look at squares controlled by opponent
        if (threatSquares.contains(new Tuple<Integer, Integer>(square_row, square_col))) {continue;}

        if (containsPiece(square_row, square_col)) {
          // possible move square contains a piece
          Piece otherPiece = getPiece(square_row, square_col);
          
          // don't do anything if piece is of same color
          if (otherPiece.getColor().equals(getTurn())) {continue;}

          // add a capture move
          Move move = new Move(PieceType.KING, row, col, square_row, square_col, true);

          // ... but only if it is check
          if (signalCheck) {
            // make the move on the board
            this.moveNoCheck(move);

            // toggle the turn as if player was moving again
            this.toggleTurn();

            // if making move doesn't put king in check, then legal
            if (!inCheck()) {
              legalMoves.add(move);
            }

            // change the turn back
            this.toggleTurn();
            // undo the move played
            this.undoLastMove();
          } else {
            legalMoves.add(move);
          }
        } else {
          Move move = new Move(PieceType.KING, row, col, square_row, square_col);
          // add a king move
          // ... but only if it is check
          if (signalCheck) {
            // make the move on the board
            this.moveNoCheck(move);

            // toggle the turn as if player was moving again
            this.toggleTurn();

            // if making move doesn't put king in check, then legal
            if (!inCheck()) {
              legalMoves.add(move);
            }

            // change the turn back
            this.toggleTurn();
            // undo the move played
            this.undoLastMove();
          } else {
            legalMoves.add(move);
          }
        }
      }
    }

    // can't castle if in check
    if (signalCheck) {return;}

    // get king and its position
    Piece king = getPiece(row, col);
    Tuple<Integer, Integer> kingPos = new Tuple<>(row, col);

    Color side = getTurn();

    // can't castle if king is not in initial position
    if (side.equals(Color.WHITE)) {
      if (!kingPos.equals(whiteKingInitSq)) {return;}
    } else {
      if (!kingPos.equals(blackKingInitSq)) {return;}
    }

    // can't castle if king moved
    if (king.hasMoved()) {return;}
 
    // add castling moves
    addCastleKingSide(threatSquares, legalMoves);
    addCastleQueenSide(threatSquares, legalMoves);
  }

  /**
   * Adds a castling kingside move to set of legal moves, if it castling is legal
   * @param threatSquares a set of squares that is controlled by the opponent
   * @param legalMoves set of legal moves to add castling to, if castling kingside is legal
   */
  private void addCastleKingSide(Set<Tuple<Integer, Integer>> threatSquares, Set<Move> legalMoves) {
    Color side = getTurn();
    // set rook squares and castling list for turn
    Tuple<Integer, Integer> rookSq;
    List<Tuple<Integer, Integer>> castleSqs;
    
    if (side.equals(Color.WHITE)) {
      rookSq = new Tuple<>(0,7);
      castleSqs = whiteKCastleSq;
    } else {
      rookSq = new Tuple<>(7,7);
      castleSqs = blackKCastleSq;
    }

    int rookSqRow = rookSq.x();
    int rookSqCol = rookSq.y();

    // can't castle if rook isn't on its home square
    if (!containsPiece(rookSqRow, rookSqCol)) {return;}
    Piece rook = getPiece(rookSqRow, rookSqCol);

    // can't castle if piece on the home square isn't actually a rook
    //  - nor can't castle if piece on home square isn't the same color as the king
    //  - nor can't castle if rook has moved
    if (!rook.getType().equals(PieceType.ROOK) || !rook.getColor().equals(side) || rook.hasMoved()) {return;}

    // can't castle if king passes through check or into check
    //  - also can't castle if pieces occupy castle squares
    for (Tuple<Integer, Integer> square : castleSqs) {
      if (threatSquares.contains(square)) {return;}
      if (containsPiece(square.x(), square.y())) {return;}
    }

    // castling kingside is verified to be a legal move
    Move castleMove;
    if (side.equals(Color.WHITE)) {
      castleMove = new Move(PieceType.KING, "e1", "g1");
    } else {
      castleMove = new Move(PieceType.KING, "e8", "g8");
    }
    legalMoves.add(castleMove);
  }

  /**
   * Adds a castling queenside move to set of legal moves, if it castling is legal
   * @param threatSquares a set of squares that is controlled by the opponent
   * @param legalMoves set of legal moves to add castling to, if castling queenside is legal
   */
  private void addCastleQueenSide(Set<Tuple<Integer, Integer>> threatSquares, Set<Move> legalMoves) {
    Color side = getTurn();
    // set rook squares and castling list for turn
    Tuple<Integer, Integer> rookSq;
    List<Tuple<Integer, Integer>> castleSqs;
    if (side.equals(Color.WHITE)) {
      rookSq = new Tuple<>(0,0);
      castleSqs = whiteQCastleSq;
    } else {
      rookSq = new Tuple<>(7,0);
      castleSqs = blackQCastleSq;
    }

    int rookSqRow = rookSq.x();
    int rookSqCol = rookSq.y();

    // can't castle if rook isn't on its home square
    if (!containsPiece(rookSqRow, rookSqCol)) {return;}
    Piece rook = getPiece(rookSqRow, rookSqCol);
    // can't castle if piece on the home square isn't actually a rook
    //  - nor can't castle if piece on home square isn't the same color as the king
    //  - nor can't castle if rook has moved
    if (!rook.getType().equals(PieceType.ROOK) || !rook.getColor().equals(side) || rook.hasMoved()) {return;}

    // can't castle if king passes through check or into check
    for (Tuple<Integer, Integer> square : castleSqs) {
      if (threatSquares.contains(square)) {return;}
      if (containsPiece(square.x(), square.y())) {return;}
    }

    // can't castle if square in between rook+castleSqs is occupied
    Tuple<Integer, Integer> lastCastleSq = castleSqs.get(castleSqs.size()-1);
    if (containsPiece(lastCastleSq.x(), lastCastleSq.y()-1)) {return;}

    // castling kingside is verified to be a legal move
    Move castleMove;
    if (side.equals(Color.WHITE)) {
      castleMove = new Move(PieceType.KING, "e1", "c1");
    } else {
      castleMove = new Move(PieceType.KING, "e8", "c8");
    }
    legalMoves.add(castleMove);
  }

  /**
   * Add to set of moves that a queen can make on this board if
   *  it is on a certain square and is not pinned
   * @param row row at which queen is located
   * @param col column at which queen is located
   * @param signalCheck whether or not position is in check
   * @param legalMoves set of moves to add legal queen moves to
   */
  private void legalMovesQueen(int row, int col, boolean signalCheck, Set<Move> legalMoves) {
    Set<Tuple<Integer, Integer>> threatSquares = threatenedSquaresQueen(row, col, getTurn());
    int square_row;
    int square_col;
    Color side = getTurn();
    Move move;
    for (Tuple<Integer, Integer> square : threatSquares) {
      square_row = square.x();
      square_col = square.y();
      // if threatened square contains a piece, then it must be a capture - otherwise it isn't a capture
      if (containsPiece(square_row, square_col)) {
        Piece capturePiece = getPiece(square_row, square_col);
        // don't capture own piece
        if (capturePiece.getColor().equals(side)) {continue;}

        move = new Move(PieceType.QUEEN, row, col, square_row, square_col, true);
      } else {
        move = new Move(PieceType.QUEEN, row, col, square_row, square_col);
      }

      if (signalCheck) {
        // make the move on the board
        this.moveNoCheck(move);

        // toggle the turn as if player was moving again
        this.toggleTurn();

        // if making move doesn't put king in check, then legal
        if (!inCheck()) {
          legalMoves.add(move);
        }

        // change the turn back
        this.toggleTurn();
        // undo the move played
        this.undoLastMove();
      } else {
        legalMoves.add(move);
      }
    }
  }

  /**
   * Add to set of moves that a rook can make on this board if
   *  it is on a certain square and is not pinned
   * @param row row at which rook is located
   * @param col column at which rook is located
   * @param signalCheck whether or not position is in check
   * @param legalMoves set of moves to add legal rook moves to
   */
  private void legalMovesRook(int row, int col, boolean signalCheck, Set<Move> legalMoves) {
    Set<Tuple<Integer, Integer>> threatSquares = threatenedSquaresRook(row, col, getTurn());
    int square_row;
    int square_col;
    Move move;
    Color side = getTurn();
    for (Tuple<Integer, Integer> square : threatSquares) {
      square_row = square.x();
      square_col = square.y();
      // if threatened square contains a piece, then it must be a capture - otherwise it isn't a capture
      if (containsPiece(square_row, square_col)) {
        Piece capturePiece = getPiece(square_row, square_col);
        // don't capture own piece
        if (capturePiece.getColor().equals(side)) {continue;}

        move = new Move(PieceType.ROOK, row, col, square_row, square_col, true);
      } else {
        move = new Move(PieceType.ROOK, row, col, square_row, square_col);
      }

      if (signalCheck) {
        // make the move on the board
        this.moveNoCheck(move);

        // toggle the turn as if player was moving again
        this.toggleTurn();

        // if making move doesn't put king in check, then legal
        if (!inCheck()) {
          legalMoves.add(move);
        }

        // change the turn back
        this.toggleTurn();
        // undo the move played
        this.undoLastMove();
      } else {
        legalMoves.add(move);
      }
    }
  }
  
  /**
   * Add to set of moves that a knight can make on this board if
   *  it is on a certain square and is not pinned
   * @param row row at which knight is located
   * @param col column at which knight is located
   * @param signalCheck whether or not position is in check
   * @param legalMoves set of moves to add legal knight moves to
   */
  private void legalMovesKnight(int row, int col, boolean signalCheck, Set<Move> legalMoves) {
    Set<Tuple<Integer, Integer>> threatSquares = threatenedSquaresKnight(row, col, getTurn());
    int square_row;
    int square_col;
    Move move;
    Color side = getTurn();
    for (Tuple<Integer, Integer> square : threatSquares) {
      square_row = square.x();
      square_col = square.y();
      // if threatened square contains a piece, then it must be a capture - otherwise it isn't a capture
      if (containsPiece(square_row, square_col)) {
        Piece capturePiece = getPiece(square_row, square_col);
        // don't capture own piece
        if (capturePiece.getColor().equals(side)) {continue;}

        move = new Move(PieceType.KNIGHT, row, col, square_row, square_col, true);
      } else {
        move = new Move(PieceType.KNIGHT, row, col, square_row, square_col);
      }

      if (signalCheck) {
        // make the move on the board
        this.moveNoCheck(move);

        // toggle the turn as if player was moving again
        this.toggleTurn();

        // if making move doesn't put king in check, then legal
        if (!inCheck()) {
          legalMoves.add(move);
        }

        // change the turn back
        this.toggleTurn();
        // undo the move played
        this.undoLastMove();
      } else {
        legalMoves.add(move);
      }
    }
  }

  /**
   * Add to set of moves that a bishop can make on this board if
   *  it is on a certain square and is not pinned
   * @param row row at which bishop is located
   * @param col column at which bishop is located
   * @param signalCheck whether or not position is in check
   * @param legalMoves set of moves to add legal bishop moves to
   */
  private void legalMovesBishop(int row, int col, boolean signalCheck, Set<Move> legalMoves) {
    Set<Tuple<Integer, Integer>> threatSquares = threatenedSquaresBishop(row, col, getTurn());
    int square_row;
    int square_col;
    Move move;
    Color side = getTurn();
    for (Tuple<Integer, Integer> square : threatSquares) {
      square_row = square.x();
      square_col = square.y();
      // if threatened square contains a piece, then it must be a capture - otherwise it isn't a capture
      if (containsPiece(square_row, square_col)) {
        Piece capturePiece = getPiece(square_row, square_col);
        // don't capture own piece
        if (capturePiece.getColor().equals(side)) {continue;}

        move = new Move(PieceType.BISHOP, row, col, square_row, square_col, true);
      } else {
        move = new Move(PieceType.BISHOP, row, col, square_row, square_col);
      }

      if (signalCheck) {
        // make the move on the board
        this.moveNoCheck(move);

        // toggle the turn as if player was moving again
        this.toggleTurn();

        // if making move doesn't put king in check, then legal
        if (!inCheck()) {
          legalMoves.add(move);
        }

        // change the turn back
        this.toggleTurn();
        // undo the move played
        this.undoLastMove();
      } else {
        legalMoves.add(move);
      }
    }
  }

  /**
   * Add to set of moves that a pawn can make on this board if
   *  it is on a certain square and is not pinned
   * @param row row at which pawn is located
   * @param col column at which pawn is located
   * @param signalCheck whether or not position is in check
   * @param legalMoves set of moves to add legal pawn moves to
   */
  private void legalMovesPawn(int row, int col, boolean signalCheck, Set<Move> legalMoves) {
    Set<Move> possibleMoves = new HashSet<Move>();

    // grab the pawn
    Piece pawn = getPiece(row, col);

    Color color = pawn.getColor();
    // set yOffset to point in direction of pawn advance
    // set initRow to rank where pawn must have started
    // set promoteRow to 1 LESS rank before pawn promotes
    int yOffset;
    int initRow;
    int promoteRow;
    if (color.equals(Color.WHITE)) {
      yOffset = 1;
      initRow = 1;
      promoteRow = 6;
    } else {
      yOffset = -1;
      initRow = 6;
      promoteRow = 1;
    }

    boolean hasPieceInFront = containsPiece(row+yOffset,col);

    // if no piece blocking pawn's path, pawn can move forward 
    //  one square
    //    - check for promotion if on second-to-last rank
    if (!hasPieceInFront) {
      if (row == promoteRow) {
        possibleMoves.add(new Move(PieceType.PAWN, row, col, row+yOffset, col, false, PieceType.QUEEN));
        possibleMoves.add(new Move(PieceType.PAWN, row, col, row+yOffset, col, false, PieceType.ROOK));
        possibleMoves.add(new Move(PieceType.PAWN, row, col, row+yOffset, col, false, PieceType.BISHOP));
        possibleMoves.add(new Move(PieceType.PAWN, row, col, row+yOffset, col, false, PieceType.KNIGHT));
      } else {
        possibleMoves.add(new Move(PieceType.PAWN, row, col, row+yOffset, col));
      }
    }

    // if pawn hasn't moved yet and no piece blocking BOTH
    //  squares, then pawn can move two squares forward
    if (row == initRow && !hasPieceInFront) {
      if (!containsPiece(row+2*yOffset,col)) {
        possibleMoves.add(new Move(PieceType.PAWN, row, col, row+2*yOffset, col));
      }
    }

    // check to see if pawn captures are valid, including en passent
    int squareRow;
    int squareCol;

    // LEFT CAPTURE
    squareRow = row+yOffset;
    squareCol = col-1;
    if (inBounds(squareRow, squareCol)) {
      if (containsPiece(squareRow, squareCol) && !getPiece(squareRow, squareCol).getColor().equals(color)) {
        // check if capture square is in bounds, and contains a piece that is of the opposite color
        //  - check for promotion square
        if (row == promoteRow) {
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true, PieceType.QUEEN));
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true, PieceType.ROOK));
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true, PieceType.BISHOP));
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true, PieceType.KNIGHT));
        } else {
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true));
        }
      }

      // check for enPassent
      Move lastMove = getLastMove();
      if (lastMove != null) {
        int lmEndRow = lastMove.getEndRow();
        int lmStartRow = lastMove.getStartRow();

        boolean movedPawn = lastMove.getPieceType().equals(PieceType.PAWN);
        boolean pushedPawn2Sq = Math.abs(lmEndRow-lmStartRow) == 2;
        boolean landedByPawn = (lmEndRow == row) && col-lastMove.getEndCol() == 1;

        if (movedPawn && pushedPawn2Sq && landedByPawn) {
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true));
        }
      }
    }
    // RIGHT CAPTURE
    squareRow = row+yOffset;
    squareCol = col+1;
    if (inBounds(squareRow, squareCol)) {
      if (containsPiece(squareRow, squareCol) && !getPiece(squareRow, squareCol).getColor().equals(color)) {
        // check if capture square is in bounds, and contains a piece that is of the opposite color
        //  - check for promotion square
        if (row == promoteRow) {
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true, PieceType.QUEEN));
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true, PieceType.ROOK));
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true, PieceType.BISHOP));
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true, PieceType.KNIGHT));
        } else {
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true));
        }
      }

      // check for enPassent
      Move lastMove = getLastMove();
      if (lastMove != null) {
        int lmEndRow = lastMove.getEndRow();
        int lmStartRow = lastMove.getStartRow();

        boolean movedPawn = lastMove.getPieceType().equals(PieceType.PAWN);
        boolean pushedPawn2Sq = Math.abs(lmEndRow-lmStartRow) == 2;
        boolean landedByPawn = (lmEndRow == row) && lastMove.getEndCol()-col == 1;

        if (movedPawn && pushedPawn2Sq && landedByPawn) {
          possibleMoves.add(new Move(PieceType.PAWN, row, col, squareRow, squareCol, true));
        }
      }
    }

    if (signalCheck) {
      for (Move move : possibleMoves) {
        // make the move on the board
        this.moveNoCheck(move);

        // toggle the turn as if player was moving again
        this.toggleTurn();

        // if making move doesn't put king in check, then legal
        if (!inCheck()) {
          legalMoves.add(move);
        }

        // change the turn back
        this.toggleTurn();
        // undo the move played
        this.undoLastMove();
      }
    } else {
      // if not in check, all possible moves are legal
      legalMoves.addAll(possibleMoves);
    }
  }

  /**
   * Get the piece that was last captured on this board, and
   *  removes it from the captured list
   * @return the piece that was last captured. return null
   *          if no piece was captured at this point
   */
  private Piece popLastCapturedPiece() {
    int numCaptured = capturedPieces.size();
    if (numCaptured == 0) {return null;}

    Piece piece = capturedPieces.get(numCaptured-1);
    capturedPieces.remove(numCaptured-1);
    return piece;
  }

  /**
   * Get the piece that was last promoted on this board, and
   *  removes it from the promoted list
   * @return the piece that was last promoted. return null
   *          if no piece was promoted at this point
   */
  private Piece popLastPromotedPiece() {
    int numPromoted = promotedPieces.size();
    if (numPromoted == 0) {return null;}

    Piece piece = promotedPieces.get(numPromoted-1);
    promotedPieces.remove(numPromoted-1);
    return piece;
  }

  /**
   * Get a set of threatened squares on the board for opponent
   * @return a set of all squares that are threatened on this board
   *  - a square is threatened if (assuming it is the opponents turn), the opponent can capture a piece on that square, OR
   *    if a king captures a piece on that square, it will be
   *      in check
   */
  private Set<Tuple<Integer, Integer>> threatenedSquares() {
    // initialize set
    Set<Tuple<Integer, Integer>> squares = new HashSet<Tuple<Integer, Integer>>();

    // get opposite color of the current turn
    Color oppColor = oppositeColor(getTurn());

    // loop through all squares
    for (int row=0; row<NUM_ROWS; row++) {
      for (int col=0; col<NUM_COLS; col++) {
        // skip squares with no pieces
        if (!containsPiece(row,col)) {continue;}

        // skip squares with pieces of same color
        Piece piece = getPiece(row,col);
        if (!piece.getColor().equals(oppColor)) {continue;}

        switch(piece.getType()) {
          case KING:
            squares.addAll(threatenedSquaresKing(row,col,oppColor));
            break;
          case QUEEN:
            squares.addAll(threatenedSquaresQueen(row,col,oppColor));
            break;
          case PAWN:
            squares.addAll(threatenedSquaresPawn(row,col,oppColor));
            break;
          case ROOK:
            squares.addAll(threatenedSquaresRook(row,col,oppColor));
            break;
          case KNIGHT:
            squares.addAll(threatenedSquaresKnight(row,col,oppColor));
            break;
          case BISHOP:
            squares.addAll(threatenedSquaresBishop(row,col,oppColor));
            break;
          default:
            throw new Error("Unhandled Piece Type");
        }
      }
    }

    return squares;
  }

  private int pinnedIterateRay(int squareRow, int squareCol, Color playerColor, List<Boolean> passedSameColorList, List<Integer> pinnedSquareList) {
    boolean passedSameColor = passedSameColorList.get(0);
    // make sure within boundary of board
    if (squareRow < 0 || squareRow > 7) {return 0;}
    if (squareCol < 0 || squareCol > 7) {return 0;}

    // skip if no piece
    if (!containsPiece(squareRow, squareCol)) {return 1;}
    Piece piece = getPiece(squareRow, squareCol);
    Color pieceColor = piece.getColor();
    // if we run into a piece of opposite color without passing a piece of same color, then there is no pin
    if (!pieceColor.equals(playerColor) && !passedSameColor) {return 0;}

    // we found a piece of same color
    if (pieceColor.equals(playerColor)) {
      if (!passedSameColor) {
        // first time we found piece of same color
        //  - record possible pin square
        //  - set the flag, and then move on
        pinnedSquareList.add(0, squareRow);
        pinnedSquareList.add(1, squareCol);
        passedSameColorList.add(0, true);
        return 1;
      } else {
        // second time we found piece of same color, no pin
        return 0;
      }
    } else {
      return 2;
    }
  }

  /**
   * Obtain the set of squares that contain pieces of the current player to move that are pinned (and therefore can't move)
   */
  private Set<Tuple<Integer, Integer>> pinnedSquares() {
    Set<Tuple<Integer, Integer>> pinSquares = new HashSet<Tuple<Integer,Integer>>();

    Color playerColor = getTurn();
    // get the king position
    Tuple<Integer, Integer> kingPos = getKingPos(playerColor);
    int kingRow = kingPos.x();
    int kingCol = kingPos.y();

    List<Boolean> passedSameColorList = new ArrayList<>(Arrays.asList(false));
    List<Integer> pinnedSquareList = new ArrayList<>(Arrays.asList(-1,-1));

    int squareRow;
    int squareCol;

    // NORTH
    passedSameColorList.add(0, false);
    for (int offset=1; offset<=NUM_ROWS; offset++) {
      squareRow = kingRow+offset;
      squareCol = kingCol;

      int value = pinnedIterateRay(squareRow, squareCol, playerColor, passedSameColorList, pinnedSquareList);
      if (value == 0) {break;}
      if (value == 1) {continue;}
      if (value == 2) {
        PieceType type = getPiece(squareRow, squareCol).getType();
        if (type.equals(PieceType.ROOK) || type.equals(PieceType.QUEEN)) {
          int pinSquareRow = pinnedSquareList.get(0);
          int pinSquareCol = pinnedSquareList.get(1);
          pinSquares.add(new Tuple<>(pinSquareRow, pinSquareCol));
        }
        break;
      }
    }

    // EAST
    passedSameColorList.add(0, false);
    for (int offset=1; offset<=NUM_ROWS; offset++) {
      squareRow = kingRow;
      squareCol = kingCol+offset;

      int value = pinnedIterateRay(squareRow, squareCol, playerColor, passedSameColorList, pinnedSquareList);
      if (value == 0) {break;}
      if (value == 1) {continue;}
      if (value == 2) {
        PieceType type = getPiece(squareRow, squareCol).getType();
        if (type.equals(PieceType.ROOK) || type.equals(PieceType.QUEEN)) {
          int pinSquareRow = pinnedSquareList.get(0);
          int pinSquareCol = pinnedSquareList.get(1);
          pinSquares.add(new Tuple<>(pinSquareRow, pinSquareCol));
        }
        break;
      }
    }

    // SOUTH
    passedSameColorList.add(0, false);
    for (int offset=1; offset<=NUM_ROWS; offset++) {
      squareRow = kingRow-offset;
      squareCol = kingCol;

      int value = pinnedIterateRay(squareRow, squareCol, playerColor, passedSameColorList, pinnedSquareList);
      if (value == 0) {break;}
      if (value == 1) {continue;}
      if (value == 2) {
        PieceType type = getPiece(squareRow, squareCol).getType();
        if (type.equals(PieceType.ROOK) || type.equals(PieceType.QUEEN)) {
          int pinSquareRow = pinnedSquareList.get(0);
          int pinSquareCol = pinnedSquareList.get(1);
          pinSquares.add(new Tuple<>(pinSquareRow, pinSquareCol));
        }
        break;
      }
    }

    // WEST
    passedSameColorList.add(0, false);
    for (int offset=1; offset<=NUM_ROWS; offset++) {
      squareRow = kingRow;
      squareCol = kingCol-offset;

      int value = pinnedIterateRay(squareRow, squareCol, playerColor, passedSameColorList, pinnedSquareList);
      if (value == 0) {break;}
      if (value == 1) {continue;}
      if (value == 2) {
        PieceType type = getPiece(squareRow, squareCol).getType();
        if (type.equals(PieceType.ROOK) || type.equals(PieceType.QUEEN)) {
          int pinSquareRow = pinnedSquareList.get(0);
          int pinSquareCol = pinnedSquareList.get(1);
          pinSquares.add(new Tuple<>(pinSquareRow, pinSquareCol));
        }
        break;
      }
    }

    // NORTH-EAST
    passedSameColorList.add(0, false);
    for (int offset=1; offset<=NUM_ROWS; offset++) {
      squareRow = kingRow+offset;
      squareCol = kingCol+offset;

      int value = pinnedIterateRay(squareRow, squareCol, playerColor, passedSameColorList, pinnedSquareList);
      if (value == 0) {break;}
      if (value == 1) {continue;}
      if (value == 2) {
        PieceType type = getPiece(squareRow, squareCol).getType();
        if (type.equals(PieceType.BISHOP) || type.equals(PieceType.QUEEN)) {
          int pinSquareRow = pinnedSquareList.get(0);
          int pinSquareCol = pinnedSquareList.get(1);
          pinSquares.add(new Tuple<>(pinSquareRow, pinSquareCol));
        }
        break;
      }
    }

    // NORTH-WEST
    passedSameColorList.add(0, false);
    for (int offset=1; offset<=NUM_ROWS; offset++) {
      squareRow = kingRow+offset;
      squareCol = kingCol-offset;

      int value = pinnedIterateRay(squareRow, squareCol, playerColor, passedSameColorList, pinnedSquareList);
      if (value == 0) {break;}
      if (value == 1) {continue;}
      if (value == 2) {
        PieceType type = getPiece(squareRow, squareCol).getType();
        if (type.equals(PieceType.BISHOP) || type.equals(PieceType.QUEEN)) {
          int pinSquareRow = pinnedSquareList.get(0);
          int pinSquareCol = pinnedSquareList.get(1);
          pinSquares.add(new Tuple<>(pinSquareRow, pinSquareCol));
        }
        break;
      }
    }

    // SOUTH-EAST
    passedSameColorList.add(0, false);
    for (int offset=1; offset<=NUM_ROWS; offset++) {
      squareRow = kingRow-offset;
      squareCol = kingCol+offset;

      int value = pinnedIterateRay(squareRow, squareCol, playerColor, passedSameColorList, pinnedSquareList);
      if (value == 0) {break;}
      if (value == 1) {continue;}
      if (value == 2) {
        PieceType type = getPiece(squareRow, squareCol).getType();
        if (type.equals(PieceType.BISHOP) || type.equals(PieceType.QUEEN)) {
          int pinSquareRow = pinnedSquareList.get(0);
          int pinSquareCol = pinnedSquareList.get(1);
          pinSquares.add(new Tuple<>(pinSquareRow, pinSquareCol));
        }
        break;
      }
    }

    // SOUTH-WEST
    passedSameColorList.add(0, false);
    for (int offset=1; offset<=NUM_ROWS; offset++) {
      squareRow = kingRow-offset;
      squareCol = kingCol-offset;

      int value = pinnedIterateRay(squareRow, squareCol, playerColor, passedSameColorList, pinnedSquareList);
      if (value == 0) {break;}
      if (value == 1) {continue;}
      if (value == 2) {
        PieceType type = getPiece(squareRow, squareCol).getType();
        if (type.equals(PieceType.BISHOP) || type.equals(PieceType.QUEEN)) {
          int pinSquareRow = pinnedSquareList.get(0);
          int pinSquareCol = pinnedSquareList.get(1);
          pinSquares.add(new Tuple<>(pinSquareRow, pinSquareCol));
        }
        break;
      }
    }

    return pinSquares;
  }

  /**
   * Obtain the set of squares that a king of a certain color would threaten if placed at a particular location
   * @param row row at which the king is placed
   * @param col column at which the king is placed
   * @param color color of the king
   */
  private Set<Tuple<Integer, Integer>> threatenedSquaresKing(int row, int col, Color color) {
    Set<Tuple<Integer, Integer>> squares = new HashSet<Tuple<Integer, Integer>>();
    int square_row;
    int square_col;
    for (int xOffset=-1; xOffset<=1; xOffset++) {
      for (int yOffset=-1; yOffset<=1; yOffset++) {
        // don't look at same square as piece
        if (xOffset == 0 && yOffset == 0) {continue;}

        square_row = row+yOffset;
        square_col = col+xOffset;

        // don't look at squares outside bounds of board
        if (square_row < 0 || square_row > 7) {continue;}
        if (square_col < 0 || square_col > 7) {continue;}

        squares.add(new Tuple<>(square_row,square_col));
      }
    }

    return squares;
  }

  /**
   * Obtain the set of squares that a queen of a certain color would threaten if placed at a particular location
   * @param row row at which the queen is placed
   * @param col column at which the queen is placed
   * @param color color of the queen
   */
  private Set<Tuple<Integer, Integer>> threatenedSquaresQueen(int row, int col, Color color) {
    Set<Tuple<Integer, Integer>> squares = new HashSet<Tuple<Integer, Integer>>();

    // queen threats are made of bishop threats...
    squares.addAll(threatenedSquaresBishop(row,col,color));
    // ... and rook threats
    squares.addAll(threatenedSquaresRook(row,col,color));

    return squares;
  }

  /**
   * Obtain the set of squares that a pawn of a certain color would threaten if placed at a particular location
   * @param row row at which the pawn is placed
   * @param col column at which the pawn is placed
   * @param color color of the pawn
   */
  private Set<Tuple<Integer, Integer>> threatenedSquaresPawn(int row, int col, Color color) {
    Set<Tuple<Integer, Integer>> squares = new HashSet<Tuple<Integer, Integer>>();

    // threatened squares are diagonal capture squares

    int yOffset;
    if (color.equals(Color.WHITE)) {
      yOffset = 1;
    } else {
      yOffset = -1;
    }

    int leftSquareCol = col-1;
    int leftSquareRow = row+yOffset;
    int rightSquareCol = col+1;
    int rightSquareRow = row+yOffset;

    // check if left capture square is threatened
    if (inBounds(leftSquareRow, leftSquareCol)) {
      squares.add(new Tuple<>(leftSquareRow, leftSquareCol));
    }

    // check if right capture square is threatened
    if (inBounds(rightSquareRow, rightSquareCol)) {
      squares.add(new Tuple<>(rightSquareRow, rightSquareCol));
    }

    return squares;
  }

  /**
   * Obtain the set of squares that a rook of a certain color would threaten if placed at a particular location
   * @param row row at which the rook is placed
   * @param col column at which the rook is placed
   * @param color color of the rook
   */
  private Set<Tuple<Integer, Integer>> threatenedSquaresRook(int row, int col, Color color) {
    Set<Tuple<Integer, Integer>> squares = new HashSet<Tuple<Integer, Integer>>();
    int squareRow;
    int squareCol;

    // EAST
    for (int offset=1; offset<=NUM_COLS; offset++) {
      squareRow = row;
      squareCol = col+offset;
      if (!inBounds(squareRow, squareCol)) {break;} // out of bounds
      if (!containsPiece(squareRow, squareCol)) {
        // empty square, so add and continue
        squares.add(new Tuple<>(squareRow, squareCol));
        continue;
      }

      // ran into a piece, end
      squares.add(new Tuple<>(squareRow, squareCol));
      break;
    }

    // NORTH
    for (int offset=1; offset<=NUM_COLS; offset++) {
      squareRow = row+offset;
      squareCol = col;
      if (!inBounds(squareRow, squareCol)) {break;} // out of bounds
      if (!containsPiece(squareRow, squareCol)) {
        // empty square, so add and continue
        squares.add(new Tuple<>(squareRow, squareCol));
        continue;
      }

      // ran into a piece, end
      squares.add(new Tuple<>(squareRow, squareCol));
      break;
    }

    // SOUTH
    for (int offset=1; offset<=NUM_COLS; offset++) {
      squareRow = row-offset;
      squareCol = col;
      if (!inBounds(squareRow, squareCol)) {break;} // out of bounds
      if (!containsPiece(squareRow, squareCol)) {
        // empty square, so add and continue
        squares.add(new Tuple<>(squareRow, squareCol));
        continue;
      }
      // ran into a piece, end
      squares.add(new Tuple<>(squareRow, squareCol));
      break;
    }

    // WEST
    for (int offset=1; offset<=NUM_COLS; offset++) {
      squareRow = row;
      squareCol = col-offset;
      if (!inBounds(squareRow, squareCol)) {break;} // out of bounds
      if (!containsPiece(squareRow, squareCol)) {
        // empty square, so add and continue
        squares.add(new Tuple<>(squareRow, squareCol));
        continue;
      }
      // ran into a piece, end
      squares.add(new Tuple<>(squareRow, squareCol));
      break;
    }

    return squares;
  }

  /**
   * Obtain the set of squares that a bishop of a certain color would threaten if placed at a particular location
   * @param row row at which the bishop is placed
   * @param col column at which the bishop is placed
   * @param color color of the bishop
   */
  private Set<Tuple<Integer, Integer>> threatenedSquaresBishop(int row, int col, Color color) {
    Set<Tuple<Integer, Integer>> squares = new HashSet<Tuple<Integer, Integer>>();
    int squareRow;
    int squareCol;

    // NORTH-EAST
    for (int offset=1; offset<=NUM_COLS; offset++) {
      squareRow = row+offset;
      squareCol = col+offset;
      if (!inBounds(squareRow, squareCol)) {break;} // out of bounds
      if (!containsPiece(squareRow, squareCol)) {
        // empty square, so add and continue
        squares.add(new Tuple<>(squareRow, squareCol));
        continue;
      }
      // ran into a piece, end
      squares.add(new Tuple<>(squareRow, squareCol));
      break;
    }

    // NORTH-WEST
    for (int offset=1; offset<=NUM_COLS; offset++) {
      squareRow = row+offset;
      squareCol = col-offset;
      if (!inBounds(squareRow, squareCol)) {break;} // out of bounds
      if (!containsPiece(squareRow, squareCol)) {
        // empty square, so add and continue
        squares.add(new Tuple<>(squareRow, squareCol));
        continue;
      }
      // ran into a piece, end
      squares.add(new Tuple<>(squareRow, squareCol));
      break;
    }

    // SOUTH-WEST
    for (int offset=1; offset<=NUM_COLS; offset++) {
      squareRow = row-offset;
      squareCol = col-offset;
      if (!inBounds(squareRow, squareCol)) {break;} // out of bounds
      if (!containsPiece(squareRow, squareCol)) {
        // empty square, so add and continue
        squares.add(new Tuple<>(squareRow, squareCol));
        continue;
      }
      // ran into a piece, end
      squares.add(new Tuple<>(squareRow, squareCol));
      break;
    }

    // SOUTH-EAST
    for (int offset=1; offset<=NUM_COLS; offset++) {
      squareRow = row-offset;
      squareCol = col+offset;
      if (!inBounds(squareRow, squareCol)) {break;} // out of bounds
      if (!containsPiece(squareRow, squareCol)) {
        // empty square, so add and continue
        squares.add(new Tuple<>(squareRow, squareCol));
        continue;
      }
      // ran into a piece, end
      squares.add(new Tuple<>(squareRow, squareCol));
      break;
    }

    return squares;
  }

  /**
   * Obtain the set of squares that a knight of a certain color would threaten if placed at a particular location
   * @param row row at which the knight is placed
   * @param col column at which the knight is placed
   * @param color color of the knight
   */
  private Set<Tuple<Integer, Integer>> threatenedSquaresKnight(int row, int col, Color color) {
    Set<Tuple<Integer, Integer>> squares = new HashSet<Tuple<Integer, Integer>>();

    // check all 8 combinations of knight jumps
  
    int xOffset;
    int yOffset;
    int squareRow;
    int squareCol;

    xOffset = 1;
    yOffset = 2;
    squareRow = row+yOffset;
    squareCol = col+xOffset;
    if (inBounds(squareRow, squareCol)) { // check if in bounds
      squares.add(new Tuple<>(squareRow, squareCol));
    }

    xOffset = -1;
    yOffset = 2;
    squareRow = row+yOffset;
    squareCol = col+xOffset;
    if (inBounds(squareRow, squareCol)) { // check if in bounds
      squares.add(new Tuple<>(squareRow, squareCol));
    }

    xOffset = 1;
    yOffset = -2;
    squareRow = row+yOffset;
    squareCol = col+xOffset;
    if (inBounds(squareRow, squareCol)) { // check if in bounds
      squares.add(new Tuple<>(squareRow, squareCol));
    }

    xOffset = -1;
    yOffset = -2;
    squareRow = row+yOffset;
    squareCol = col+xOffset;
    if (inBounds(squareRow, squareCol)) { // check if in bounds
      squares.add(new Tuple<>(squareRow, squareCol));
    }

    xOffset = 2;
    yOffset = 1;
    squareRow = row+yOffset;
    squareCol = col+xOffset;
    if (inBounds(squareRow, squareCol)) { // check if in bounds
      squares.add(new Tuple<>(squareRow, squareCol));
    }

    xOffset = -2;
    yOffset = 1;
    squareRow = row+yOffset;
    squareCol = col+xOffset;
    if (inBounds(squareRow, squareCol)) { // check if in bounds
      squares.add(new Tuple<>(squareRow, squareCol));
    }

    xOffset = 2;
    yOffset = -1;
    squareRow = row+yOffset;
    squareCol = col+xOffset;
    if (inBounds(squareRow, squareCol)) { // check if in bounds
      squares.add(new Tuple<>(squareRow, squareCol));
    }

    xOffset = -2;
    yOffset = -1;
    squareRow = row+yOffset;
    squareCol = col+xOffset;
    if (inBounds(squareRow, squareCol)) { // check if in bounds
      squares.add(new Tuple<>(squareRow, squareCol));
    }

    return squares;
  }

  /**
   * Get the total number of moves played on this board
   * @return the number of moves played on this board
   */
  public int getNumMoves() {
    return this.moveList.size();
  }

  /**
   * Whether or not white performed a castle move
   * @return if white ever made a castling move
   */
  public boolean whiteCastled() {
    for (Move move : this.moveList) {
      if (move.isCastleMove() && move.getEndRow() == 0) {return true;}
    }
    return false;
  }

  /**
   * Whether or not black performed a castle move
   * @return if black ever made a castling move
   */
  public boolean blackCastled() {
    for (Move move : this.moveList) {
      if (move.isCastleMove() && move.getEndRow() == 7) {return true;}
    }
    return false;
  }

  /**
   * Check if this position is checkmate
   */
  public boolean checkmate() {
    return (inCheck() && legalMoves().size()==0);
  }

  /**
   * Get the opposite color 
   *  - BLACK to WHITE
   *  - WHITE to BLACK
   * @param color the color to find the opposite of 
   * @return the color opposite to color
   */
  private Color oppositeColor(Color color) {
    if (color.equals(Color.WHITE)) {
      return Color.BLACK;
    } else {
      return Color.WHITE;
    }
  }

  /**
   * Obtain the location of a particular king on this board
   * @param color the color of the king to find
   * @return the location of the king with color color
   */
  private Tuple<Integer, Integer> getKingPos (Color color) {
    for (int row=0; row<NUM_ROWS; row++) {
      for (int col=0; col<NUM_COLS; col++) {
        // skip if the square doesn't contain a piece
        if (!containsPiece(row,col)) {continue;}
        Piece piece = getPiece(row,col);
        // skip if the piece isn't a king
        if (!piece.getType().equals(PieceType.KING)) {continue;}

        // check for the color of the king
        if (piece.getColor().equals(color)) {
          return new Tuple<>(row,col);
        } else {
          continue;
        }
      }
    }

    System.err.println(this);
    throw new Error("Couldn't find a king - THERE SHOULD ALWAYS BE A KING");
  }

  /**
   * Check if the king is in check in current position
   * @return true iff the king is in check, else false
   */
  public boolean inCheck () {
    // get squares under control by opposite team
    Set<Tuple<Integer, Integer>> threatSquares = threatenedSquares();
    // in check if king is being threatened by opponent
    return threatSquares.contains(getKingPos(getTurn()));
  }

  /**
   * Check if a square is in boundary of board
   * @param row row of query square
   * @param col column of query square
   * @return true iff the square is in bounds of board
   */
  private boolean inBounds(int row, int col) {
    return (row >= 0 && row <= 7 && col >= 0 && col <= 7);
  }
}

