/**
 * Copyrighted 2013 by Jude Shavlik.  Maybe be freely used for non-profit educational purposes.
 */

/*
 * A player that simply random chooses from among the possible moves.
 * 
 * NOTE: I (Jude) recommend you COPY THIS TO YOUR PLAYER (NannonPlayer_yourLoginName.java) AND THEN EDIT IN THAT FILE.
 * 
 *       Be sure to change "Random-Move Player" in getPlayerName() to something unique to you!
 */

import java.util.List;

public class FullJointProbTablePlayer_mfortman extends NannonPlayer {

	/*
	 * A good way to create your players is to edit these methods.  See PlayNannon.java for more details.
	 */

	@Override
	public String getPlayerName() { return "mfortman Full Joint Prob Table Player"; } // <------------------ choose a name for your player here in your (ranamed) copy of this class (ok to simply use your normal name or your initials, but also consider including your login name so unique).

	// Constructors.
	public FullJointProbTablePlayer_mfortman() {
		initialize();

	}
	public FullJointProbTablePlayer_mfortman(NannonGameBoard gameBoard) {
		super(gameBoard);
		initialize();
	}

	private int[][][][][][] fjt_win = null; 
	private int[][][][][][] fjt_loss = null;

	private void initialize() {
		// Put things here needed for instance creation.
		fjt_win = new int[4][4][2][4][4][2];
		fjt_loss = new int[4][4][2][4][4][2];
	}

