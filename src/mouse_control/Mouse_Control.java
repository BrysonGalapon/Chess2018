package src.mouse_control;

import src.datatypes.*;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Rectangle;
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
  // difference in the screenshots to signal a move
  private final int DIFF_THRESHOLD = 1000;
  // the screen for the chess board
  private final Rectangle screen;

  // length of time between moves in ms
  private int move_time_ms = 1500;
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

  // rgb screenshot of board
  private int[][] board_screen_shot;

  // different squares detected by lookMoves function
  private List<Tuple<Integer, Integer>> diffSquares = new LinkedList<Tuple<Integer, Integer>>();

  // start square of white king
  private final Tuple<Integer, Integer> whiteStartSq = new Tuple<>(0, 4);
  // start square of black king
  private final Tuple<Integer, Integer> blackStartSq = new Tuple<>(7, 4);
  // end square of white king if castling kingside
  private final Tuple<Integer, Integer> whiteEndKingside = new Tuple<>(0, 6);
  // end square of black king if castling kingside
  private final Tuple<Integer, Integer> blackEndKingside = new Tuple<>(7, 6);
  // end square of white king if castling queenside
  private final Tuple<Integer, Integer> whiteEndQueenside = new Tuple<>(0, 2);
  // end square of black king if castling queenside
  private final Tuple<Integer, Integer> blackEndQueenside = new Tuple<>(7, 2);

  /**
   * Create a new Mouse_Control object to interface with browser
   *  - Performs calibration to calculate square coordinates
   *  - To do calibration, hover over top left corner of board
   *      until first snapshot, then hover over bottom right
   *      corner of board until second snapshot
   *  @throws InterruptedException if Thread gets interrupted
   *  @throws AWTException if the mouse hardware excepts
   */
  public Mouse_Control() throws InterruptedException, AWTException{
    System.out.println("here");
    
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

    this.screen = new Rectangle(top_left_x, top_left_y, xLength, yLength);

    this.board_screen_shot = takeBoardScreenShot();
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
      robot.mouseMove(col2XPixel(col),row2YPixel(row));
  }

  /**
   * Converts a column to the x pixel of the center of square
   */
  private int col2XPixel(int col) {
    return col2X(col)+xOffset;
  }

  /**
   * Converts a column to the x pixel of the center of square
   */
  private int row2YPixel(int row) {
    return row2Y(row)+yOffset;
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

  /**
   * Converts a pixel x-coordinate into a column
   * @param pixel_x the x-coordinate of the pixel
   * @return the 0-indexed column in which that pixel lies in
   */
  private int X2col(int pixel_x) {
    return 0;
  }

  /**
   * Obtain a localized rgb screenshot of the board
   */
  private int[][] takeBoardScreenShot() {
    int[][] screenShot = new int[8][8];

    int board_offset = (int) Math.floor(length_square/2.0);
    BufferedImage img = robot.createScreenCapture(screen);
    for (int col=0; col<8; col++) {
      for (int row=0; row<8; row++) {
        // get 5 points in each square
        int center_x = col*length_square+board_offset;
        int center_y = (7-row)*length_square+board_offset;
        int offset = (int) Math.floor(length_square/4.0);

        int center = img.getRGB(center_x, center_y);
        int north = img.getRGB(center_x, center_y+offset);
        int south = img.getRGB(center_x, center_y-offset);
        int east = img.getRGB(center_x+offset, center_y);
        int west = img.getRGB(center_x-offset, center_y);
        
        screenShot[row][col] = center+north+south+east+west;
      }
    }

    return screenShot;
  }

  /**
   * Detects a move that was played on the screen, and applies
   *  the move to board
   *  - if a move was successfully made on the board, return true
   *  - else return false
   */
  public boolean lookForMove(Board board) {
    int[][] updatedScreenShot = takeBoardScreenShot();
    int[][] diff = new int[8][8];

    // check for differences in updatedScreenShot and board_screen_shot
    for (int row=0; row<8; row++) {
      for (int col=0; col<8; col++) {
        diff[row][col] = updatedScreenShot[row][col]-this.board_screen_shot[row][col];
      }
    }

    List<Tuple<Integer,Integer>> changedSquares = new LinkedList<Tuple<Integer, Integer>>();
    for (int row=0; row<8; row++) {
      for (int col=0; col<8; col++) {
        if (Math.abs(diff[row][col]) > DIFF_THRESHOLD) {
          Tuple<Integer, Integer> changedCoord = new Tuple<>(row, col);
          // ignore if we've seen that coordinate changed before
          if (this.diffSquares.contains(changedCoord)) {continue;}
          // add it to the set of changed coordinates
          changedSquares.add(changedCoord);
        }
      }
    }

    //for (Tuple<Integer, Integer> flop : changedSquares) {
    //  System.out.println(String.format("Square (%d,%d) is different", flop.x(), flop.y()));
    //}

    // update the previous screenshot
    this.board_screen_shot = updatedScreenShot;


    // if no squares changed, no move was detected at all
    if (changedSquares.size() == 0) {
      System.out.println("No squares changed");
      return false;
    }

    // detected a capture
    if (changedSquares.size() == 1) {
      Tuple<Integer, Integer> startSq = changedSquares.get(0);
      for (Tuple<Integer, Integer> capSq : this.diffSquares) {
        if (board.move(startSq, capSq)) {
          this.diffSquares = changedSquares;
          this.diffSquares.add(capSq);
          return true;
        }
      }
      return false;
    }

    if (changedSquares.size() == 2) {
      Tuple<Integer, Integer> square1 = changedSquares.get(0);
      Tuple<Integer, Integer> square2 = changedSquares.get(1);

      // try to make both moves on the board
      if (board.move(square1, square2)) {
        this.diffSquares = changedSquares;
        return true;
      }
      if (board.move(square2, square1)) {
        this.diffSquares = changedSquares;
        return true;
      }

      // neither move was legal, return an unsuccessful attempt
      System.out.println("Either move wasn't legal");
      return false;
    }

    // castle move
    if (changedSquares.size() == 4) {
      System.out.println("Detected Castle move!");
      if (changedSquares.contains(whiteStartSq) && changedSquares.contains(whiteEndKingside)) {
        // white castle kingside
        if (board.move("e1","g1")) {
          this.diffSquares = changedSquares;
          return true;
        }
        return false;
      } else if (changedSquares.contains(whiteStartSq) && changedSquares.contains(whiteEndQueenside)) {
        // white castle queenside
        if (board.move("e1","c1")) {
          this.diffSquares = changedSquares;
          return true;
        }
        return false;
      } else if (changedSquares.contains(blackStartSq) && changedSquares.contains(blackEndKingside)) {
        // black castle kingside
        if (board.move("e8","g8")) {
          this.diffSquares = changedSquares;
          return true;
        }
        return false;
      } else if (changedSquares.contains(blackStartSq) && changedSquares.contains(blackEndQueenside)) {
        // black castle queenside
        if (board.move("e8","c8")) {
          this.diffSquares = changedSquares;
          return true;
        }
        return false;
      } else {
        // couldn't interpret changed squares
        return false;
      }
    }


    System.out.println("Changed sqs not 2 or 4");
    for (Tuple<Integer, Integer> sq : changedSquares) {
      System.out.println(sq);
    }
    // can't understand set of changed squares
    return false;
  }
}
