package src.datatypes;

/**
 * Represents a Bishop
 */
public class Bishop implements Piece {
  // whether or not this piece has moved
  private boolean hasMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new Bishop of a given color
   * @param color color of the bishop
   */
  public Bishop(Color color) {
    this.color = color; 
    this.hasMoved = false;
    this.type = PieceType.BISHOP;
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
      return "B";
    } else {
      return "b";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new Bishop(this.getColor());
    if (this.hasMoved()) {
      newPiece.setMoved();
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Bishop)) {return false;}
    Bishop otherBishop = (Bishop) other;
    boolean sameColor = this.getColor().equals(otherBishop.getColor());
    boolean sameMoved = this.hasMoved() == otherBishop.hasMoved();
    boolean sameType = this.getType().equals(otherBishop.getType());

    return sameColor && sameMoved && sameType;
  }
}
