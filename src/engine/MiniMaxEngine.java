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

  private final int NORMAL_RECURSION_DEPTH=3;
  private final int MATE_RECURSION_DEPTH=5;

  private final Map<String,Tuple<Move,Double>> min_memo;
  private final Map<String,Tuple<Move,Double>> max_memo;

  /**
   * Create a new engine that uses minimax
   */
  public MiniMaxEngine(Board board, Color color) {
    this.board = board;
    this.side = color;
    this.max_memo = new HashMap<String,Tuple<Move,Double>>();
    this.min_memo = new HashMap<String,Tuple<Move,Double>>();
  }

  // returns the (move,value) pair that minimizes the heuristic
  //  on this board when looking depth moves into the future,
  //  assuming that opponent make moves that minimize heuristic
  //  with depth: depth-1. 
  //  - value corresponds to the heuristic at the end of the line
  //  - if there are no legal moves, return (null,1000)
  private Tuple<Move, Double> min(Board board, int depth) {
    String compressedString = board.compressBoard();
    compressedString += depth;
    if (min_memo.containsKey(compressedString)) {
      return min_memo.get(compressedString);
    }

    Move bestMove = null;
    double bestMoveValue = 1000;
    Set<Move> legalMoves = board.legalMoves();
    double value;
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
      Tuple<Move, Double> bestResponse = new Tuple<>(bestMove, bestMoveValue);
      min_memo.put(compressedString, bestResponse);
      return bestResponse;
    }

    // recursive case: depth > 1
    for (Move move : legalMoves) {
      // make the move
      board.move(move);
      // if found a checkmate, immediately return it
      if (board.checkmate()) {
        board.undoLastMove();
        Tuple<Move, Double> bestResponse = new Tuple<>(move, -1000.0);
        min_memo.put(compressedString, bestResponse);
        return bestResponse;
      }
      // get best response 
      Tuple<Move, Double> bestResponse = max(board, depth-1);
      value = bestResponse.y();
      // undo the move
      board.undoLastMove();

      if (value <= bestMoveValue) {
        bestMove = move;
        bestMoveValue = value;
      }
    }
    Tuple<Move,Double> bestResponse = new Tuple<>(bestMove, bestMoveValue);
    min_memo.put(compressedString, bestResponse);
    return bestResponse;
  }

  // returns the (move,value) pair that maximizes the heuristic
  //  on this board when looking depth moves into the future,
  //  assuming that opponent make moves that minimize heuristic
  //  with depth: depth-1. 
  //  - value corresponds to the heuristic at the end of the line
  //  - if there are no legal moves, return (null,-1000)
  private Tuple<Move, Double> max(Board board, int depth) {
    String compressedString = board.compressBoard();
    compressedString += depth;
    if (max_memo.containsKey(compressedString)) {
      return max_memo.get(compressedString);
    }

    Move bestMove = null;
    double bestMoveValue = -1000;
    Set<Move> legalMoves = board.legalMoves();
    double value;
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
        if (value >= bestMoveValue) {
          bestMove = move;
          bestMoveValue = value;
        }
      }
      Tuple<Move, Double> bestResponse = new Tuple<>(bestMove, bestMoveValue);
      max_memo.put(compressedString, bestResponse);
      return bestResponse;
    }

    // recursive case: depth > 1
    for (Move move : legalMoves) {
      // make the move
      board.move(move);
      // if found a checkmate, immediately return it
      if (board.checkmate()) {
        board.undoLastMove();
        Tuple<Move, Double> bestResponse = new Tuple<>(move, 1000.0);
        max_memo.put(compressedString, bestResponse);
        return bestResponse;
      }
      // get best response 
      Tuple<Move, Double> bestResponse = min(board, depth-1);
      value = bestResponse.y();
      // undo the move
      board.undoLastMove();

      if (value >= bestMoveValue) {
        bestMove = move;
        bestMoveValue = value;
      }
    }
    Tuple<Move,Double> bestResponse = new Tuple<>(bestMove, bestMoveValue);
    max_memo.put(compressedString, bestResponse);
    return bestResponse;
  }

  public double heuristic(Board board) {
    int numMoves = board.getNumMoves();
    double h = 0;
    double val = 0;
    for (int row=0; row < 8; row++) {
      for (int col=0; col < 8; col++) {
        // skip square if no piece there
        if (!board.containsPiece(row,col)) {continue;}
        // get the piece
        Piece piece = board.getPiece(row,col);
        double colCont;
        double rowCont;
        switch(piece.getType()) {
          case KING:
            val = 0;
            break;
          case QUEEN:
            val = 9;
            val += (piece.numTimesMoved()==0) ? clamp(-1*numMoves/30.0,-0.3,0) : 0;
            // prioritize center columns
            colCont = -0.1*(Math.abs(col-3.5)+0.5)+0.4;
            // prioritize center rows
            rowCont = -0.1*(Math.abs(row-3.5)+0.5)+0.4;
            val += colCont*rowCont;
            break;
          case ROOK:
            val = 5;
            break;
          case BISHOP:
            val = 3;
            val += (piece.numTimesMoved()==0) ? clamp(-1*numMoves/4.0,-0.95,0) : 0;
            // prioritize center columns
            colCont = -0.1*(Math.abs(col-3.5)+0.5)+0.4;
            // prioritize center rows
            rowCont = -0.1*(Math.abs(row-3.5)+0.5)+0.4;
            val += colCont*rowCont;
            break;
          case PAWN:
            val = 1;
            if (piece.getColor().equals(Color.WHITE)) {
              // prioritize center columns
              colCont = -0.1*(Math.abs(col-3.5)+0.5)+0.4;
              // reward pushing the pawn
              rowCont = -0.1*(7-row)+0.6;
              val += colCont*rowCont;
            } else {
              // prioritize center columns
              colCont = -0.1*(Math.abs(col-3.5)+0.5)+0.4;
              // reward pushing the pawn
              rowCont = -0.1*(row)+0.6;
              val += colCont*rowCont;
            }
            break;
          case KNIGHT:
            val = 3;
            val += (piece.numTimesMoved()==0) ? clamp(-1*numMoves/7.0,-0.95,0) : 0;
            
            // prioritize center columns
            colCont = -0.1*(Math.abs(col-3.5)+0.5)+0.4;
            // prioritize center rows
            rowCont = -0.1*(Math.abs(row-3.5)+0.5)+0.4;
            val += colCont*rowCont;
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
      h += (board.whiteCastled()) ? 1 : clamp(-1*(numMoves-6)/3.0,-1.6,0);
      h -= (board.blackCastled()) ? 1 : clamp(-1*(numMoves-6)/3.0,-1.6,0);
    }

    if (board.getTurn().equals(Color.WHITE)) {
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
      int depth;
      if (board.numBlackPieces() == 1) {
        // try to find a checkmate
        depth = MATE_RECURSION_DEPTH;
      } else {
        // normal
        depth = NORMAL_RECURSION_DEPTH;
      }
      Tuple<Move, Double> response = max(board, depth);
      Move move = response.x();
      board.move(move);
    } else {
      int depth;
      // if black, min the heuristic
      if (board.numWhitePieces() == 1) {
        // try to find a checkmate
        depth = MATE_RECURSION_DEPTH;
      } else {
        // normal
        depth = NORMAL_RECURSION_DEPTH;
      }
      Tuple<Move, Double> response = min(board, depth);
      Move move = response.x();
      board.move(move);
    }
  }
}
