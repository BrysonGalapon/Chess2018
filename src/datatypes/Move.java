package src.datatypes;

/**
 * Represents an immutable chess move
 */
public class Move {
  // type of piece that is being moved
  private final PieceType pieceType;
  // whether or not this move captures a piece
  private final boolean isCapture;
  // piece that piecetype promotes to, if this move is a promotion
  private final PieceType promotion;
  // row of start square of piece
  private final int start_row;
  // column of start square of piece
  private final int start_col;
  // row of end square of piece
  private final int end_row;
  // column of end square of piece
  private final int end_col;

  /////////////////////////////////////////////////////
  //       CONSTRUCTORS (There's alot of them)       //
  /////////////////////////////////////////////////////

  /**
   * Create a new chess Move
   * @param pieceType the type of piece to be moved
   * @param start_row the 0-indexed row at which the piece starts at
   * @param start_col the 0-indexed column at which the piece starts at 
   * @param end_row the 0-indexed row at which the piece ends up at
   * @param end_col the 0-indexed column at which the piece ends up at
   */
  public Move(PieceType pieceType, int start_row, int start_col, int end_row, int end_col) {
    this.pieceType = pieceType;
    this.start_row = start_row;
    this.start_col = start_col;
    this.end_row = end_row;
    this.end_col = end_col;
    this.isCapture = false;
    this.promotion = null;
  }

  /**
   * Create a new chess Move
   * @param pieceType the type of piece to be moved
   * @param start_row the 0-indexed row at which the piece starts at
   * @param start_col the 0-indexed column at which the piece starts at 
   * @param end_row the 0-indexed row at which the piece ends up at
   * @param end_col the 0-indexed column at which the piece ends up at
   * @param isCapture whether or not this move captures a piece
   */
  public Move(PieceType pieceType, int start_row, int start_col, int end_row, int end_col, boolean isCapture) {
    this.pieceType = pieceType;
    this.start_row = start_row;
    this.start_col = start_col;
    this.end_row = end_row;
    this.end_col = end_col;
    this.isCapture = isCapture;
    this.promotion = null;
  }

/**
   * Create a new chess Move
   * @param pieceType the type of piece to be moved
   * @param start_row the 0-indexed row at which the piece starts at
   * @param start_col the 0-indexed column at which the piece starts at 
   * @param end_row the 0-indexed row at which the piece ends up at
   * @param end_col the 0-indexed column at which the piece ends up at
   * @param isCapture whether or not this move captures a piece
   * @param promotion piece to promote to
   */
  public Move(PieceType pieceType, int start_row, int start_col, int end_row, int end_col, boolean isCapture, PieceType promotion) {
    this.pieceType = pieceType;
    this.start_row = start_row;
    this.start_col = start_col;
    this.end_row = end_row;
    this.end_col = end_col;
    this.isCapture = isCapture;
    this.promotion = promotion;
  }

  /**
   * Create a new chess Move using chess coordinates (ie 'a1' or 'c7')
   * @param pieceType the type of piece to be moved
   * @param startSquare the square in chess coordinates at which the piece starts at
   * @param endSquare the square in chess coordinates at which the piece ends at
   * @throws Error if either startSquare or endSquare is an invalid coordinate
   */
  public Move(PieceType pieceType, String startSquare, String endSquare) {
    if (startSquare.length() != 2 || endSquare.length() != 2) {throw new Error("startSquare or endSquare isn't 2 chars long");}

    char startChar1 = startSquare.charAt(0);
    char startChar2 = startSquare.charAt(1);
    char endChar1 = endSquare.charAt(0);
    char endChar2 = endSquare.charAt(1);

    this.pieceType = pieceType;
    this.start_row = this.rankToRow(startChar2);
    this.start_col = this.fileToCol(startChar1);
    this.end_row = this.rankToRow(endChar2);
    this.end_col = this.fileToCol(endChar1);
    this.isCapture = false;
    this.promotion = null;
  }

  /**
   * Create a new chess Move using chess coordinates (ie 'a1' or 'c7')
   * @param pieceType the type of piece to be moved
   * @param startSquare the square in chess coordinates at which the piece starts at
   * @param endSquare the square in chess coordinates at which the piece ends at
   * @param isCapture whether or not this move captures a piece
   * @throws Error if either startSquare or endSquare is an invalid coordinate
   */
  public Move(PieceType pieceType, String startSquare, String endSquare, boolean isCapture) {
    if (startSquare.length() != 2 || endSquare.length() != 2) {throw new Error("startSquare or endSquare isn't 2 chars long");}

    char startChar1 = startSquare.charAt(0);
    char startChar2 = startSquare.charAt(1);
    char endChar1 = endSquare.charAt(0);
    char endChar2 = endSquare.charAt(1);

    this.pieceType = pieceType;
    this.start_row = this.rankToRow(startChar2);
    this.start_col = this.fileToCol(startChar1);
    this.end_row = this.rankToRow(endChar2);
    this.end_col = this.fileToCol(endChar1);
    this.isCapture = isCapture;
    this.promotion = null;
  }

