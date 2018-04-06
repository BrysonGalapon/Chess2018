package src;

import java.awt.AWTException;
import java.awt.event.*;

import src.datatypes.*;
import src.mouse_control.*;

public class Main {
  public static void main(String[] args) {
    try {
      Mouse_Control mouse = new Mouse_Control();
      Thread.sleep(2000);
      mouse.makeMove(new Move(PieceType.PAWN, "e2", "e4"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.PAWN, "e7", "e5"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KNIGHT, "g1", "f3"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KNIGHT, "b8", "c6"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.BISHOP, "f1", "c4"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.BISHOP, "f8", "c5"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.PAWN, "c2", "c3"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KNIGHT, "g8", "f6"));
      Thread.sleep(500);
    } catch (InterruptedException e1) {
      throw new Error("Thread was interrupted");
    } catch (AWTException e2) {
      throw new Error("Mouse excepted");
    }
  }
}
