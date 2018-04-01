package src.datatypes;

/**
 * Represents an immutable chess move
 */
public class Move {
  // type of piece that is being moved
  private final PieceType pieceType;
  // row of start square of piece
  private final int start_row;
  // column of start square of piece
  private final int start_col;
  // row of end square of piece
  private final int end_row;
  // column of end square of piece
  private final int end_col;

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
  }

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
   * Obtain the chess coordinate equivalent of a square
   * @param row 0-indexed row of square
   * @param col 0-indexed column of square
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
    String rep = String.format("%s%s->%s", piece, startSquare, endSquare);
    return rep;
  }

  @Override
  public int hashCode() {
    int pieceHash = this.getPieceType().hashCode();
    int startRowHash = this.getStartRow();
    int startColHash = this.getStartCol();
    int endRowHash = this.getEndRow();
    int endColHash = this.getEndCol();
    return pieceHash^(startRowHash+(startColHash*endRowHash^endColHash));
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
    return samePieceType && sameStartRow && sameStartCol && sameEndRow && sameEndCol;
  }
}
