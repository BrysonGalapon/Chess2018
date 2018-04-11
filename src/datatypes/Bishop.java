package src.datatypes;

/**
 * Represents a Bishop
 */
public class Bishop implements Piece {
  // number of times this piece has moved
  private int numMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new Bishop of a given color
   * @param color color of the Bishop
   */
  public Bishop(Color color) {
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
      return "B";
    } else {
      return "b";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new Bishop(this.getColor());
    if (this.hasMoved()) {
      for (int i=0; i < this.numTimesMoved(); i++) {
        newPiece.indicateMoved();
      }
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Bishop)) {return false;}
    Bishop otherBishop = (Bishop) other;
    boolean sameColor = this.getColor().equals(otherBishop.getColor());
    boolean sameMoved = this.numTimesMoved() == otherBishop.numTimesMoved();
    boolean sameType = this.getType().equals(otherBishop.getType());

    return sameColor && sameMoved && sameType;
  }
}
