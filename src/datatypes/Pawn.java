package src.datatypes;

/**
 * Represents a Pawn
 */
public class Pawn implements Piece {
  // whether or not this piece has moved
  private boolean hasMoved;
  // color of this piece
  private final Color color;
  // type of piece this is
  private final PieceType type;

  /**
   * Create a new Pawn of a given color
   * @param color color of the pawn
   */
  public Pawn(Color color) {
    this.color = color; 
    this.hasMoved = false;
    this.type = PieceType.PAWN;
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
      return "P";
    } else {
      return "p";
    }
  }

  @Override
  public Piece copy() {
    Piece newPiece = new Pawn(this.getColor());
    if (this.hasMoved()) {
      newPiece.setMoved();
    }
    return newPiece;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Pawn)) {return false;}
    Pawn otherPawn = (Pawn) other;
    boolean sameColor = this.getColor().equals(otherPawn.getColor());
    boolean sameMoved = this.hasMoved() == otherPawn.hasMoved();
    boolean sameType = this.getType().equals(otherPawn.getType());

    return sameColor && sameMoved && sameType;
  }
}
