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
      mouse.makeMove(new Move(PieceType.PAWN, "d2", "d4"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.PAWN, "e5", "d4"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.PAWN, "c3", "d4"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.BISHOP, "c5", "b4"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KNIGHT, "b1", "c3"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KNIGHT, "f6", "e4"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KING, "e1", "g1"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KNIGHT, "e4", "c3"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.PAWN, "b2", "c3"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.BISHOP, "b4", "c3"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.QUEEN, "d1", "b3"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.BISHOP, "c3", "a1"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.BISHOP, "c4", "f7"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KING, "e8", "f8"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.BISHOP, "c1", "g5"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KNIGHT, "c6", "e7"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.KNIGHT, "f3", "e5"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.BISHOP, "a1", "d4"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.BISHOP, "f7", "g6"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.PAWN, "h7", "g6"));
      Thread.sleep(500);
      mouse.makeMove(new Move(PieceType.QUEEN, "b3", "f7"));
      Thread.sleep(500);
    } catch (InterruptedException e1) {
      throw new Error("Thread was interrupted");
    } catch (AWTException e2) {
      throw new Error("Mouse excepted");
    }
  }
}
