import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class HW4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 3)
		{
			System.err.println("You need 3 arguments\n");
			System.exit(1);
		}    

		// Read in the file names.
		String trainset = args[0];
		String tuneset 	= args[1];
		String testset  = args[2];

		// Read in the examples from the files.
		ListOfExamples trainExamples = new ListOfExamples();
		ListOfExamples tuneExamples  = new ListOfExamples();
		ListOfExamples testExamples  = new ListOfExamples();
		if (!trainExamples.ReadInExamplesFromFile(trainset) || !testExamples.ReadInExamplesFromFile(testset) || !tuneExamples.ReadInExamplesFromFile(tuneset))
		{
			System.err.println("Something went wrong reading the datasets ... " +
					"giving up.");
			System.exit(1);
		}
		else
		{
			BinaryFeature[] inputs = testExamples.getFeatures();
			double[] weights = new double[inputs.length + 1]; //last weight is our threshold
			int[] featureVals = new int[inputs.length];
			//System.out.println(inputs[0].getName());
			
			double wSum = 0.0;
			int predictedOut = 0;
			int actualOut = 0;
			int correct = 0;
			int total = 0;
			double trainAcc = 0.0;
			double tuneAcc = 0.0;
			double testAcc = 0.0;
			
			double bestTuneAcc = 0.0;
			int bestTuneEpoch = 0;
			double testAccAtBestTune = 0.0;

			
			//for 1000 epoches
			for (int i = 1; i <= 1000; i++) {
				correct = 0;
				total = 0;
				//System.out.println(i);
				//1 epoch is once pass through the examples
				//trainExamples.PrintThisExample(0);
				Collections.shuffle(trainExamples);
				//trainExamples.PrintThisExample(0);
				for (Example e: trainExamples) {
					wSum = 0.0;
					//for each feature in the example
					for (int j = 0; j < inputs.length; j++) {
						if (e.getFeatureValue(inputs[j]).equals(inputs[j].getFirstValue())) {
							featureVals[j] = 1;
							wSum += (1.0 * weights[j]); //no need to add to the sum if feature value is 0
						} else {
							featureVals[j] = 0;
						}
					}
					//System.out.println("through features");
					//-1 feature
					wSum += (-1.0 * weights[weights.length - 1]);
					
					if (wSum >= weights[weights.length - 1]) {
						predictedOut = 1;
					}
					else {
						predictedOut = 0;
					}
					
					if (e.getLabel().equals(trainExamples.getOutputLabel().getFirstValue())) {
						actualOut = 1;
					}
					else {
						actualOut = 0;
					}
					total++;
					//check if outputs match if they don't update weights
					if (predictedOut != actualOut) {
						//update each weight
						for (int w = 0; w < weights.length-1; w++) {
							weights[w] = weights[w] + (.1 * (actualOut - predictedOut) * (double)featureVals[w]);
						}
						weights[weights.length-1] = weights[weights.length-1] + (.1 * (actualOut - predictedOut) * (-1.0));
					}
					else {
						//System.out.println("hit");
						correct++;
					}
				}
				if (i % 50 == 0) {
					trainAcc = (double)(correct) / (double)(total);
					
					correct = 0;
					total = 0;
					
					for (Example e: tuneExamples) {
						wSum = 0.0;
						//for each feature in the example
						for (int j = 0; j < inputs.length; j++) {
							if (e.getFeatureValue(inputs[j]).equals(inputs[j].getFirstValue())) {
								featureVals[j] = 1;
								wSum += (1.0 * weights[j]); //no need to add to the sum if feature value is 0
							} else {
								featureVals[j] = 0;
							}
						}
						//System.out.println("through features");
						//-1 feature
						wSum += (-1.0 * weights[weights.length - 1]);
						
						if (wSum >= weights[weights.length - 1]) {
							predictedOut = 1;
						}
						else {
							predictedOut = 0;
						}
						
						if (e.getLabel().equals(trainExamples.getOutputLabel().getFirstValue())) {
							actualOut = 1;
						}
						else {
							actualOut = 0;
						}
						total++;
						//no weight updates
						if (predictedOut != actualOut) {

						}
						else {
							//System.out.println("hit");
							correct++;
						}
					}
					
					tuneAcc = (double)(correct) / (double)(total);
					
					correct = 0;
					total = 0;
					
					for (Example e: testExamples) {
						wSum = 0.0;
						//for each feature in the example
						for (int j = 0; j < inputs.length; j++) {
							if (e.getFeatureValue(inputs[j]).equals(inputs[j].getFirstValue())) {
								featureVals[j] = 1;
								wSum += (1.0 * weights[j]); //no need to add to the sum if feature value is 0
							} else {
								featureVals[j] = 0;
							}
						}
						//System.out.println("through features");
						//-1 feature
						wSum += (-1.0 * weights[weights.length - 1]);
						
						if (wSum >= weights[weights.length - 1]) {
							predictedOut = 1;
						}
						else {
							predictedOut = 0;
						}
						
						if (e.getLabel().equals(trainExamples.getOutputLabel().getFirstValue())) {
							actualOut = 1;
						}
						else {
							actualOut = 0;
						}
						total++;
						//no weight updates
						if (predictedOut != actualOut) {

						}
						else {
							//System.out.println("hit");
							correct++;
						}
					}
					
					testAcc = (double)(correct) / (double)(total);
					//System.out.println("test " + testAcc);
					
					if (tuneAcc > bestTuneAcc) {
						//System.out.println("new best");
						bestTuneAcc = tuneAcc;
						bestTuneEpoch = i;
						testAccAtBestTune = testAcc;
					}
					
					System.out.println("epoch " + i + " train: " + trainAcc + " tune: " + tuneAcc + " test: " + testAcc);
					
				}
			}
			
			System.out.println("\nThe tune set was highest (" + bestTuneAcc + " accuracy) at Epoch " + bestTuneEpoch + ". Test set = " + testAccAtBestTune + " here.\n");
			
			for (int f = 0; f < inputs.length; f++) {
				System.out.println("Weight = " + weights[f] + " " + inputs[f].getName());
			}
			System.out.println("Threshold = " + weights[weights.length-1]);
		}
	}
}

//This class, an extension of ArrayList, holds an individual example.
//The new method PrintFeatures() can be used to
//display the contents of the example. 
//The items in the ArrayList are the feature values.
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