	@SuppressWarnings("unused") // This prevents a warning from the "if (false)" below.
	@Override
	public List<Integer> chooseMove(int[] boardConfiguration, List<List<Integer>> legalMoves) {

		// Below is some code you might want to use in your solution.
		//      (a) converts to zero-based counting for the cell locations
		//      (b) converts NannonGameBoard.movingFromHOME and NannonGameBoard.movingToSAFE to NannonGameBoard.cellsOnBoard,
		//          (so you could then make arrays with dimension NannonGameBoard.cellsOnBoard+1)
		//      (c) gets the current and next board configurations.
		
		List<Integer> bestMove = Utils.chooseRandomElementFromThisList(legalMoves);
		double bestProb = 0.0;
		double prob = 0.0;
		if (legalMoves != null) {
			for (List<Integer> move : legalMoves) { // <----- be sure to drop the "false &&" !

				int fromCountingFromOne    = move.get(0);  // Convert below to an internal count-from-zero system.
				int   toCountingFromOne    = move.get(1);			
				int                 effect = move.get(2);  // See ManageMoveEffects.java for the possible values that can appear here.	

				// Note we use 0 for both 'from' and 'to' because one can never move FROM SAFETY or TO HOME, so we save a memory cell.
				int from = (fromCountingFromOne == NannonGameBoard.movingFromHOME ? 0 : fromCountingFromOne);
				int to   = (toCountingFromOne   == NannonGameBoard.movingToSAFETY ? 0 : toCountingFromOne);

				// The 'effect' of move is encoded in these four booleans:
				boolean        hitOpponent = ManageMoveEffects.isaHit(      effect);  // Did this move 'land' on an opponent (sending it back to HOME)?
				boolean       brokeMyPrime = ManageMoveEffects.breaksPrime( effect);  // A 'prime' is when two pieces from the same player are adjacent on the board;
				// an opponent can NOT land on pieces that are 'prime' - so breaking up a prime of 
				// might be a bad idea.
				boolean extendsPrimeOfMine = ManageMoveEffects.extendsPrime(effect);  // Did this move lengthen (i.e., extend) an existing prime?
				boolean createsPrimeOfMine = ManageMoveEffects.createsPrime(effect);  // Did this move CREATE a NEW prime? (A move cannot both extend and create a prime.)

				// Note that you can compute other effects than the four above (but you need to do it from the info in boardConfiguration, resultingBoard, and move).

				// See comments in updateStatistics() regarding how to use these.
				int[] resultingBoard = gameBoard.getNextBoardConfiguration(boardConfiguration, move);  // You might choose NOT to use this - see updateStatistics().

				/* Here is what is in a board configuration vector.  There are also accessor functions in NannonGameBoard.java (starts at or around line 60).

			   	boardConfiguration[0] = whoseTurn;        // Ignore, since it is OUR TURN when we play, by definition. (But needed to compute getNextBoardConfiguration.)
        		boardConfiguration[1] = homePieces_playerX; 
        		boardConfiguration[2] = homePieces_playerO;
        		boardConfiguration[3] = safePieces_playerX;
        		boardConfiguration[4] = safePieces_playerO;
        		boardConfiguration[5] = die_playerX;      // I added these early on, but never used them.
        		boardConfiguration[6] = die_playerO;      // Probably can be ignored since get the number of legal moves, which is more meaningful.

        		cells 7 to (6 + NannonGameBoard.cellsOnBoard) record what is on the board at each 'cell' (ie, board location).
        					- one of NannonGameBoard.playerX, NannonGameBoard.playerO, or NannonGameBoard.empty.

				 */

				// DO SOMETHING HERE.             <-------------------------------------------------
				int homeNum = 0;
				int safeNum = 0;
				int hasPrime = 0; //1 = true 0 = false
				int oppHomeNum = 0;
				int oppSafeNum = 0;
				int oppHasPrime = 0; //1 = true 0 = false
				
				int wins = 0;
				int losses = 0;

				prob = 0.0;//reset
				
				//if it is PlayerX's turn
				if(resultingBoard[0] == 1) {
					homeNum = resultingBoard[1];
					oppHomeNum = resultingBoard[2];
					safeNum = resultingBoard[3];
					oppHomeNum = resultingBoard[4];

					if (extendsPrimeOfMine || createsPrimeOfMine)
						hasPrime = 1;
					else if (brokeMyPrime)
						hasPrime = 0;
					else {
						hasPrime = 0;
						for (int i = 0; i < 5; i++) {
							if (resultingBoard[7 + i] == 1 && resultingBoard[7 + i + 1] == 1){
								hasPrime = 1;
								break;
							}
						}
					}

					oppHasPrime = 0;
					for (int i = 0; i < 5; i++) {
						if (resultingBoard[7 + i] == 2 && resultingBoard[7 + i + 1] == 2){
							oppHasPrime = 1;
							break;
						}
					}
				}
				//if it is PlayerO's turn
				else if(resultingBoard[0] == 2) {
					oppHomeNum = resultingBoard[1];
					homeNum = resultingBoard[2];
					oppHomeNum = resultingBoard[3];
					safeNum = resultingBoard[4];

					if (extendsPrimeOfMine || createsPrimeOfMine)
						hasPrime = 1;
					else if (brokeMyPrime)
						hasPrime = 0;
					else {
						hasPrime = 0;
						for (int i = 0; i < 5; i++) {
							if (resultingBoard[7 + i] == 2 && resultingBoard[7 + i + 1] == 2){
								hasPrime = 1;
								break;
							}
						}
					}

					oppHasPrime = 0;
					for (int i = 0; i < 5; i++) {
						if (resultingBoard[7 + i] == 1 && resultingBoard[7 + i + 1] == 1){
							oppHasPrime = 1;
							break;
						}
					}
				}
				
				wins = fjt_win[homeNum][safeNum][hasPrime][oppHomeNum][oppSafeNum][oppHasPrime];
				losses = fjt_loss[homeNum][safeNum][hasPrime][oppHomeNum][oppSafeNum][oppHasPrime];
				//System.out.println(wins + " " + losses);
				prob = (double) wins / (double) (wins + losses);
				//replace with new best move if found
				if (prob > bestProb) {
					bestProb = prob;
					bestMove = move;
				}
				
			}
		}

		//return Utils.chooseRandomElementFromThisList(legalMoves); // In you own code you should of course get rid of this line.
		return bestMove;
	}

