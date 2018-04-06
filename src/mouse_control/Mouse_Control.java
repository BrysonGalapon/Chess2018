package src.mouse_control;

import src.datatypes.*;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.*;
import java.util.*;

public class Mouse_Control {
  // largest x coordinate on screen
  private final int MAX_X_COORD = 1600;
  // largest y coordinate on screen
  private final int MAX_Y_COORD = 860;
  // waiting time in ms between calibration point captures
  private final int CAL_WAIT_TIME_MS = 2000;
  // percent difference tolerance between board length and height
  private final double EPSILON = 0.05;

  // length of time between moves in ms
  private int move_time_ms = 1000;
  // length in pixels of a square
  private final int length_square;
  // mouse controller
  private final Robot robot;
  // horizontal offset from screen (0,0) to top left corner of board
  private final int xOffset;
  // vertical offset from screen (0,0) to top left corner of board
  private final int yOffset;
  // top left x coordinate of board
  private final int top_left_x;
  // top left y coordinate of board
  private final int top_left_y;
  // bottom right x coordinate of board
  private final int bottom_right_x;
  // bottom right y coordinate of board
  private final int bottom_right_y;

  /**
   * Create a new Mouse_Control object to interface with browser
   *  - Performs calibration to calculate square coordinates
   *  @throws InterruptedException if Thread gets interrupted
   *  @throws AWTException if the mouse hardware excepts
   */
  public Mouse_Control() throws InterruptedException, AWTException{
    this.robot = new Robot();

    System.out.println("Calibrating started...");

    // capture top-left point
    Thread.sleep(1000); // wait a second
    System.out.println("Capturing point in 3...");
    Thread.sleep(1000); // wait a second
    System.out.println("2...");
    Thread.sleep(1000); // wait a second
    System.out.println("1...");
    Thread.sleep(1000); // wait a second
    this.top_left_x = MouseInfo.getPointerInfo().getLocation().x;
    this.top_left_y = MouseInfo.getPointerInfo().getLocation().y;

    System.out.println(String.format("Captured: (%d,%d)", top_left_x, top_left_y));

    Thread.sleep(CAL_WAIT_TIME_MS); // wait until next point capture

    // capture bottom-right point
    System.out.println("Capturing point in 3...");
    Thread.sleep(1000); // wait a second
    System.out.println("2...");
    Thread.sleep(1000); // wait a second
    System.out.println("1...");
    Thread.sleep(1000); // wait a second
    this.bottom_right_x = MouseInfo.getPointerInfo().getLocation().x;
    this.bottom_right_y = MouseInfo.getPointerInfo().getLocation().y;

    System.out.println(String.format("Captured: (%d,%d)", bottom_right_x, bottom_right_y));

    // check if points collected form a square-ish shape
    int xLength = bottom_right_x-top_left_x;
    int yLength = bottom_right_y-top_left_y;
    if (Math.abs(1.0*xLength/yLength - 1) > EPSILON) {
      throw new Error("Calibration points do not form roughly a square");
    }

    this.length_square = xLength/8;
    this.xOffset = top_left_x+this.length_square/2;
    this.yOffset = top_left_y+this.length_square/2;
  }

  /**
   * Makes a move on the browser
   * @param move move to send to browser
   */
  public void makeMove(Move move) throws InterruptedException {
    // move to initial square and click to pick up piece
    moveMouseTo(move.getStartCol(), move.getStartRow());
    click();

    // let chess gui catch up
    Thread.sleep(move_time_ms);

    // move to end square and click to drop piece
    moveMouseTo(move.getEndCol(), move.getEndRow());
    click();
  }

  /**
   * Moves the mouse to the center of the square defined by col
   *  and row
   * @param col 0-indexed column of square to move to
   * @param row 0-indexed row of square to move to
   */
  private void moveMouseTo(int col, int row) {
      robot.mouseMove(col2X(col)+xOffset,row2Y(row)+yOffset);
  }

  /**
   * Performs a mouse click
   */
  private void click() throws InterruptedException {
      robot.mousePress(InputEvent.BUTTON1_MASK);
      Thread.sleep(1);
      robot.mouseRelease(InputEvent.BUTTON1_MASK);
  }

  /**
   * Converts a row into a pixel y-coordinate
   * @param col 0-indexed row to convert
   * @return the y-coordinate of the center of the row 
   *          corresponding to col as per the calibration
   */
  private int row2Y(int row) {
    return (7-row)*length_square;
  }

  /**
   * Converts a column into a pixel x-coordinate
   * @param col 0-indexed column to convert
   * @return the x-coordinate of the center of the column 
   *          corresponding to col as per the calibration
   */
  private int col2X(int col) {
    return col*length_square;
  }
}
