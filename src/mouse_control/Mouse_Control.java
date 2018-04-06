package src.mouse_control;

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
  private int move_time_ms = 500;
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

  public void makeMove() throws InterruptedException {
      int col = 4;
      int row = 1;
      moveMouseTo(col, row);
      click();

      // let chess gui catch up
      Thread.sleep(move_time_ms);

      col = 4;
      row = 3;
      moveMouseTo(col, row);
      click();
  }

  public void moveMouseTo(int col, int row) {
      robot.mouseMove(col2X(col)+xOffset,row2Y(row)+yOffset);
  }

  public void click() throws InterruptedException {
      robot.mousePress(InputEvent.BUTTON1_MASK);
      Thread.sleep(1);
      robot.mouseRelease(InputEvent.BUTTON1_MASK);
  }

  public int row2Y(int row) {
    return (7-row)*length_square;
  }

  public int col2X(int col) {
    return col*length_square;
  }
}
