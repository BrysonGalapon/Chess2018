package src.datatypes;

/**
 * Represents a Pawn
 */
public class Pawn implements Piece {
  // number of times this piece has moved
  private int numMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new Pawn of a given color
   * @param color color of the Pawn
   */
  public Pawn(Color color) {
    this.color = color; 
    this.numMoved = 0;
    this.type = PieceType.PAWN;
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
      return "P";
    } else {
      return "p";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new Pawn(this.getColor());
    if (this.hasMoved()) {
      for (int i=0; i < this.numTimesMoved(); i++) {
        newPiece.indicateMoved();
      }
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Pawn)) {return false;}
    Pawn otherPawn = (Pawn) other;
    boolean sameColor = this.getColor().equals(otherPawn.getColor());
    boolean sameMoved = this.numTimesMoved() == otherPawn.numTimesMoved();
    boolean sameType = this.getType().equals(otherPawn.getType());

    return sameColor && sameMoved && sameType;
  }
}
