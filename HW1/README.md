Building Decision Trees in Java (100 points)

In this part of the homework you will implement in Java a simplified version of the decision-tree induction algorithm of Fig 18.5 (recall that we call it ID3). We'll assume that all features are binary valued.
We will not be using a tuning set in this part of the homework. In HW2 we will investigate an alternate method for reducing overfitting in decision trees.

Recall the 'real world' dataset about wine from HW0. It involves predicting whether or not a given wine is highly rated. We have divided it into a training set and a testing set.

A second sample dataset, one used in early machine-learning research, is also available. We have divided it into a training set and a testing set. You should not turn in anything related to this dataset, but you might want to use it for debugging or to get more experience with decision-tree induction. (Ditto for the Titantic dataset provided for HW0.) The voting dataset is based on actual votes on the US House of Representatives in the 1980's. The task is: given a representative's voting record on the 16 chosen bills, was the representative a Democrat or a Republican? More information is available via the UC-Irvine archive of machine-learning datasets. The voting dataset is one of the few that only involves binary-valued features. Also, hopefully everyone has some intuitions about the task domain.

We also recommend you create 2-3 simple and 'meaning-free' datasets for debugging, ones where you can compute by hand the correct answer (you might even want to use your code to check your answer to Problem 1 of this homework!). By 'meaning free,' we suggest you simply call the features F1, F2, etc. Also consider looking at some old CS540 midterm exams for simple datasets.

We have provided some code (will be released 9/15/16) that reads the data files into some Java data structures. PLEASE DO NOT LOOK AT THIS FILE UNTIL YOU HAVE TURNED IN YOUR HW0. See BuildAndTestDecisionTree.java. You're welcome to use any or all of this provided code. The only requirement is that you create a BuildAndTestDecisionTree class, whose calling convention is as follows:


  java BuildAndTestDecisionTree <trainsetFilename> <testsetFilename> 

Note that you can provide the SAME file name for BOTH training and testing to see how well your code 'fit' the training data (it should get them all correct except for the 'extreme noise' case that was discussed in class). Accuracy on the training set is not of much interest, but it can help during debugging.
See BuildAndTestDecisionTree.java for more details. Do be aware that we will test your solutions on additional datasets beyond those we provided.

Here is what you need to do:

Use the TRAINING SET of examples to build a decision tree.
Print out the induced decision tree (using simple, indented ASCII text; we'll discuss how one might do this in class).
Categorize the TESTING SET using the induced tree, reporting which examples were INCORRECTLY classified, as well as the FRACTION that were incorrectly classified. Just print out the NAMES of the examples incorrectly classified (though during debugging you might wish to print out the full example to see if it was processed correctly by your decision tree).