	@SuppressWarnings("unused") // This prevents a warning from the "if (false)" below.
	@Override
	public void updateStatistics(boolean             didIwinThisGame, 
			List<int[]>         allBoardConfigurationsThisGameForPlayer,
			List<Integer>       allCountsOfPossibleMovesForPlayer,
			List<List<Integer>> allMovesThisGameForPlayer) {

		// Do nothing with these in the random player (but hints are here for use in your players).	

		// However, here are the beginnings of what you might want to do in your solution (see comments in 'chooseMove' as well).
		//if (false) { // <------------ Be sure to remove this 'false' *********************************************************************
		int numberOfMyMovesThisGame = allBoardConfigurationsThisGameForPlayer.size();	

		for (int myMove = 0; myMove < numberOfMyMovesThisGame; myMove++) {
			int[]         currentBoard        = allBoardConfigurationsThisGameForPlayer.get(myMove);
			int           numberPossibleMoves = allCountsOfPossibleMovesForPlayer.get(myMove);
			List<Integer> moveChosen          = allMovesThisGameForPlayer.get(myMove);
			int[]         resultingBoard      = (numberPossibleMoves < 1 ? currentBoard // No move possible, so board is unchanged.
					: gameBoard.getNextBoardConfiguration(currentBoard, moveChosen));

			// You should compute the statistics needed for a Bayes Net for any of these problem formulations:
			//
			//     prob(win | currentBoard and chosenMove and chosenMove's Effects)  <--- this is what I (Jude) did, but mainly because at that point I had not yet written getNextBoardConfiguration()
			//     prob(win | resultingBoard and chosenMove's Effects)               <--- condition on the board produced and also on the important changes from the prev board
			//
			//     prob(win | currentBoard and chosenMove)                           <--- if we ignore 'chosenMove's Effects' we would be more in the spirit of a State Board Evaluator (SBE)
			//     prob(win | resultingBoard)                                        <--- but it seems helpful to know something about the impact of the chosen move (ie, in the first two options)
			//
			//     prob(win | currentBoard)                                          <--- if you estimate this, be sure when CHOOSING moves you apply to the NEXT boards (since when choosing moves, one needs to score each legal move).

			if (numberPossibleMoves < 1) { continue; } // If NO moves possible, nothing to learn from (it is up to you if you want to learn for cases where there is a FORCED move, ie only one possible move).

			// Convert to our internal count-from-zero system.
			// A move is a list of three integers.  Their meanings should be clear from the variable names below.
			int fromCountingFromOne = moveChosen.get(0);  // Convert below to an internal count-from-zero system.
			int   toCountingFromOne = moveChosen.get(1);
			int              effect = moveChosen.get(2);  // See ManageMoveEffects.java for the possible values that can appear here. Also see the four booleans below.

			// Note we use 0 for both 'from' and 'to' because one can never move FROM SAFETY or TO HOME, so we save a memory cell.
			int from = (fromCountingFromOne == NannonGameBoard.movingFromHOME ? 0 : fromCountingFromOne);
			int to   = (toCountingFromOne   == NannonGameBoard.movingToSAFETY ? 0 : toCountingFromOne);

			// The 'effect' of move is encoded in these four booleans:
			boolean        hitOpponent = ManageMoveEffects.isaHit(      effect); // Explained in chooseMove() above.
			boolean       brokeMyPrime = ManageMoveEffects.breaksPrime( effect);
			boolean extendsPrimeOfMine = ManageMoveEffects.extendsPrime(effect);
			boolean createsPrimeOfMine = ManageMoveEffects.createsPrime(effect);

			// DO SOMETHING HERE.  See chooseMove() for an explanation of what is stored in currentBoard and resultingBoard.

			int homeNum = 0;
			int safeNum = 0;
			int hasPrime = 0; //1 = true 0 = false
			int oppHomeNum = 0;
			int oppSafeNum = 0;
			int oppHasPrime = 0; //1 = true 0 = false

			//if it is PlayerX's turn
			if(resultingBoard[0] == 1) {
				homeNum = resultingBoard[1];
				oppHomeNum = resultingBoard[2];
				safeNum = resultingBoard[3];
				oppHomeNum = resultingBoard[4];

				if (extendsPrimeOfMine || createsPrimeOfMine)
					hasPrime = 1;
				else if (brokeMyPrime)
					hasPrime = 0;
				else {
					hasPrime = 0;
					for (int i = 0; i < 5; i++) {
						if (resultingBoard[7 + i] == 1 && resultingBoard[7 + i + 1] == 1){
							hasPrime = 1;
							break;
						}
					}
				}

				oppHasPrime = 0;
				for (int i = 0; i < 5; i++) {
					if (resultingBoard[7 + i] == 2 && resultingBoard[7 + i + 1] == 2){
						oppHasPrime = 1;
						break;
					}
				}
			}
			//if it is PlayerO's turn
			else if(resultingBoard[0] == 2) {
				oppHomeNum = resultingBoard[1];
				homeNum = resultingBoard[2];
				oppHomeNum = resultingBoard[3];
				safeNum = resultingBoard[4];

				if (extendsPrimeOfMine || createsPrimeOfMine)
					hasPrime = 1;
				else if (brokeMyPrime)
					hasPrime = 0;
				else {
					hasPrime = 0;
					for (int i = 0; i < 5; i++) {
						if (resultingBoard[7 + i] == 2 && resultingBoard[7 + i + 1] == 2){
							hasPrime = 1;
							break;
						}
					}
				}

				oppHasPrime = 0;
				for (int i = 0; i < 5; i++) {
					if (resultingBoard[7 + i] == 1 && resultingBoard[7 + i + 1] == 1){
						oppHasPrime = 1;
						break;
					}
				}
			}
			if (didIwinThisGame) {
				fjt_win[homeNum][safeNum][hasPrime][oppHomeNum][oppSafeNum][oppHasPrime]++;
				//System.out.println("Win: " + fjt_win[homeNum][safeNum][hasPrime][oppHomeNum][oppSafeNum][oppHasPrime]);
			} else {
				fjt_loss[homeNum][safeNum][hasPrime][oppHomeNum][oppSafeNum][oppHasPrime]++;
				//System.out.println("Loss: " + fjt_loss[homeNum][safeNum][hasPrime][oppHomeNum][oppSafeNum][oppHasPrime]);
			}


		}
		//}
	}

