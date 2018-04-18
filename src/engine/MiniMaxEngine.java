package src.engine;

import src.datatypes.*;
import java.util.*;

/**
 * Engine that implements MiniMax
 */
public class MiniMaxEngine implements Engine {
  // instance of board to make moves on
  private final Board board;
  private final Color side;

  private final int MAX_RECURSION_DEPTH=3;

  /**
   * Create a new engine that uses minimax
   */
  public MiniMaxEngine(Board board) {
    this.board = board;
    this.side = board.getTurn();
  }

  // returns the (move,value) pair that minimizes the heuristic
  //  on this board when looking depth moves into the future,
  //  assuming that opponent make moves that minimize heuristic
  //  with depth: depth-1. 
  //  - value corresponds to the heuristic at the end of the line
  //  - if there are no legal moves, return (null,1000)
  private Tuple<Move, Double> min(Board board, int depth) {
    Move bestMove = null;
    double bestMoveValue = 1000;
    Set<Move> legalMoves = board.legalMoves();
    double value;
    // base case 
    if (depth == 1) {
      for (Move move : legalMoves) {
        String first = board.toString();
        // make the move
        board.move(move);
        // evaluate the position
        value = heuristic(board);
        // undo the move
        board.undoLastMove();
        String last = board.toString();
        if (!first.equals(last)) {
          System.out.println(move);
          System.out.println(first);
          System.out.println();
          System.out.println(last);
          System.out.println();
          throw new Error("FUCK_1");
        }

        // update best move combination
        if (value <= bestMoveValue) {
          bestMove = move;
          bestMoveValue = value;
        }

      }
      return new Tuple<>(bestMove, bestMoveValue);
    }

    // recursive case: depth > 1
    for (Move move : legalMoves) {
      String first = board.toString();
      // make the move
      board.move(move);
      // get best response 
      Tuple<Move, Double> bestResponse = max(board, depth-1);
      value = bestResponse.y();
      // undo the move
      board.undoLastMove();
      String last = board.toString();
      if (!first.equals(last)) {
        System.out.println(move);
        System.out.println(first);
        System.out.println();
        System.out.println(last);
        System.out.println();
        throw new Error("FUCK_2");
      }

      if (value <= bestMoveValue) {
        bestMove = move;
        bestMoveValue = value;
      }
    }
    return new Tuple<>(bestMove, bestMoveValue);
  }

  // returns the (move,value) pair that maximizes the heuristic
  //  on this board when looking depth moves into the future,
  //  assuming that opponent make moves that minimize heuristic
  //  with depth: depth-1. 
  //  - value corresponds to the heuristic at the end of the line
  //  - if there are no legal moves, return (null,-1000)
  private Tuple<Move, Double> max(Board board, int depth) {
    Move bestMove = null;
    double bestMoveValue = -1000;
    Set<Move> legalMoves = board.legalMoves();
    double value;
    // base case 
    if (depth == 1) {
      for (Move move : legalMoves) {
        String first = board.toString();
        // make the move
        board.move(move);
        // evaluate the position
        value = heuristic(board);
        // undo the move
        board.undoLastMove();
        String last = board.toString();

        if (!first.equals(last)) {
          System.out.println(move);
          System.out.println(first);
          System.out.println();
          System.out.println(last);
          System.out.println();
          throw new Error("FUCK_3");
        }

        // update best move combination
        if (value >= bestMoveValue) {
          bestMove = move;
          bestMoveValue = value;
        }
      }
      return new Tuple<>(bestMove, bestMoveValue);
    }

    // recursive case: depth > 1
    for (Move move : legalMoves) {
      String first = board.toString();
      // make the move
      board.move(move);
      // get best response 
      Tuple<Move, Double> bestResponse = min(board, depth-1);
      value = bestResponse.y();
      // undo the move
      board.undoLastMove();
      String last = board.toString();
      if (!first.equals(last)) {
        System.out.println(move);
        System.out.println(first);
        System.out.println();
        System.out.println(last);
        System.out.println();
        throw new Error("FUCK_4");
      }

      if (value >= bestMoveValue) {
        bestMove = move;
        bestMoveValue = value;
      }
    }
    return new Tuple<>(bestMove, bestMoveValue);
  }

  private double heuristic(Board board) {
    int numMoves = board.getNumMoves();
    double h = 0;
    double val = 0;
    for (int row=0; row < 8; row++) {
      for (int col=0; col < 8; col++) {
        // skip square if no piece there
        if (!board.containsPiece(row,col)) {continue;}
        // get the piece
        Piece piece = board.getPiece(row,col);
        switch(piece.getType()) {
          case KING:
            val = 0;
            break;
          case QUEEN:
            val = 9;
            if (piece.getColor().equals(side))
              val += (piece.numTimesMoved()==0) ? clamp(-1*numMoves/20.0,-0.95,0) : 0;
            break;
          case ROOK:
            val = 5;
            break;
          case BISHOP:
            val = 3;
            if (piece.getColor().equals(side))
              val += (piece.numTimesMoved()==0) ? clamp(-1*numMoves/4.0,-0.95,0) : 0;
            break;
          case PAWN:
            val = 1;
            break;
          case KNIGHT:
            val = 3;
            if (piece.getColor().equals(side))
              val += (piece.numTimesMoved()==0) ? clamp(-1*numMoves/7.0,-0.95,0) : 0;
            if (piece.getColor().equals(side))
              val += (piece.numTimesMoved()<2) ? -0.1*(Math.abs(col-3.5)+0.5)+0.4 : 0;
            break;
          default:
            throw new Error("Unexpected Piece Type");
        }

        // attach a -1 factor for black pieces
        if (piece.getColor().equals(Color.BLACK)) {
          val = -1*val;
        }

        // increment value of piece to the heuristic
        h = h+val;
      }
    }

    if (numMoves >= 6) {
      val += (board.whiteCastled()) ? 1 : clamp(-1*(numMoves-6)/3.0,-1.6,0);
      val -= (board.blackCastled()) ? 1 : clamp(-1*(numMoves-6)/3.0,-1.6,0);
    }
    h = h+val;

    Color side = board.getTurn();
    if (side.equals(Color.WHITE)) {
      if (board.checkmate()) {
        h = h + (-2000);
      }
    }  else {
      if (board.checkmate()) {
        h = h + (2000);
      }
    }

    return h;
  }

  /**
   * Clamp a value between a minimum and maximum
   */
  private double clamp(double value, double min, double max) {
    return Math.min(Math.max(value, min), max);
  }

  @Override
  public void signalTurn() {
    // if white, max the heuristic
    if (side.equals(Color.WHITE)) {
      Tuple<Move, Double> response = max(board, MAX_RECURSION_DEPTH);
      Move move = response.x();
      if (move == null) {throw new Error("CRYYY");}
      board.move(move);
    } else {
    // if black, min the heuristic
      Tuple<Move, Double> response = min(board, MAX_RECURSION_DEPTH);
      Move move = response.x();
      if (move == null) {throw new Error("CRYYY");}
      board.move(move);
    }
  }
}
