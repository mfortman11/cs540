import java.util.*;


//import BuildAndTestDecisionTree.DecisionTreeNode;
//import BuildAndTestDecisionTree.DecisionTreeNodeType;
//import BuildAndTestDecisionTree.Feature;

//import BuildAndTestDecisionTree.TreeNode;
//import BuildAndTestDecisionTree.Example;
//import BuildAndTestDecisionTree.Feature;

import java.io.*;
import java.text.DecimalFormat;

////////////////////////////////////////////////////////////////////////////
//                                                                       
// 	Code for HW2, 
//  CS540 (Shavlik)
//	Michael Fortman
//	 9068638114
//                                                                        
////////////////////////////////////////////////////////////////////////////

/* BuildAndTestDecisionTree.java 

   Copyright 2008, 2011, 2013 by Jude Shavlik and Nick Bridle.
   May be freely used for non-profit educational purposes.

   To run after compiling, type:

     java BuildAndTestDecisionTree <trainsetFilename> <testsetFilename>

   Eg,

     java BuildAndTestDecisionTree train-hepatitis.data test-hepatitis.data

   where <trainsetFilename> and <testsetFilename> are the 
   input files of examples.

   Notes:  

          Please place all your HW1 code in a single file. 

          All that is required is that you keep the name of the
          BuildAndTestDecisionTree class and don't change the 
          calling convention for its main function.  

          There is no need to worry about "error detection" when reading data files.
          We'll be responsible for that.  HOWEVER, DO BE AWARE THAT
          WE WILL USE ONE OR MORE DIFFERENT DATASETS DURING TESTING,
          SO DON'T  WRITE CODE THAT IS SPECIFIC TO THE PROVIDED
          DATASETS.  (As  stated above, you may assume that our additional datasets
          are properly formatted in the style used for the provided dataset.)

          A weakness of our design is that the category and feature
          names are defined in BOTH the train and test files.  These
          names MUST match, though this isn't checked.  However,
          we'll live with the weakness because it reduces complexity
          overall (note: you can use the SAME filename for both the
          train and the test set, as a debugging method; you should
          get ALL the test examples correct in this case, since we are
		  not "pruning" decision trees to avoid overfitting the training data).
 */

