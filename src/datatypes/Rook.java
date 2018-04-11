package src.datatypes;

/**
 * Represents a Rook
 */
public class Rook implements Piece {
  // number of times this piece has moved
  private int numMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new Rook of a given color
   * @param color color of the Rook
   */
  public Rook(Color color) {
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
      return "R";
    } else {
      return "r";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new Rook(this.getColor());
    if (this.hasMoved()) {
      for (int i=0; i < this.numTimesMoved(); i++) {
        newPiece.indicateMoved();
      }
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Rook)) {return false;}
    Rook otherRook = (Rook) other;
    boolean sameColor = this.getColor().equals(otherRook.getColor());
    boolean sameMoved = this.numTimesMoved() == otherRook.numTimesMoved();
    boolean sameType = this.getType().equals(otherRook.getType());

    return sameColor && sameMoved && sameType;
  }
}
