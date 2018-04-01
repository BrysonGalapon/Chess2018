package src.datatypes;

/**
 * Represents a King
 */
public class King implements Piece {
  // whether or not this piece has moved
  private boolean hasMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new King of a given color
   * @param color color of the king
   */
  public King(Color color) {
    this.color = color; 
    this.hasMoved = false;
    this.type = PieceType.KING;
  }

  @Override
  public boolean hasMoved() {
    return this.hasMoved;
  }

  @Override
  public void setMoved() {
    this.hasMoved = true;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public PieceType getType() {
    return this.type;
  }

  @Override
  public String toString() {
    if (this.getColor().equals(Color.WHITE)) {
      return "K";
    } else {
      return "k";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new King(this.getColor());
    if (this.hasMoved()) {
      newPiece.setMoved();
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof King)) {return false;}
    King otherKing = (King) other;
    boolean sameColor = this.getColor().equals(otherKing.getColor());
    boolean sameMoved = this.hasMoved() == otherKing.hasMoved();
    boolean sameType = this.getType().equals(otherKing.getType());

    return sameColor && sameMoved && sameType;
  }
}
