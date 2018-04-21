package src;

import java.util.*;
import java.awt.AWTException;
import java.awt.event.*;

import src.datatypes.*;
import src.engine.*;
import src.mouse_control.*;

public class Main {
  public static void main(String[] args) {
    try {
      Board board = new Board();
      Scanner reader = new Scanner(System.in);
      Mouse_Control mouse = new Mouse_Control();
      for (int i=0;i<200000;i++) {
        if (mouse.lookForMove(board)) {
          System.out.println("Success!");
        } else {
          System.out.println("Fail :(");
        }
        System.out.println(board);
        String iloveyou = reader.nextLine();
      }

      double x = 1/0;

      //Mouse_Control mouse = new Mouse_Control();
      Thread.sleep(3000);

      //Board board = new Board();

      //Engine player1 = Engine.miniMaxEngine(board, Color.WHITE);
      //Engine player2 = Engine.miniMaxEngine(board, Color.BLACK);
      MiniMaxEngine2 player1 = (MiniMaxEngine2) Engine.miniMaxEngine(board, Color.WHITE);
      MiniMaxEngine2 player2 = (MiniMaxEngine2) Engine.miniMaxEngine(board, Color.BLACK);
      //Engine player2 = Engine.randomEngine(board, Color.BLACK);

      Move move;
      while (!board.checkmate()) {
        Thread.sleep(500);
        player1.signalTurn();
        move = board.getLastMove();
        
        mouse.makeMove(move);
        System.out.println(String.format("Eval: %s", player1.heuristic(board)));
        //System.out.println(String.format("Eval: %s", player2.heuristic(board)));
        if (move.isPromotion()) {Thread.sleep(2000);}

        if (board.checkmate()) {break;}

        Thread.sleep(500);
        player2.signalTurn();
        move = board.getLastMove();
        mouse.makeMove(move);
        System.out.println(String.format("Eval: %s", player1.heuristic(board)));
        //System.out.println(String.format("Eval: %s", player2.heuristic(board)));
        if (move.isPromotion()) {Thread.sleep(2000);}
      }

      if (board.getTurn().equals(Color.WHITE)) {
        System.out.println("Checkmate! Black wins!");
      } else {
        System.out.println("Checkmate! White wins!");
      }

    } catch (InterruptedException e1) {
      throw new Error("Thread was interrupted");
    } catch (AWTException e2) {
      throw new Error("Mouse excepted");
    }
  }
}
