public class Tuple <X,Y> {
  // first tuple value
  private final X x;
  // second tuple value
  private final Y y;

  /**
   * Create a new Tuple object
   * @param x x object
   * @param y y object
   */
  public Tuple(X x, Y y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Get the first element of this Tuple
   * @return first element of tuple
   */
  public X x() {
    return this.x;
  }

  /**
   * Get the second element of this Tuple
   * @return second element of tuple
   */
  public Y y() {
    return this.y;
  }

  @Override
  public String toString() {
    return String.format("(%s, %s)", x(), y());
  }
}
