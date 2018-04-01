package src.datatypes;

/**
 * Represents a Queen
 */
public class Queen implements Piece {
  // whether or not this piece has moved
  private boolean hasMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new Queen of a given color
   * @param color color of the queen
   */
  public Queen(Color color) {
    this.color = color; 
    this.hasMoved = false;
    this.type = PieceType.QUEEN;
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
      return "Q";
    } else {
      return "q";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new Queen(this.getColor());
    if (this.hasMoved()) {
      newPiece.setMoved();
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Queen)) {return false;}
    Queen otherQueen = (Queen) other;
    boolean sameColor = this.getColor().equals(otherQueen.getColor());
    boolean sameMoved = this.hasMoved() == otherQueen.hasMoved();
    boolean sameType = this.getType().equals(otherQueen.getType());

    return sameColor && sameMoved && sameType;
  }
}