  /**
   * Create a new chess Move using chess coordinates (ie 'a1' or 'c7')
   * @param pieceType the type of piece to be moved
   * @param startSquare the square in chess coordinates at which the piece starts at
   * @param endSquare the square in chess coordinates at which the piece ends at
   * @param isCapture whether or not this move captures a piece
   * @param promotion piece that pieceType promotes to
   * @throws Error if either startSquare or endSquare is an invalid coordinate
   */
  public Move(PieceType pieceType, String startSquare, String endSquare, boolean isCapture, PieceType promotion) {
    if (startSquare.length() != 2 || endSquare.length() != 2) {throw new Error("startSquare or endSquare isn't 2 chars long");}

    char startChar1 = startSquare.charAt(0);
    char startChar2 = startSquare.charAt(1);
    char endChar1 = endSquare.charAt(0);
    char endChar2 = endSquare.charAt(1);

    this.pieceType = pieceType;
    this.start_row = this.rankToRow(startChar2);
    this.start_col = this.fileToCol(startChar1);
    this.end_row = this.rankToRow(endChar2);
    this.end_col = this.fileToCol(endChar1);
    this.isCapture = isCapture;
    this.promotion = promotion;
  }

  /////////////////////////////////////////////////////
  //                    METHODS                      //
  /////////////////////////////////////////////////////

  /**
   * Obtain the type of piece that is being moved
   * @return the type of piece being moved
   */
  public PieceType getPieceType() {
    return this.pieceType;
  }

  /**
   * Obtain the 0-indexed row at which the moved piece started at
   * @return the 0-indexed row at which the piece starts at
   */
  public int getStartRow() {
    return this.start_row;
  }

  /**
   * Check if this move captures a piece
   * @return whether or not this move captures a piece
   */
  public boolean isCapture() {
    return this.isCapture;
  }

  /**
   * Check if this move promotes a piece
   * @return whether or not this move promotes a piece
   */
  public boolean isPromotion() {
    return (this.promotion != null);
  }

  /**
   * Obtain the piece that the piece being moved promotes into
   * @return the piece that the piece being moved promotes into.
   *          if this move isn't a promotion, returns null
   */
  public PieceType getPromotion() {
    return this.promotion;
  }

  /**
   * Check if this move is a castling move
   */
  public boolean isCastleMove() {
    boolean pieceIsKing = this.getPieceType().equals(PieceType.KING);
    if (!pieceIsKing) {
      return false;
    }

    String startSquare = this.chessCoord(this.getStartRow(), this.getStartCol());
    String endSquare = this.chessCoord(this.getEndRow(), this.getEndCol());

    // run through all 4 types of castling moves
    if (startSquare.equals("e1") && endSquare.equals("g1")) {
      // white castles kingside
      return true;
    } else if (startSquare.equals("e1") && endSquare.equals("c1")) {
      // white castles queenside
      return true;
    } else if (startSquare.equals("e8") && endSquare.equals("g8")) {
      // black castles kingside
      return true;
    } else if (startSquare.equals("e8") && endSquare.equals("c8")) {
      // black castles queenside
      return true;
    } else {
      return false;
    }
  }

  /**
   * Obtain the string representation of the start square of this move
   * @return the string representation of the start square of this move
   */
  public String getStartSquare() {
    return chessCoord(getStartRow(), getStartCol());
  }

  /**
   * Obtain the string representation of the start square of this move
   * @return the string representation of the start square of this move
   */
  public String getEndSquare() {
    return chessCoord(getEndRow(), getEndCol());
  }

  /**
   * Obtain the 0-indexed column at which the moved piece started at
   * @return the 0-indexed column at which the piece starts at
   */
  public int getStartCol() {
    return this.start_col;
  }

  /**
   * Obtain the 0-indexed row at which the moved piece ended at
   * @return the 0-indexed row at which the piece ends at
   */
  public int getEndRow() {
    return this.end_row;
  }

  /**
   * Obtain the 0-indexed column at which the moved piece ended at
   * @return the 0-indexed column at which the piece ends at
   */
  public int getEndCol() {
    return this.end_col;
  }

