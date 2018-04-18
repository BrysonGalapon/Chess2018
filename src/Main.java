package src;

import java.util.*;
import java.awt.AWTException;
import java.awt.event.*;

import src.datatypes.*;
import src.engine.*;
import src.mouse_control.*;

public class Main {
  public static void main(String[] args) {

    //Scanner reader = new Scanner(System.in);
    //try {
    //  Board board = new Board();
    //  System.out.println("Let's play a chess game!");
    //  String value = "";

    //  Engine player1 = Engine.miniMaxEngine(board);
    //  Engine player2 = Engine.randomEngine(board);
    //  
    //  Set<Move> legalMoves;
    //  while(!value.equals("end")) {
    //    System.out.println(board);
    //    System.out.println();
    //    value = reader.nextLine();
    //    legalMoves = board.legalMoves();
    //    if (legalMoves.size() == 0) {
    //      throw new Error("NANI, CHECKMATE??");
    //    }

    //    System.out.println("Started P1 Turn");
    //    player1.signalTurn();
    //    System.out.println("Ended P1 Turn");
    //    System.out.println(String.format("P1 played: %s", board.getLastMove()));

    //    System.out.println(board);
    //    System.out.println();
    //    value = reader.nextLine();
    //    legalMoves = board.legalMoves();
    //    if (legalMoves.size() == 0) {
    //      throw new Error("NANI, CHECKMATE??");
    //    }

    //    System.out.println("Started P2 Turn");
    //    player2.signalTurn();
    //    System.out.println("Ended P2 Turn");
    //    System.out.println(String.format("P2 played: %s", board.getLastMove()));

    //  }
    //} catch (Exception e) {
    //  System.err.println("Tomato :(");
    //  System.out.println(e);
    //} finally {
    //  reader.close();
    //}

    try {
      Mouse_Control mouse = new Mouse_Control();
      Thread.sleep(3000);

      Board board = new Board();

      Engine player1 = Engine.miniMaxEngine(board);
      Engine player2 = Engine.randomEngine(board);

      Move move;
      while (board.legalMoves().size() != 0) {
        Thread.sleep(500);
        player1.signalTurn();
        move = board.getLastMove();
        mouse.makeMove(move);

        if (board.legalMoves().size() == 0) {break;}

        Thread.sleep(500);
        player2.signalTurn();
        move = board.getLastMove();
        mouse.makeMove(move);
      }

      System.out.println("CHECKMATUUU?");

    } catch (InterruptedException e1) {
      throw new Error("Thread was interrupted");
    } catch (AWTException e2) {
      throw new Error("Mouse excepted");
    }
  }
}
