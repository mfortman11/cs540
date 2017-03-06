Implementing the Perceptron (40 points)

In this part of HW4 you will implement the perceptron algorithm and run it on the WINE dataset.
I.e., you will largely repeat Problem 1 of this HW, but in Java and using WINE. In addition, you
will use the TUNE set examples to choose the network state that will be applied to the TEST
examples (i.e., ‘early stopping’).

Here is what you need to do.

1) Copy the TRAIN set from
 http://pages.cs.wisc.edu/~shavlik/cs540/HWs/HW0/red-wine-quality-train.data
 
2) Copy the TUNE set from
 http://pages.cs.wisc.edu/~shavlik/cs540/HWs/HW0/red-wine-quality-tune.data .
 
3) Copy the TEST set from
 http://pages.cs.wisc.edu/~shavlik/cs540/HWs/HW0/red-wine-quality-test.data
 
4) Create a file HW4.java that takes as inputs these three strings:
 nameOfTrainSetFile nameOfTuneSetFile nameOfTestSetFile
 
5) Your code should create one input unit per binary feature, where 0 represents the first value
of that feature and 1 represents the second value. (There will also be the ‘-1’ input unit.)
The output should be the class label, where 0 represents the first output class in the dataset
files and 1 represents the second.

Initialize all weights to 0. Use a learning rate (α) of 0.1.

Recall that an epoch is one pass through each of the examples. Use the train set to adjust
weight for 1000 epochs, where at the start of each epoch your code permutes the order the
training examples are processed. (One way you can permute a list is to assign a random
number to each element, in a new list of elements of a class that has two values: the original
item and a double. Sort this new list, then create a new list of examples in that sorted
order.)

After every 50 epochs, measure the accuracy of the current perceptron on the train, tune,
and test sets. Have your code print out these values, like this (these numbers are made up):

Epoch 50: train = 73.6% tune = 67.5% test = 65.8%

Epoch 100: train = 76.9% tune = 71.3% test = 70.4%

Epoch 150: train = 80.1% tune = 72.2% test = 72.7%

 …
 
After 1000 epochs, before exiting your code should print out the epoch where the TUNE
set was highest, and the TEST set accuracy at that epoch, like this (numbers also made up):

 The tune set was highest (74.0% accuracy) at Epoch 250. Test set = 73.8% here.

Your code should also print, after learning is completed, the weights on each feature
and the threshold, e.g. (again, numbers made up):

Wgt = 0.1 fixedAcidityGt47

Wgt = -2.4 volatileAcidityGt17

Wgt = 3.9 volatileAcidityGt29

 …
 
 Threshold = 2.8
 
6) Turn in a short lab report where you plot (by hand is ok) the train, tune, and test
accuracies as a function of the epoch. (The lecture notes have a graph of this form,
illustrating ‘early stopping’ as a method for reducing overfitting.) Briefly discuss your
results. Do you see overfitting? Did the tune set do a reasonable job of choosing a good
stopping point? Be sure to label your axes.

Also compare the test set results for the perceptron to the ensemble of decision trees of
HW2. You only need to discuss the TUNE and TEST set results at the chosen L (of
HW2) and epoch (in this HW) – i.e., no need to compare the entire graphs in these two
HWs.

Finally, include in your lab report the weights on each feature plus the threshold.
Highlight (i.e., mark somehow) which feature has the largest positive weight (if any),
which has the largest negative weight (if any), and which feature weight is closest to
zero. Since you are probably not an expert on wine acidity, etc., you do not need to
discuss if these weights make sense.

Note that we are likely to test your code on datasets different than WINE, but all will only
involve binary-valued features and output.