  /**
   * Convert a chess file to a column
   * @param file the chess file
   * @return the 0-indexed column corresponding to the file
   * @throws Error if the chess file isn't a valid file
   */
  private int fileToCol(char file) {
    switch(file) {
      case 'a':
        return 0;
      case 'b':
        return 1;
      case 'c':
        return 2;
      case 'd':
        return 3;
      case 'e':
        return 4;
      case 'f':
        return 5;
      case 'g':
        return 6;
      case 'h':
        return 7;
      default:
        throw new Error(String.format("Invalid file: %s ", file));
    }
  }

  /**
   * Convert a chess rank to a row
   * @param rank the chess rank
   * @return the 0-indexed row corresponding to the rank
   * @throws Error if the chess rank isn't a valid rank
   */
  private int rankToRow(char rank) {
    switch(rank) {
      case '1':
        return 0;
      case '2':
        return 1;
      case '3':
        return 2;
      case '4':
        return 3;
      case '5':
        return 4;
      case '6':
        return 5;
      case '7':
        return 6;
      case '8':
        return 7;
      default:
        throw new Error(String.format("Invalid rank: %s ", rank));
    }
  }

  /**
   * Obtain the chess coordinate equivalent of a square
   * @param row 0-indexed row of square
   * @param col 0-indexed column of square
   * @return a string composed of two characters: f+r, where
   *          f is the file of the square and r is the rank 
   * @throws Error if (row, col) is not a valid square 
   */
  private String chessCoord(int row, int col) {
    String file;
    String rank;

    // set the file
    switch(col) {
      case 0:
        file = "a";
        break;
      case 1:
        file = "b";
        break;
      case 2:
        file = "c";
        break;
      case 3:
        file = "d";
        break;
      case 4:
        file = "e";
        break;
      case 5:
        file = "f";
        break;
      case 6:
        file = "g";
        break;
      case 7:
        file = "h";
        break;
      default:
        throw new Error(String.format("Unexpected column value: %s", col));
    }

    // set the rank
    switch(row) {
      case 0:
        rank = "1";
        break;
      case 1:
        rank = "2";
        break;
      case 2:
        rank = "3";
        break;
      case 3:
        rank = "4";
        break;
      case 4:
        rank = "5";
        break;
      case 5:
        rank = "6";
        break;
      case 6:
        rank = "7";
        break;
      case 7:
        rank = "8";
        break;
      default:
        throw new Error(String.format("Unexpected row value: %s", row));
    }
    return file+rank;
  }

  @Override
  public String toString() {
    String piece;
    switch(this.getPieceType()) {
      case PAWN:
        piece = "P";
        break;
      case ROOK:
        piece = "R";
        break;
      case KNIGHT:
        piece = "N";
        break;
      case BISHOP:
        piece = "B";
        break;
      case QUEEN:
        piece = "Q";
        break;
      case KING:
        piece = "K";
        break;
      default:
        throw new Error(String.format("Unexpected piece type: %s", this.getPieceType()));
    }
    String startSquare = this.chessCoord(this.getStartRow(), this.getStartCol());
    String endSquare = this.chessCoord(this.getEndRow(), this.getEndCol());
    String rep;
    if (isPromotion()) {
      rep = String.format("%s%s->%s:%s", piece, startSquare, endSquare, promotion);
    } else {
      rep = String.format("%s%s->%s", piece, startSquare, endSquare);
    }
    return rep;
  }

  @Override
  public int hashCode() {
    int pieceHash = this.getPieceType().hashCode();
    int startRowHash = this.getStartRow();
    int startColHash = this.getStartCol();
    int endRowHash = this.getEndRow();
    int endColHash = this.getEndCol();
    int capture = this.isCapture() ? 12 : 82;
    int promotion = (this.getPromotion() != null) ? this.getPromotion().hashCode() : 7;
    return pieceHash^(startRowHash+(startColHash*endRowHash^endColHash))+capture^promotion;
  }

  @Override
  public boolean equals(Object other) {
    if(!(other instanceof Move)) {return false;}
    Move otherMove = (Move) other;
    boolean samePieceType = this.getPieceType().equals(otherMove.getPieceType());
    boolean sameStartRow = this.getStartRow() == otherMove.getStartRow();
    boolean sameStartCol = this.getStartCol() == otherMove.getStartCol();
    boolean sameEndRow = this.getEndRow() == otherMove.getEndRow();
    boolean sameEndCol = this.getEndCol() == otherMove.getEndCol();
    boolean sameCaptures = this.isCapture() == otherMove.isCapture();
    boolean samePromotion = this.getPromotion() == otherMove.getPromotion();
    return samePieceType && sameStartRow && sameStartCol && sameEndRow && sameEndCol && sameCaptures && samePromotion;
  }
}
