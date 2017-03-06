Creating Probabilistic Reasoners that Play Nannon (90 points)

This problem involves writing Java code that implements three probabilistic reasoners to play the
two-person board game called Nannon (http://nannon.com), which is a simplified version of the
well-known game Backgammon (http://en.wikipedia.org/wiki/Backgammon). Instructions for Nannon
are available at http://nannon.com/rules.html.

Here is how we will formulate the task. At each turn, whenever there is more than one legal
move, your chooseMove method is given access to:
1) The current board configuration.
2) A list of legal moves; each move's effect is also provided, and you are able to
determine the next board configuration from each move. (Explanations of which
effects are computed for you appear in the chooseMove method of the provided
RandomNannonPlayer and in the ManageMoveEffects.java file).

Your chooseMove method needs to return one of the legal moves. It should do this by using
Bayes’ Rule to estimate the odds each move will lead to a winning game, returning the one with
the highest odds. That is, it should compute for each possible move:

  Prob(will win game | current board, move, next board, and move’s effect)
  ______________________________________________________________
  Prob(will lose game | current board, move, next board, and move’s effect)

Your solution need not use ALL of these given’s to estimate these probabilities, and you can
choose to define whichever random variables you wish from the provided information. The
specific design is up to you and we expect each student’s solution to be unique.

You need to create three solutions. In one, you will create a full joint probability table. In the
other two you will create two (Bayesian Networks, BNs); neither can be a BN equivalent to your
full joint probability table. One BN should be Naive Bayes (NB) and the other needs to somehow
go beyond the NB conditional-independence assumption (see notes). It is up to you to decide the
specific random variables used and, for the non-naive Bayesian Network, which conditional
independence assumptions you wish to make. The random variables in your three solutions need
not be the same.

You need to place your three solutions in these files, where YourMoodleLoginName is your
actual Moodle (i.e., your UWisc) login:
  FullJointProbTablePlayer_YourMoodleLoginName.java
  NaiveBayesNetPlayer_YourMoodleLoginName.java
  BayesNetPlayer_YourMoodleLoginName.java
Copy all the Java files in http://pages.cs.wisc.edu/~shavlik/cs540/HWs/HW3/ to your working space.
The provided PlayNannon.java, NannonPlayer.java, and RandomNannonPlayer.java files
contain substantial details on what you need to do. You should start by reading the comments in
them; I suggest you read the files in the order they appear in the previous sentence. 
CS 540 HW3 Page 6
After the final HW due date, you can share you code with others and have your players play one
another, since they have unique names. We might also run student code against one another
during grading.

So how do you get the necessary information to compute these probabilities? After each game,
your players’ updateStatistics method is given information about the sequence of board
configurations encountered and the moves chosen by your player in that game, as well as
whether or not your player won that game. You should not try to figure out which moves where
good or bad in any one specific games; instead, if a game was won, all moves in it should be
considered good (i.e., led to a win) and if a game is lost all moves should be considered bad (led
to a loss). Obviously some moves in losing games were good and vice versa, but because we are
using statistics, we are robust to this ‘noise.’

Your three players need to implement the reportLearnedModel method, which reports the value
of the random variable (or values of the combination of random variables) where the following
ratio is largest (i.e., most indicative of a win) and the smallest (i.e., most indicative of a loss):

  prob( randomVariable(s) | win) / prob(randomVariable(s) | loss)
  
For your full-joint-prob table, randomVariables should be a setting of all the random variables
other than the ‘win’ variable (i.e., loss = ¬ win). For your NB player, randomVariable(s) should
be one variable other than win. For your Bayes Net approach, randomVariable(s) should be
one of the non-NB entries in the product of probabilities you compute. (Recall that if we want to
allow some dependencies among our random variables, the product in a non-naive Bayes Net
calculation will include something like p(A | B ∧ win) x p(B | win), which is equivalent to
p(A ∧ B | win), as explained in class.)

The reportLearnedModel method is automatically called (by code we have written) after a run of
k games completes when Nannon.reportLearnedModels is set to true.

It is fine to print more about what was learned, but the above requested information should be
easy to find in your printout.
