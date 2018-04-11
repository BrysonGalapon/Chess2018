package src.datatypes;

/**
 * Represents a King
 */
public class King implements Piece {
  // number of times this piece has moved
  private int numMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new King of a given color
   * @param color color of the King
   */
  public King(Color color) {
    this.color = color; 
    this.numMoved = 0;
    this.type = PieceType.KING;
  }

  @Override
  public boolean hasMoved() {
    return numMoved != 0;
  }

  @Override
  public void indicateMoved() {
    numMoved++;
  }

  @Override
  public void indicateBackward() {
    numMoved--;
  }

  @Override
  public int numTimesMoved() {
    return numMoved;
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
      for (int i=0; i < this.numTimesMoved(); i++) {
        newPiece.indicateMoved();
      }
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof King)) {return false;}
    King otherKing = (King) other;
    boolean sameColor = this.getColor().equals(otherKing.getColor());
    boolean sameMoved = this.numTimesMoved() == otherKing.numTimesMoved();
    boolean sameType = this.getType().equals(otherKing.getType());

    return sameColor && sameMoved && sameType;
  }
}
