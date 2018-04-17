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
  private Tuple<Move, Integer> min(Board board, int depth) {
    Move bestMove = null;
    int bestMoveValue = 1000;
    Set<Move> legalMoves = board.legalMoves();
    int value;
    // base case 
    if (depth == 1) {
      for (Move move : legalMoves) {
        // make the move
        board.move(move);
        // evaluate the position
        value = heuristic(board);
        // undo the move
        board.undoLastMove();

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
      // make the move
      board.move(move);
      // get best response 
      Tuple<Move, Integer> bestResponse = max(board, depth-1);
      value = bestResponse.y();
      // undo the move
      board.undoLastMove();

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
  private Tuple<Move, Integer> max(Board board, int depth) {
    Move bestMove = null;
    int bestMoveValue = -1000;
    Set<Move> legalMoves = board.legalMoves();
    int value;
    String ogPos = board.toString();
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
          System.out.println("OG POS");
          System.out.println(ogPos);
          System.out.println();
          System.out.println("Move");
          System.out.println(move);
          System.out.println();
          System.out.println("First");
          System.out.println(first);
          System.out.println();
          System.out.println("Last");
          System.out.println(last);
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
      // make the move
      board.move(move);
      // get best response 
      Tuple<Move, Integer> bestResponse = min(board, depth-1);
      value = bestResponse.y();
      // undo the move
      board.undoLastMove();

      if (value >= bestMoveValue) {
        bestMove = move;
        bestMoveValue = value;
      }
    }
    return new Tuple<>(bestMove, bestMoveValue);
  }

  private int heuristic(Board board) {
    int h = 0;
    int val;
    for (int row=0; row < 8; row++) {
      for (int col=0; col < 8; col++) {
        // skip square if no piece there
        if (!board.containsPiece(row,col)) {continue;}
        // get the piece
        Piece piece = board.getPiece(row,col);
        switch(piece.getType()) {
          case KING:
            // ignore the king
            val = 0;
            break;
          case QUEEN:
            val = 9;
            break;
          case ROOK:
            val = 5;
            break;
          case BISHOP:
            val = 3;
            break;
          case PAWN:
            val = 1;
            break;
          case KNIGHT:
            val = 3;
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

    return h;
  }

  @Override
  public void signalTurn() {
    // if white, max the heuristic
    if (side.equals(Color.WHITE)) {
      Tuple<Move, Integer> response = max(board, MAX_RECURSION_DEPTH);
      Move move = response.x();
      if (move == null) {throw new Error("CRYYY");}
      board.move(move);
    } else {
    // if black, min the heuristic
      Tuple<Move, Integer> response = min(board, MAX_RECURSION_DEPTH);
      Move move = response.x();
      if (move == null) {throw new Error("CRYYY");}
      board.move(move);
    }
  }
}