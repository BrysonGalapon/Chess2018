package src.datatypes;

/**
 * Represents a chess piece
 */
public interface Piece {
  /**
   * Obtain a new pawn
   */
  public static Piece pawn(Color color) {
    // TODO
    return null;
  }

  /**
   * Obtain a new rook
   */
  public static Piece rook(Color color) {
    // TODO
    return null;
  }

  /**
   * Obtain a new knight
   */
  public static Piece knight(Color color) {
    // TODO
    return null;
  }

  /**
   * Obtain a new bishop
   */
  public static Piece bishop(Color color) {
    // TODO
    return null;
  }

  /**
   * Obtain a new queen
   */
  public static Piece queen(Color color) {
    // TODO
    return null;
  }

  /**
   * Obtain a new king
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
  public void setMoved();

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