public class HW2
{
	// "Main" reads in the names of the files we want to use, then reads 
	// in their examples.
	public static void main(String[] args)
	{   
		/*if (args.length != 2)
		{
			System.err.println("You must call BuildAndTestDecisionTree as " + 
					"follows:\n\njava BuildAndTestDecisionTree " + 
					"<trainsetFilename> <testsetFilename>\n");
			System.exit(1);
		}    

		// Read in the file names.
		String trainset = args[0];
		String testset  = args[1];
		 */
		// Read in the examples from the files.
		ListOfExamples[] trainExamples = new ListOfExamples[102];
		for (int j = 0; j < trainExamples.length; j++) {
			trainExamples[j] = new ListOfExamples();
		}
		//ListOfExamples trainExamples = new ListOfExamples();
		ListOfExamples testExamples  = new ListOfExamples();
		ListOfExamples tuneExamples = new ListOfExamples();
		/*
		if (!trainExamples.ReadInExamplesFromFile(trainset) || !testExamples.ReadInExamplesFromFile(testset))
		{
			System.err.println("Something went wrong reading the datasets ... " +
					"giving up.");
			System.exit(1);
		}
		else
		{ /* The following is included so you can see the data organization.
         You'll need to REPLACE it with code that:

          1) uses the TRAINING SET of examples to build a decision tree

          2) prints out the induced decision tree (using simple, indented 
             ASCII text)

          3) categorizes the TESTING SET using the induced tree, reporting
             which examples were INCORRECTLY classified, as well as the
             FRACTION that were incorrectly classified.
             Just print out the NAMES of the examples incorrectly classified
             (though during debugging you might wish to print out the full
             example to see if it was processed correctly by your decision 
             tree)       
		 */
		String trainName;
		for (int i = 1; i <= 101; i++) {
			trainName = "wine-bagged\\wine-b-"+i+".data";
			if (!trainExamples[i].ReadInExamplesFromFile(trainName))
			{
				System.err.println("Something went wrong reading the datasets ... " +
						"giving up." + i);
				System.exit(1);
			}
		}
		
		String tuneName = "red-wine-quality-tune.data";
		String testName = "red-wine-quality-test.data";
		
		if (!tuneExamples.ReadInExamplesFromFile(tuneName) || !testExamples.ReadInExamplesFromFile(testName))
		{
			System.err.println("Something went wrong reading the datasets ... " +
					"giving up.");
			System.exit(1);
		}
		
		BinaryFeature outputLabels = trainExamples[1].getOutputLabel();
		//System.out.println(examples.toString());
		//List<Example> trainingExamples = trainExamples;
		List<Example> tuneingExamples = tuneExamples;
		List<Example> testingExamples = testExamples;	
		List<BinaryFeature> features = Arrays.asList(trainExamples[1].getFeatures());
		
		TreeNode root[] = new TreeNode[102];
		for(int i = 1; i <= 101; i++) {
			root[i] = buildTree(null, features, trainExamples[i], outputLabels, outputLabels.getSecondValue());
		}
		//BinaryFeature best = getBestFeature(outputLabels, features, examples);
		//printTree(root, "", 1);
		String[][] tuneResults = testResults2D(root, tuneingExamples, "TUNE_SET");
		String[][] testResults = testResults2D(root, testingExamples, "TEST_SET");
		//System.out.println(Arrays.deepToString(tuneResults));
		
		for(String[] row : tuneResults) {
			System.out.println(row[0]);
		}
		/*int lowToMidCount = 0;
		for (int ex = 0; ex < tuneingExamples.size(); ex++){
			lowToMidCount = 0;
			for (int tr = 1; tr <= 101; tr++){
				if (tuneResults[ex][tr].equals("lowToMid"))
					lowToMidCount++;
			}
			System.out.println(lowToMidCount);
		}*/
		//testTree(root, tuneingExamples, "TUNE_SET");
		//testTree(root, testingExamples, "TEST_SET");
		
		
		//trainExamples.DescribeDataset();
		//testExamples.DescribeDataset();
		//trainExamples.PrintThisExample(0);  // Print out an example
		//trainExamples.PrintAllExamples(); // Don't waste paper printing all 
		// of this out!
		//testExamples.PrintAllExamples();  // Instead, just view it on the screen
	//}

	Utilities.waitHere("Hit <enter> when ready to exit.");
}
	
public static String[][] testResults2D(TreeNode[] roots, List<Example> ex, String type){

	String[][] resultsArray = new String[ex.size()][roots.length];
	
	for (int i = 1; i <= 101; i++){
		testTree(roots[i], ex, type, resultsArray, i);
	}
	
	
	return resultsArray;
}

/**
 * recursive method used to build a treenode and its recursively build its children
 * 
 * @param parent
 * @param features
 * @param examples
 * @param outputLabels
 * @param labelVal
 * @return
 */
public static TreeNode buildTree(TreeNode parent, List<BinaryFeature> features, List<Example> examples,  BinaryFeature outputLabels, String labelVal)
{
	String label1 = outputLabels.getFirstValue();
	String label2 = outputLabels.getSecondValue();
	int numLabel1 = 0;
	int numLabel2 = 0;
	boolean leaf = true;

	if(examples.isEmpty()) {
		TreeNode node = new TreeNode(parent, examples, features, labelVal, leaf);
		return node;
	}

	for (Example e: examples) {
		if (e.getLabel().equals(outputLabels.getFirstValue())) {
			numLabel1++;
		} else {
			numLabel2++;
		}
	}

	//if all examples have the same label value create a leaf node
	if(numLabel1 == 0) {
		TreeNode node = new TreeNode(parent, examples, features, label2, leaf);
		return node;
	}
	else if(numLabel2 == 0) {
		TreeNode node = new TreeNode(parent, examples, features, label1, leaf);
		return node;
	}

	// find label value for node based on majority tie goes to label2
	String newLabelVal = null;
	if(numLabel2 >= numLabel1) {
		newLabelVal = label2;
	}
	else {
		newLabelVal = label1;
	}

	if(features.isEmpty()) {
		TreeNode node = new TreeNode(parent, examples, null, newLabelVal, leaf);
		return node;
	}

	BinaryFeature bestFeature = getBestFeature(outputLabels, features, examples);
	List<BinaryFeature> remainingFeatures = new ArrayList<BinaryFeature>();

	for (BinaryFeature f: features) {
		if(!f.getName().equals(bestFeature.getName())){
			remainingFeatures.add(f);
		}
	}

	TreeNode newNode = new TreeNode(parent, examples, remainingFeatures, bestFeature);

	List<Example> exFeatureVal1 = new ArrayList<Example>();
	List<Example> exFeatureVal2 = new ArrayList<Example>();

	for (Example e : examples) {
		if (e.getFeatureValue(bestFeature).equals(bestFeature.getFirstValue())) {
			exFeatureVal1.add(e);
		} else {
			exFeatureVal2.add(e);
		}
	}

	// build subtree for first feature value (left)
	TreeNode firstChild = buildTree(newNode, remainingFeatures, exFeatureVal1, outputLabels, newLabelVal);
	newNode.setLeftChild(firstChild);
	// build subtree for second feature value (right)
	TreeNode secondChild = buildTree(newNode, remainingFeatures, exFeatureVal2, outputLabels, newLabelVal);
	newNode.setRightChild(secondChild);

	return newNode;
}

/**
 * Searches though feature list to find the best feature to create a root node on by finding the feature with the
 * smallest remainder
 * 
 * @param outputLabel
 * @param features
 * @param examples
 * @return
 */
private static BinaryFeature getBestFeature (BinaryFeature outputLabel, List<BinaryFeature> features, List<Example> examples) {

	BinaryFeature best = null;

	int difference = 0;
	double bestGain = Integer.MAX_VALUE;
	double posGain = 0.0;
	double negGain = 0.0;
	double gain = 0.0;

	for (BinaryFeature f: features) {

		int pLabel1 = 0;
		int pLabel2 = 0;
		int nLabel1 = 0;
		int nLabel2 = 0;
		int numFeatureValue1 = 0;
		int numFeatureValue2 = 0;

		for (Example e: examples) {

			if (e.getFeatureValue(f).equals(f.getFirstValue())) {
				numFeatureValue1++;
				if (e.getLabel().equals(outputLabel.getFirstValue())) {
					pLabel1++;
				} else {
					nLabel1++;
				}

			} else {
				numFeatureValue2++;
				if (e.getLabel().equals(outputLabel.getFirstValue())) {
					pLabel2++;
				} else {
					nLabel2++;
				}
			}
		}

		posGain = calcInfoGain(numFeatureValue1, examples.size(), pLabel1, nLabel1);
		negGain = calcInfoGain(numFeatureValue2, examples.size(), pLabel2, nLabel2);
		gain = posGain + negGain;
		difference = Double.compare(gain, bestGain);
		if(difference < 0) {
			bestGain = gain;
			best = f;
		}
		else if(difference == 0) { //for same gain choose based on feature name
			if(f.getName().compareTo(best.getName()) < 0) {
				best = f;
			}
		}
	}

	//System.out.println(bestGain);
	return best;

}

/**
 * calculates the info gain's remainder based on a features stats
 * 
 * @param numFeatureVal
 * @param numExamples
 * @param numPositive
 * @param numNegative
 * @return
 */
private static double calcInfoGain(double numFeatureVal, double numExamples, double numPositive, double numNegative) {
	if(numFeatureVal == 0.0 || ( numPositive == 0.0 && numNegative == 0.0 )) {
		return 0.0;
	}

	double pEntropy = 0.0;
	double nEntropy = 0.0;
	double totalEntropy = 0.0;
	if(numPositive != 0) {
		pEntropy = (numPositive/numFeatureVal)*(Math.log(numPositive/numFeatureVal)/Math.log(2)) * -1;
	}
	if(numNegative != 0) {
		nEntropy = (numNegative/numFeatureVal)*(Math.log(numNegative/numFeatureVal)/Math.log(2)) * -1;
	}
	totalEntropy = pEntropy + nEntropy;

	return (numFeatureVal/numExamples)*totalEntropy;
}

/**
 * print out the tree with a max depth of 
 * 
 * @param node
 * @param indents
 * @param depth
 */
private static void printTree(TreeNode node, String indents, int depth) {
	if(node == null) {
		return;
	}

	if(depth > 5) {
		System.out.print(" DEPTH LIMIT REACHED");
		return;
	}

	//check if it is a leaf
	if(node.checkIfLeaf()) {
		System.out.print(" " + node.getLabelVal().toUpperCase());
	}
	//if not a leaf node
	else {
		BinaryFeature feature = node.getFeature();

		System.out.print("\n" + indents + feature.getName() + " = " + feature.getFirstValue() + ":");
		printTree(node.getLeftChild(), indents + "\t", depth + 1);

		System.out.print("\n"+ indents + feature.getName() + " = " + feature.getSecondValue() + ":");
		printTree(node.getRightChild(), indents + "\t", depth + 1);
	}
}

/**
 * tests the tree w/ other examples and prints the stats and examples that missed
 * 
 * @param root
 * @param examples
 * @param dataSetName
 */
private static void testTree(TreeNode root, List<Example> examples, String dataSetName) {

	List<Example> misses = new ArrayList<Example>();

	int totalExamples = examples.size();

	for(Example e : examples) {
		String exLabel = e.getLabel();
		String treeLabel = getTreeLabel(root, e);
		if(!exLabel.equals(treeLabel)) {
			misses.add(e);
		}
	}

	if(!misses.isEmpty()) {
		System.out.print("\nList of failed examples : ");
		for(Example e : misses) {
			System.out.print(e.getName() + " ");
		}
		System.out.println("\n");
	}
	int numHits = totalExamples - misses.size();

	double hitPercentage = (double)(numHits)/totalExamples *100;
	System.out.println("Hit percentage for " + dataSetName + " data set :  " + String.format("%.2f%%", hitPercentage) + 
			" % :(" + numHits + "/" + totalExamples + ")");
}

/**
 * tests the tree w/ other examples and prints the stats and examples that missed
 * HW2 also adds predictons to array
 * 
 * @param root
 * @param examples
 * @param dataSetName
 */
private static void testTree(TreeNode root, List<Example> examples, String dataSetName, String[][] results, int treeNum) {

	List<Example> misses = new ArrayList<Example>();

	int totalExamples = examples.size();
	int exNum = 0;
	
	for(Example e : examples) {
		String exLabel = e.getLabel();
		results[exNum][0] = exLabel;
		String treeLabel = getTreeLabel(root, e);
		results[exNum][treeNum] = treeLabel;
		//System.out.println(results[treeNum][exNum]);
		exNum++;
		if(!exLabel.equals(treeLabel)) {
			misses.add(e);
		}
	}
/*
	if(!misses.isEmpty()) {
		System.out.print("\nList of failed examples : ");
		for(Example e : misses) {
			System.out.print(e.getName() + " ");
		}
		System.out.println("\n");
	}
	int numHits = totalExamples - misses.size();

	double hitPercentage = (double)(numHits)/totalExamples *100;
	System.out.println("Hit percentage for " + dataSetName + " data set :  " + String.format("%.2f%%", hitPercentage) + 
			" % :(" + numHits + "/" + totalExamples + ")");*/
}

private static String getTreeLabel(TreeNode root, Example e) {
	while(!root.checkIfLeaf() && root != null) {
		BinaryFeature f = root.getFeature();
		String featureVal = e.getFeatureValue(f);

		//System.out.println(e.getName() + "  " + e.getFeatureValue(f) + " " + e.getLabel());
		if (featureVal.equals(f.getFirstValue())) {
			root = root.getLeftChild();
		} else {
			root = root.getRightChild();
		}
	}

	String label = root.getLabelVal();
	return label;
}
}

