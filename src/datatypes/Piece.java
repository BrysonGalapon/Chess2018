package src.datatypes;

/**
 * Represents a chess piece
 */
public interface Piece {
  /*
   * Piece := Pawn | Rook | Knight | Bishop | Queen | King
   */

  /**
   * Obtain a new piece of type pieceType and Color color
   * @param pieceType the type of piece to create
   * @param color the color of the piece to create
   * @return a piece of type pieceType and color color
   */
  public static Piece newPiece(PieceType pieceType, Color color) {
    switch(pieceType) {
      case PAWN:
        return pawn(color);

      case ROOK:
        return rook(color);

      case KNIGHT:
        return knight(color);

      case BISHOP:
        return bishop(color);

      case QUEEN:
        return queen(color);

      case KING:
        return king(color);

      default:
        throw new Error("Unexpected PieceType input");
    }
  }

  /**
   * Obtain a new pawn
   * @param color the color of the piece to create
   * @return a pawn of color color
   */
  public static Piece pawn(Color color) {
    return new Pawn(color);
  }

  /**
   * Obtain a new rook
   * @param color the color of the piece to create
   * @return a rook of color color
   */
  public static Piece rook(Color color) {
    return new Rook(color);
  }

  /**
   * Obtain a new knight
   * @param color the color of the piece to create
   * @return a knight of color color
   */
  public static Piece knight(Color color) {
    return new Knight(color);
  }

  /**
   * Obtain a new bishop
   * @param color the color of the piece to create
   * @return a bishop of color color
   */
  public static Piece bishop(Color color) {
    return new Bishop(color);
  }

  /**
   * Obtain a new queen
   * @param color the color of the piece to create
   * @return a queen of color color
   */
  public static Piece queen(Color color) {
    return new Queen(color);
  }

  /**
   * Obtain a new king
   * @param color the color of the piece to create
   * @return a king of color color
   */
  public static Piece king(Color color) {
    return new King(color);
  }

  /**
   * Check if this piece has moved
   * @return true if this piece has moved, o.w. false
   */
  public boolean hasMoved();

  /**
   * Indicate to this piece that it has moved
   */
  public void indicateMoved();

  /**
   * Indicate to this piece that it has been moved back
   */
  public void indicateBackward();

  /**
   * Obtain the number of times this piece has moved
   * @return the number of times this piece has moved
   */
  public int numTimesMoved();

  /**
   * Obtain the color of this piece
   * @return the color of this piece
   */
  public Color getColor();

  /**
   * Obtain the type of piece this is
   * @return the type of this piece
   */
  public PieceType getType();

  /**
   * Obtain a copy of this piece
   * @return a new copy of this piece
   */
  public Piece copy();
}
