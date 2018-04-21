package src.engine;

import src.datatypes.*;
import java.util.*;

/**
 * Engine that implements MiniMax
 */
public class MiniMaxEngine2 implements Engine {
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
  public MiniMaxEngine2(Board board, Color color) {
    this.board = board;
    this.side = color;
    this.max_memo = new HashMap<String,Tuple<Move,Double>>();
    this.min_memo = new HashMap<String,Tuple<Move,Double>>();
  }

  private Tuple<Move, Double> min(Board board, int depth) {
    String position = board.compressBoard() + depth;
    if (min_memo.containsKey(position)) {
      return min_memo.get(position);
    }

    // base case
    if (depth == 0) {
      Tuple<Move, Double> response = new Tuple<>(null, heuristic(board));
      min_memo.put(position, response);
      return response;
    }

    String og = board.compressBoard();

    Set<Move> legalMoves = board.legalMoves();
    // no legal moves
    if (legalMoves.size() == 0) {
      if (board.checkmate()) {
        // checkmate
        Tuple<Move, Double> checkmate_response = new Tuple<>(null, 1000.0);
        min_memo.put(position, checkmate_response);
        return checkmate_response;
      } else {
        // stalemate
        Tuple<Move, Double> stalemate_response = new Tuple<>(null, 0.0);
        min_memo.put(position, stalemate_response);
        return stalemate_response;
      }
    }

    double evaluation;
    double bestEvaluation = 1000;

    int four_counter=0;

    // list of moves-to-evaluation
    List<Tuple<Move,Double>> m2e = new ArrayList<Tuple<Move,Double>>();
    for (Move move : legalMoves) {
      if (depth == 4) {
        if (four_counter >= 3) {break;}
        four_counter++;
      }

      if (!board.compressBoard().equals(og)) {throw new Error("NOT OG_BEGIN_min");}

      // make the move
      board.move(move);

      if (board.checkmate()) {
        // found a mate!
        board.undoLastMove();
        Tuple<Move, Double> checkmate_response = new Tuple<>(move, -1000.0);
        min_memo.put(position, checkmate_response);
        return checkmate_response;
      }

      // get the evaluation assuming best response
      Tuple<Move,Double> response = max(board, depth-1);
      evaluation = response.y();

      m2e.add(new Tuple<>(move, evaluation));
      // update the evaluation for best move
      if (evaluation <  bestEvaluation) {bestEvaluation=evaluation;}

      // undo the move
      board.undoLastMove();

      if (!board.compressBoard().equals(og)) {throw new Error("NOT OG_END_min");}
    }

    // find the move with the best evaluation
    Move bestMove = getBestMove(m2e, bestEvaluation);
    Tuple<Move, Double> bestResponse = new Tuple<>(bestMove, bestEvaluation);
    min_memo.put(position, bestResponse);
    return bestResponse;
  }

  private Tuple<Move, Double> max(Board board, int depth) {
    String position = board.compressBoard() + depth;
    if (max_memo.containsKey(position)) {
      return max_memo.get(position);
    }

    // base case
    if (depth == 0) {
      Tuple<Move, Double> response = new Tuple<>(null, heuristic(board));
      max_memo.put(position, response);
      return response;
    }

    String og = board.compressBoard();

    Set<Move> legalMoves = board.legalMoves();
    // no legal moves
    if (legalMoves.size() == 0) {
      if (board.checkmate()) {
        // checkmate
        Tuple<Move, Double> checkmate_response = new Tuple<>(null, -1000.0);
        max_memo.put(position, checkmate_response);
        return checkmate_response;
      } else {
        // stalemate
        Tuple<Move, Double> stalemate_response = new Tuple<>(null, 0.0);
        max_memo.put(position, stalemate_response);
        return stalemate_response;
      }
    }

    double evaluation;
    double bestEvaluation = -1000;

    int four_counter=0;

    // list of moves-to-evaluation
    List<Tuple<Move,Double>> m2e = new ArrayList<Tuple<Move,Double>>();
    for (Move move : legalMoves) {
      if (depth == 4) {
        if (four_counter >= 3) {break;}
        four_counter++;
      }

      if (!board.compressBoard().equals(og)) {throw new Error("NOT OG_BEGIN_min");}

      // make the move
      board.move(move);

      if (board.checkmate()) {
        // found a mate!
        board.undoLastMove();
        Tuple<Move, Double> checkmate_response = new Tuple<>(move, 1000.0);
        max_memo.put(position, checkmate_response);
        return checkmate_response;
      }

      // get the evaluation assuming best response
      Tuple<Move,Double> response = min(board, depth-1);
      evaluation = response.y();

      m2e.add(new Tuple<>(move, evaluation));
      // update the evaluation for best move
      if (evaluation >  bestEvaluation) {bestEvaluation=evaluation;}

      // undo the move
      board.undoLastMove();

      if (!board.compressBoard().equals(og)) {throw new Error("NOT OG_END_min");}
    }

    // find the move with the best evaluation
    Move bestMove = getBestMove(m2e, bestEvaluation);
    Tuple<Move, Double> bestResponse = new Tuple<>(bestMove, bestEvaluation);
    max_memo.put(position, bestResponse);
    return bestResponse;
  }

  /**
   * Get a move with a certain evaluation from a list of tuples
   *  - throws an Error if input list is empty, or if evaluation
   *    does not appear in the list
   * @param movePlusEval a list containing (move, eval) tuples
   * @param evaluation an evaluation that is considered 'best' in the list
   */
  private Move getBestMove(List<Tuple<Move, Double>> movePlusEval, double evaluation) {
    if (movePlusEval.size() == 0) {throw new Error("Input list is empty");}

    Random gen = new Random();
    List<Move> possibleMoves = new ArrayList<Move>();

    // filter for moves with that evaluation
    for (Tuple<Move, Double> element : movePlusEval) {
      if (element.y() != evaluation) {continue;}
      possibleMoves.add(element.x());
    }

    if (possibleMoves.size() == 0) {throw new Error("No moves with that evaluation");}

    // get a random one
    int randIndex = gen.nextInt(possibleMoves.size()); 
    return possibleMoves.get(randIndex);
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
    Tuple<Move, Double> response;
    // if white, max the heuristic
    if (side.equals(Color.WHITE)) {
      response = max(this.board, NORMAL_RECURSION_DEPTH);
    } else {
      response = min(this.board, NORMAL_RECURSION_DEPTH);
    }

    Move move = response.x();
    this.board.move(move);
  }
}