// This class, an extension of ArrayList, holds an individual example.
// The new method PrintFeatures() can be used to
// display the contents of the example. 
// The items in the ArrayList are the feature values.
class Example extends ArrayList<String>
{
	// The name of this example.
	private String name;  

	// The output label of this example.
	private String label;

	// The data set in which this is one example.
	private ListOfExamples parent;  

	//map each features value to the BinaryFeature name so it is easily searchable
	private Map<String, String> featureMap = new HashMap<String, String>();

	// Constructor which stores the dataset which the example belongs to.
	public Example(ListOfExamples parent) {
		this.parent = parent;
	}

	// Print out this example in human-readable form.
	public void PrintFeatures()
	{
		System.out.print("Example " + name + ",  label = " + label + "\n");
		for (int i = 0; i < parent.getNumberOfFeatures(); i++)
		{
			System.out.print("     " + parent.getFeatureName(i)
			+ " = " +  this.get(i) + "\n");
		}
	}

	// Adds a feature value to the example. and feature map
	public void addFeatureValue(BinaryFeature feature, String value) {
		this.add(value);
		this.featureMap.put(feature.getName(), value);
	}

	public String getFeatureValue(BinaryFeature feature) {
		return featureMap.get(feature.getName());
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	// Mutator methods.
	public void setName(String name) {
		this.name = name;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}

/* This class holds all of our examples from one dataset
   (train OR test, not BOTH).  It extends the ArrayList class.
   Be sure you're not confused.  We're using TWO types of ArrayLists.  
   An Example is an ArrayList of feature values, while a ListOfExamples is 
   an ArrayList of examples. Also, there is one ListOfExamples for the 
   TRAINING SET and one for the TESTING SET. 
 */
class ListOfExamples extends ArrayList<Example>
{
	// The name of the dataset.
	private String nameOfDataset = "";

	// The number of features per example in the dataset.
	private int numFeatures = -1;

	// An array of the parsed features in the data.
	private BinaryFeature[] features;

	// A binary feature representing the output label of the dataset.
	private BinaryFeature outputLabel;

	// The number of examples in the dataset.
	private int numExamples = -1;

	public ListOfExamples() {} 

	// Print out a high-level description of the dataset including its features.
	public void DescribeDataset()
	{
		System.out.println("Dataset '" + nameOfDataset + "' contains "
				+ numExamples + " examples, each with "
				+ numFeatures + " features.");
		System.out.println("Valid category labels: "
				+ outputLabel.getFirstValue() + ", "
				+ outputLabel.getSecondValue());
		System.out.println("The feature names (with their possible values) are:");
		for (int i = 0; i < numFeatures; i++)
		{
			BinaryFeature f = features[i];
			System.out.println("   " + f.getName() + " (" + f.getFirstValue() +
					" or " + f.getSecondValue() + ")");
		}
		System.out.println();
	}

	public BinaryFeature[] getFeatures() {
		return this.features;
	}

	// Print out ALL the examples.
	public void PrintAllExamples()
	{
		System.out.println("List of Examples\n================");
		for (int i = 0; i < size(); i++)
		{
			Example thisExample = this.get(i);  
			thisExample.PrintFeatures();
		}
	}

	// Print out the SPECIFIED example.
	public void PrintThisExample(int i)
	{
		Example thisExample = this.get(i); 
		thisExample.PrintFeatures();
	}

	public BinaryFeature getOutputLabel(){
		return this.outputLabel;
	}

	// Returns the number of features in the data.
	public int getNumberOfFeatures() {
		return numFeatures;
	}

	// Returns the name of the ith feature.
	public String getFeatureName(int i) {
		return features[i].getName();
	}

	// Takes the name of an input file and attempts to open it for parsing.
	// If it is successful, it reads the dataset into its internal structures.
	// Returns true if the read was successful.
	public boolean ReadInExamplesFromFile(String dataFile) {
		nameOfDataset = dataFile;

		// Try creating a scanner to read the input file.
		Scanner fileScanner = null;
		try {
			fileScanner = new Scanner(new File(dataFile));
		} catch(FileNotFoundException e) {
			return false;
		}

		// If the file was successfully opened, read the file
		this.parse(fileScanner);
		return true;
	}

	/**
	 * Does the actual parsing work. We assume that the file is in proper format.
	 *
	 * @param fileScanner a Scanner which has been successfully opened to read
	 * the dataset file
	 */
	public void parse(Scanner fileScanner) {
		// Read the number of features per example.
		numFeatures = Integer.parseInt(parseSingleToken(fileScanner));

		// Parse the features from the file.
		parseFeatures(fileScanner);

		// Read the two possible output label values.
		String labelName = "output";
		String firstValue = parseSingleToken(fileScanner);
		String secondValue = parseSingleToken(fileScanner);
		outputLabel = new BinaryFeature(labelName, firstValue, secondValue);

		// Read the number of examples from the file.
		numExamples = Integer.parseInt(parseSingleToken(fileScanner));

		parseExamples(fileScanner);
	}

	/**
	 * Returns the first token encountered on a significant line in the file.
	 *
	 * @param fileScanner a Scanner used to read the file.
	 */
	private String parseSingleToken(Scanner fileScanner) {
		String line = findSignificantLine(fileScanner);

		// Once we find a significant line, parse the first token on the
		// line and return it.
		Scanner lineScanner = new Scanner(line);
		return lineScanner.next();
	}

	/**
	 * Reads in the feature metadata from the file.
	 * 
	 * @param fileScanner a Scanner used to read the file.
	 */
	private void parseFeatures(Scanner fileScanner) {
		// Initialize the array of features to fill.
		features = new BinaryFeature[numFeatures];

		for(int i = 0; i < numFeatures; i++) {
			String line = findSignificantLine(fileScanner);

			// Once we find a significant line, read the feature description
			// from it.
			Scanner lineScanner = new Scanner(line);
			String name = lineScanner.next();
			String dash = lineScanner.next();  // Skip the dash in the file.
			String firstValue = lineScanner.next();
			String secondValue = lineScanner.next();
			features[i] = new BinaryFeature(name, firstValue, secondValue);
		}
	}

	private void parseExamples(Scanner fileScanner) {
		// Parse the expected number of examples.
		for(int i = 0; i < numExamples; i++) {
			String line = findSignificantLine(fileScanner);
			Scanner lineScanner = new Scanner(line);

			// Parse a new example from the file.
			Example ex = new Example(this);

			String name = lineScanner.next();
			ex.setName(name);

			String label = lineScanner.next();
			ex.setLabel(label);

			// Iterate through the features and increment the count for any feature
			// that has the first possible value.
			for(int j = 0; j < numFeatures; j++) {
				String feature = lineScanner.next();
				//System.out.println(this.features.length + " " + this.features[j].getName());
				ex.addFeatureValue(this.features[j], feature);
			}

			// Add this example to the list.
			this.add(ex);
		}
	}

	/**
	 * Returns the next line in the file which is significant (i.e. is not
	 * all whitespace or a comment.
	 *
	 * @param fileScanner a Scanner used to read the file
	 */
	private String findSignificantLine(Scanner fileScanner) {
		// Keep scanning lines until we find a significant one.
		while(fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine().trim();
			if (isLineSignificant(line)) {
				return line;
			}
		}

		// If the file is in proper format, this should never happen.
		System.err.println("Unexpected problem in findSignificantLine.");

		return null;
	}

	/**
	 * Returns whether the given line is significant (i.e., not blank or a
	 * comment). The line should be trimmed before calling this.
	 *
	 * @param line the line to check
	 */
	private boolean isLineSignificant(String line) {
		// Blank lines are not significant.
		if(line.length() == 0) {
			return false;
		}

		// Lines which have consecutive forward slashes as their first two
		// characters are comments and are not significant.
		if(line.length() > 2 && line.substring(0,2).equals("//")) {
			return false;
		}

		return true;
	}
}

/**
 * Represents a single binary feature with two String values.
 */
class BinaryFeature {
	private String name;
	private String firstValue;
	private String secondValue;

	public BinaryFeature(String name, String first, String second) {
		this.name = name;
		firstValue = first;
		secondValue = second;
	}

	public String getName() {
		return name;
	}

	public String getFirstValue() {
		return firstValue;
	}

	public String getSecondValue() {
		return secondValue;
	}
}

class Utilities
{
	// This method can be used to wait until you're ready to proceed.
	public static void waitHere(String msg)
	{
		System.out.print("\n" + msg);
		try { System.in.read(); }
		catch(Exception e) {} // Ignore any errors while reading.
	}
}

/**
 * TreeNode object
 * @author Mike Fortman
 *
 */
class TreeNode {

	private BinaryFeature feature;
	private TreeNode parent;
	private TreeBranch leftBranch;
	private TreeBranch rightBranch;
	private String labelVal;
	private boolean isLeaf;

	private List<Example> remainingExamples;
	private List<BinaryFeature> remainingFeatures;

	//normal node constructor
	public TreeNode (TreeNode parent, List<Example> rExamples, List<BinaryFeature> rFeatures, BinaryFeature feature) {
		this.parent = parent;
		this.remainingExamples = rExamples;
		this.remainingFeatures = rFeatures;
		this.feature = feature;
		this.isLeaf = false;
		this.labelVal = null;
		this.leftBranch = new TreeBranch(this.feature.getFirstValue(), parent);
		this.rightBranch = new TreeBranch(this.feature.getSecondValue(), parent);
	}

	//leaf node constructor
	public TreeNode (TreeNode parent, List<Example> rExamples, List<BinaryFeature> rFeatures, String labelVal, boolean leaf){
		this.parent = parent;
		this.remainingExamples = rExamples;
		this.remainingFeatures = rFeatures;
		this.feature = null;
		this.isLeaf = leaf;
		this.labelVal = labelVal;
		this.leftBranch = null;
		this.rightBranch = null;
	}

	public BinaryFeature getFeature() {
		return this.feature;
	}

	public List<BinaryFeature> getRemainingFeatures(){
		return this.remainingFeatures;
	}

	public List<Example> getRemainingExamples(){
		return this.remainingExamples;
	}

	public boolean checkIfLeaf() {
		return this.isLeaf;
	}

	public String getLabelVal(){
		return this.labelVal;
	}

	public void setLeftChild (TreeNode child) {
		this.leftBranch.setChild(child);
	}

	public TreeNode getLeftChild () {
		return this.leftBranch.getChild();
	}

	public void setRightChild (TreeNode child) {
		this.rightBranch.setChild(child);
	}

	public TreeNode getRightChild () {
		return this.rightBranch.getChild();
	}
}

/**
 * Object linking TreeNodes
 * @author Mike Fortman
 *
 */
class TreeBranch {

	private String featureValue;
	private TreeNode parent;
	private TreeNode child;

	public TreeBranch (String featureVal, TreeNode parent) {
		this.featureValue = featureVal;
		this.parent = parent;
	}

	public void setChild (TreeNode child) {
		this.child = child;
	}

	public TreeNode getChild () {
		return this.child;
	}

	public String getFeatureVal () {
		return this.featureValue;
	}
}