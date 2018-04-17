package src.datatypes;

/**
 * Represents a Queen
 */
public class Queen implements Piece {
  // number of times this piece has moved
  private int numMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new Queen of a given color
   * @param color color of the Queen
   */
  public Queen(Color color) {
    this.color = color; 
    this.numMoved = 0;
    this.type = PieceType.QUEEN;
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
      return "Q";
    } else {
      return "q";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new Queen(this.getColor());
    if (this.hasMoved()) {
      for (int i=0; i < this.numTimesMoved(); i++) {
        newPiece.indicateMoved();
      }
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Queen)) {return false;}
    Queen otherQueen = (Queen) other;
    boolean sameColor = this.getColor().equals(otherQueen.getColor());
    boolean sameMoved = this.numTimesMoved() == otherQueen.numTimesMoved();
    boolean sameType = this.getType().equals(otherQueen.getType());

    return sameColor && sameMoved && sameType;
  }
}
