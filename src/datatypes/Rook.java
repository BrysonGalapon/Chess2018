package src.datatypes;

/**
 * Represents a Rook
 */
public class Rook implements Piece {
  // whether or not this piece has moved
  private boolean hasMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new Rook of a given color
   * @param color color of the rook
   */
  public Rook(Color color) {
    this.color = color; 
    this.hasMoved = false;
    this.type = PieceType.ROOK;
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
      return "R";
    } else {
      return "r";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new Rook(this.getColor());
    if (this.hasMoved()) {
      newPiece.setMoved();
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Rook)) {return false;}
    Rook otherRook = (Rook) other;
    boolean sameColor = this.getColor().equals(otherRook.getColor());
    boolean sameMoved = this.hasMoved() == otherRook.hasMoved();
    boolean sameType = this.getType().equals(otherRook.getType());

    return sameColor && sameMoved && sameType;
  }
}
