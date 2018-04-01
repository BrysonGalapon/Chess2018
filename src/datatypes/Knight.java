package src.datatypes;

/**
 * Represents a Knight
 */
public class Knight implements Piece {
  // whether or not this piece has moved
  private boolean hasMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new Knight of a given color
   * @param color color of the knight
   */
  public Knight(Color color) {
    this.color = color; 
    this.hasMoved = false;
    this.type = PieceType.KNIGHT;
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
      return "N";
    } else {
      return "n";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new Knight(this.getColor());
    if (this.hasMoved()) {
      newPiece.setMoved();
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Knight)) {return false;}
    Knight otherKnight = (Knight) other;
    boolean sameColor = this.getColor().equals(otherKnight.getColor());
    boolean sameMoved = this.hasMoved() == otherKnight.hasMoved();
    boolean sameType = this.getType().equals(otherKnight.getType());

    return sameColor && sameMoved && sameType;
  }
}