	@Override
	public void reportLearnedModel() { // You can add some code here that reports what was learned, eg the most important feature for WIN and for LOSS.  And/or all the weights on your features.
		double bestCombo = 0.0;
		int bestHomeNum = 0;
		int bestSafeNum = 0;
		int bestHasPrime = 0;
		int bestOppHomeNum = 0;
		int bestOppSafeNum = 0;
		int bestOppHasPrime = 0;
		
		double worstCombo = 100000000.0;
		int worstHomeNum = 0;
		int worstSafeNum = 0;
		int worstHasPrime = 0;
		int worstOppHomeNum = 0;
		int worstOppSafeNum = 0;
		int worstOppHasPrime = 0;
		
		int wins = 0;
		int losses = 0;
		double prob = 0.0;
		
		for (int homeNum = 0; homeNum < gameBoard.getPiecesPerPlayer() + 1; homeNum++) {
			for (int safeNum = 0; safeNum < gameBoard.getPiecesPerPlayer() + 1; safeNum++) {
				for (int hasPrime = 0; hasPrime < 2; hasPrime++) {
					for (int oppHomeNum = 0; oppHomeNum < gameBoard.getPiecesPerPlayer() + 1; oppHomeNum++) {
						for (int oppSafeNum = 0; oppSafeNum < gameBoard.getPiecesPerPlayer() + 1; oppSafeNum++) {
							for (int oppHasPrime = 0; oppHasPrime < 2; oppHasPrime++) {
								wins = fjt_win[homeNum][safeNum][hasPrime][oppHomeNum][oppSafeNum][oppHasPrime];
								losses = fjt_loss[homeNum][safeNum][hasPrime][oppHomeNum][oppSafeNum][oppHasPrime];
								
								prob = (double) wins / (double) (wins + losses);
								
								if (prob > bestCombo) {
									bestCombo = prob;
									bestHomeNum = homeNum;
									bestSafeNum = safeNum;
									bestHasPrime = hasPrime;
									bestOppHomeNum = oppHomeNum;
									bestOppSafeNum = oppSafeNum;
									bestOppHasPrime = oppHasPrime;
								}
								if(prob < worstCombo) {
									worstCombo = prob;
									worstHomeNum = homeNum;
									worstSafeNum = safeNum;
									worstHasPrime = hasPrime;
									worstOppHomeNum = oppHomeNum;
									worstOppSafeNum = oppSafeNum;
									worstOppHasPrime = oppHasPrime;
								}
							}
						}
					}
				}
			}
		}
		
		Utils.println("\n-------------------------------------------------");
		Utils.println("Best homeNum: " + bestHomeNum);
		Utils.println("Best safeNum: " + bestSafeNum);
		Utils.println("Best hasPrime: " + bestHasPrime);
		Utils.println("Best oppHomeNum: " + bestOppHomeNum);
		Utils.println("Best oppSafeNum: " + bestOppSafeNum);
		Utils.println("Best oppHasPrime: " + bestOppHasPrime);
		Utils.println("\n-------------------------------------------------");
		Utils.println("Worst homeNum: " + worstHomeNum);
		Utils.println("Worst safeNum: " + worstSafeNum);
		Utils.println("Worst hasPrime: " + worstHasPrime);
		Utils.println("Worst oppHomeNum: " + worstOppHomeNum);
		Utils.println("Worst oppSafeNum: " + worstOppSafeNum);
		Utils.println("Worst oppHasPrime: " + worstOppHasPrime);
		Utils.println("\n-------------------------------------------------");
	}
}
