/*
 * Sentiment.java
 *
 * Created on Oct 07th 2022
 *
 * Based on Juzzy by Christian Wagner
 */
package example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

import generic.Input;
import generic.Output;
import generic.Tuple;
import intervalType2.sets.IntervalT2MF_Interface;
import intervalType2.sets.IntervalT2MF_Trapezoidal;
import intervalType2.system.IT2_Antecedent;
import intervalType2.system.IT2_Consequent;
import intervalType2.system.IT2_Rule;
import intervalType2.system.IT2_Rulebase;
import tools.JMathPlotter;
import type1.sets.T1MF_Trapezoidal;

/**
 * A simple example of a type-1 FLS based on the "Sentiment Analysis".
 * 
 * @author Rafael Bastos
 */
public class Sentiment {
	Input negativity, positivity; // the inputs to the FLS
	Output classification; // the output of the FLS
	IT2_Rulebase rulebase; // the rulebase captures the entire FLS
	String linguisticClassification;
	int accuracy;
	int accuracyCount = 0;
	
	private Double OutputXValue;
    private Double OutputYValue;

	public Sentiment() throws IOException {

		System.out.println("Starting system...");
		
		// Define the inputs
		negativity = new Input("Negativity degree", new Tuple(0, 1));
		positivity = new Input("Positivy degree", new Tuple(0, 1));
		classification = new Output("Tweet classification", new Tuple(0, 1));

		// Set up the membership functions (MFs) for each input and output
		// Negativity input
		//MF1='baixa':'itrapatype2',[-0.227 -0.09 0.28 0.45 -0.133 -0.00188 0.3 0.55 0.9]		
		double lowerLowNegLimits[] = { -0.133, -0.00188, 0.28, 0.45 };
		double upperLowNegLimits[] = { -0.227, -0.09, 0.3, 0.55 };
		double lowerModerateNegLimits[] = { 0.33, 0.46, 0.54, 0.65 };
		double upperModerateNegLimits[] = { 0.25, 0.38, 0.63, 0.75 };
		double lowerHighNegLimits[] = { 0.55, 0.72, 1.019, 1.234 };
		double upperHighNegLimits[] = { 0.45, 0.7, 1.119, 1.334 };
		
		double lowerLevels[] = {0.9, 0.9};
		double upperLevels[] = {1.0, 1.0};

		T1MF_Trapezoidal lowerLowNegativityMF = new T1MF_Trapezoidal("MF for lower low negativity", lowerLowNegLimits, lowerLevels);
		T1MF_Trapezoidal upperLowNegativityMF = new T1MF_Trapezoidal("MF for upper low negativity", upperLowNegLimits, upperLevels);
		IntervalT2MF_Trapezoidal lowNegativityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for low negativity", upperLowNegativityMF, lowerLowNegativityMF);

		T1MF_Trapezoidal lowerModerateNegativityMF = new T1MF_Trapezoidal("MF for lower moderate negativity", lowerModerateNegLimits, lowerLevels);
		T1MF_Trapezoidal upperModerateNegativityMF = new T1MF_Trapezoidal("MF for upper moderate negativity", upperModerateNegLimits, upperLevels);
		IntervalT2MF_Trapezoidal moderateNegativityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for moderate negativity", upperModerateNegativityMF, lowerModerateNegativityMF);
		
		T1MF_Trapezoidal lowerHighNegativityMF = new T1MF_Trapezoidal("MF for lower high negativity", lowerHighNegLimits, lowerLevels);
		T1MF_Trapezoidal upperHighNegativityMF = new T1MF_Trapezoidal("MF for upper high negativity", upperHighNegLimits, upperLevels);
		IntervalT2MF_Trapezoidal highNegativityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for high negativity", upperHighNegativityMF, lowerHighNegativityMF);


		// Positivity input
		double lowerLowPosLimits[] = { -0.133, -0.00188, 0.28, 0.45 };
		double upperLowPosLimits[] = { -0.227, -0.09, 0.3, 0.55 };
		double lowerModeratePosLimits[] = { 0.33, 0.46, 0.54, 0.65 };
		double upperModeratePosLimits[] = { 0.25, 0.38, 0.63, 0.75 };
		double lowerHighPosLimits[] = { 0.55, 0.72, 1.019, 1.234 };
		double upperHighPosLimits[] = { 0.45, 0.7, 1.119, 1.334 };

		T1MF_Trapezoidal lowerLowPositivityMF = new T1MF_Trapezoidal("MF for lower low positivity", lowerLowPosLimits, lowerLevels);
		T1MF_Trapezoidal upperLowPositivityMF = new T1MF_Trapezoidal("MF for upper low positivity", upperLowPosLimits, upperLevels);
		IntervalT2MF_Trapezoidal lowPositivityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for low positivity", upperLowPositivityMF, lowerLowPositivityMF);

		T1MF_Trapezoidal lowerModeratePositivityMF = new T1MF_Trapezoidal("MF for lower moderate positivity", lowerModeratePosLimits, lowerLevels);
		T1MF_Trapezoidal upperModeratePositivityMF = new T1MF_Trapezoidal("MF for upper moderate positivity", upperModeratePosLimits, upperLevels);
		IntervalT2MF_Trapezoidal moderatePositivityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for moderate positivity", upperModeratePositivityMF, lowerModeratePositivityMF);
		
		T1MF_Trapezoidal lowerHighPositivityMF = new T1MF_Trapezoidal("MF for lower high positivity", lowerHighPosLimits, lowerLevels);
		T1MF_Trapezoidal upperHighPositivityMF = new T1MF_Trapezoidal("MF for upper high positivity", upperHighPosLimits, upperLevels);
		IntervalT2MF_Trapezoidal highPositivityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for high positivity", upperHighPositivityMF, lowerHighPositivityMF);

		
		// Classification output
//		double lowerNegativeLimits[] = { 0.0, 0.0, 0.3, 0.4333333333333333 };
//		double upperNegativeLimits[] = { 0.0, 0.0, 0.3, 0.5 };
//		double lowerNeutralLimits[] = { 0.3666666666666666, 0.45, 0.55, 0.6333333333333333 };
//		double upperNeutralLimits[] = { 0.3, 0.45, 0.55, 0.7 };
//		double lowerPositiveLimits[] = { 0.5666666666666666, 0.7, 1.0, 1.0 };
//		double upperPositiveLimits[] = { 0.5, 0.7, 1.0, 1.0 };
		
		double lowerNegativeLimits[] = { -0.133, -0.00188, 0.28, 0.45 };
		double upperNegativeLimits[] = { -0.227, -0.09, 0.3, 0.55 };
		double lowerNeutralLimits[] = { 0.33, 0.46, 0.54, 0.65 };
		double upperNeutralLimits[] = { 0.25, 0.38, 0.63, 0.75 };
		double lowerPositiveLimits[] = { 0.55, 0.72, 1.019, 1.234 };
		double upperPositiveLimits[] = { 0.45, 0.7, 1.119, 1.334 };

		
		T1MF_Trapezoidal lowerNegativeClassificationMF = new T1MF_Trapezoidal("Lower negative classification", lowerNegativeLimits, lowerLevels);
		T1MF_Trapezoidal upperNegativeClassificationMF = new T1MF_Trapezoidal("Upper negative classification", upperNegativeLimits, upperLevels);
		IntervalT2MF_Trapezoidal negativeClassificationT2MF = new IntervalT2MF_Trapezoidal("T2 negative classification", upperNegativeClassificationMF, lowerNegativeClassificationMF);
		
		T1MF_Trapezoidal lowerNeutralClassificationMF = new T1MF_Trapezoidal("Lower neutral classification", lowerNeutralLimits, lowerLevels);
		T1MF_Trapezoidal upperNeutralClassificationMF = new T1MF_Trapezoidal("Upper neutral classification", upperNeutralLimits, upperLevels);
		IntervalT2MF_Trapezoidal neutralClassificationT2MF = new IntervalT2MF_Trapezoidal("T2 neutral classification", upperNeutralClassificationMF, lowerNeutralClassificationMF);

		T1MF_Trapezoidal lowerPositiveClassificationMF = new T1MF_Trapezoidal("Lower positive classification", lowerPositiveLimits, lowerLevels);
		T1MF_Trapezoidal upperPositiveClassificationMF = new T1MF_Trapezoidal("Upper positive classification", upperPositiveLimits, upperLevels);
		IntervalT2MF_Trapezoidal positiveClassificationT2MF = new IntervalT2MF_Trapezoidal("T2 positive classification", upperPositiveClassificationMF, lowerPositiveClassificationMF);

		System.out.println("Membership functions setted...");
		
		// Set up the antecedents and consequents - note how the inputs are associated...
		IT2_Antecedent lowNegativity = new IT2_Antecedent("Low Negativity", lowNegativityT2MF, negativity);
		IT2_Antecedent moderateNegativity = new IT2_Antecedent("Moderate Negativity", moderateNegativityT2MF, negativity);
		IT2_Antecedent highNegativity = new IT2_Antecedent("High Negativity", highNegativityT2MF, negativity);

		IT2_Antecedent lowPositivity = new IT2_Antecedent("Low Positivity", lowPositivityT2MF, positivity);
		IT2_Antecedent moderatePositivity = new IT2_Antecedent("Moderate Positivity", moderatePositivityT2MF, positivity);
		IT2_Antecedent highPositivity = new IT2_Antecedent("High Positivity", highPositivityT2MF, positivity);

		IT2_Consequent negativeClassification = new IT2_Consequent("Negative", negativeClassificationT2MF, classification);
		IT2_Consequent neutralClassification = new IT2_Consequent("Neutral", neutralClassificationT2MF, classification);
		IT2_Consequent positiveClassification = new IT2_Consequent("Positive", positiveClassificationT2MF, classification);

		System.out.println("Antecedents and consequents setted...");
		
		// Set up the rulebase and add rules 
		rulebase = new IT2_Rulebase(9);
		rulebase.addRule(new IT2_Rule(new IT2_Antecedent[] { lowNegativity, lowPositivity }, neutralClassification));
		rulebase.addRule(
				new IT2_Rule(new IT2_Antecedent[] { moderateNegativity, moderatePositivity }, neutralClassification));
		rulebase.addRule(new IT2_Rule(new IT2_Antecedent[] { highNegativity, highPositivity }, neutralClassification));
		rulebase.addRule(
				new IT2_Rule(new IT2_Antecedent[] { moderateNegativity, lowPositivity }, negativeClassification));
		rulebase.addRule(new IT2_Rule(new IT2_Antecedent[] { highNegativity, lowPositivity }, negativeClassification));
		rulebase.addRule(
				new IT2_Rule(new IT2_Antecedent[] { highNegativity, moderatePositivity }, negativeClassification));
		rulebase.addRule(
				new IT2_Rule(new IT2_Antecedent[] { lowNegativity, moderatePositivity }, positiveClassification));
		rulebase.addRule(
				new IT2_Rule(new IT2_Antecedent[] { moderateNegativity, highPositivity }, positiveClassification));
		rulebase.addRule(new IT2_Rule(new IT2_Antecedent[] { lowNegativity, highPositivity }, positiveClassification));

		System.out.println("Rulebases setted...");
		
		// just an example of setting the discretisation level of an output - the usual
		// level is 100 classification.setDiscretisationLevel(50);

		
		System.out.println("Reading Tweet dataset...");
		File dataset = new File("data" + File.separator + "final_7.csv");
		String line = new String();
		String[] data = new String[5];
		Scanner lineScan = new Scanner(dataset, "UTF-8");
	    
		
	    //Creating the output file
	    String outputFileName = "output-" + java.time.LocalDateTime.now() + ".csv";
	    FileOutputStream outputFLSFile = new FileOutputStream("data/" + outputFileName);
	    
	    System.out.println("Starting FLS...");
	    

		StringBuffer outputFLS = new StringBuffer();
		outputFLS.append("sequencial; classification; tweetID; positivity; negativity; punctual; Xinf; Xsup; lowerLowPositivityMF; upperLowPositivityMF; lowerModeratePositivityMF; upperModeratePositivityMF; lowerHighPositivityMF; upperHighPositivityMF; lowerLowNegativityMF; upperLowNegativityMF; lowerModerateNegativityMF; upperModerateNegativityMF; lowerHighNegativityMF; upperHighNegativityMF; lowerNegativeClassificationMF; upperNegativeClassificationMF; lowerNeutralClassificationMF; upperNeutralClassificationMF; lowerPositiveClassificationMF; upperPositiveClassificationMF; linguisticClassification; accuracy").append("\n");
		outputFLSFile.write(outputFLS.toString().getBytes());
		
		int x = 0;
		lineScan.nextLine();
		while (lineScan.hasNextLine()) {
			x++;
			line = lineScan.nextLine();

			// split the dataset line by comma
			data = line.split(",");
			Double point = getClassification(Double.valueOf(data[3]), Double.valueOf(data[4]));
			
			outputFLS = new StringBuffer();

			//Get the lower and upper bound of MF for each input variable
			//positivity
			double lowerLowPositivity = lowPositivityT2MF.getLowerBound(Double.valueOf(data[3]));
			double upperLowPositivity = lowPositivityT2MF.getUpperBound(Double.valueOf(data[3]));
			double lowerModeratePositivity = moderatePositivityT2MF.getLowerBound(Double.valueOf(data[3]));
			double upperModeratePositivity = moderatePositivityT2MF.getUpperBound(Double.valueOf(data[3]));
			double lowerHighPositivity = highPositivityT2MF.getLowerBound(Double.valueOf(data[3]));
			double upperHighPositivity = highPositivityT2MF.getUpperBound(Double.valueOf(data[3]));
			//negativity
			double lowerLowNegativity = lowNegativityT2MF.getLowerBound(Double.valueOf(data[4]));
			double upperLowNegativity = lowNegativityT2MF.getUpperBound(Double.valueOf(data[4]));
			double lowerModerateNegativity = moderateNegativityT2MF.getLowerBound(Double.valueOf(data[4]));
			double upperModerateNegativity = moderateNegativityT2MF.getUpperBound(Double.valueOf(data[4]));
			double lowerHighNegativity = highNegativityT2MF.getLowerBound(Double.valueOf(data[4]));
			double upperHighNegativity = highNegativityT2MF.getUpperBound(Double.valueOf(data[4]));
			
			//Get the lower and upper bound of MF for punctual output
			double lowerNegativeClassification = negativeClassificationT2MF.getLowerBound(point);
			double upperNegativeClassification = negativeClassificationT2MF.getUpperBound(point);
			double lowerNeutralClassification = neutralClassificationT2MF.getLowerBound(point);
			double upperNeutralClassification = neutralClassificationT2MF.getUpperBound(point);
			double lowerPositiveClassification = positiveClassificationT2MF.getLowerBound(point);
			double upperPositiveClassification = positiveClassificationT2MF.getUpperBound(point);

			double avgNegativeClassification = (upperNegativeClassification + lowerNegativeClassification)/2;
			double avgNeutralClassification = (upperNeutralClassification + lowerNeutralClassification)/2;
			double avgPositiveClassification = (upperPositiveClassification + lowerPositiveClassification)/2;
			
			if (avgNegativeClassification > avgNeutralClassification && avgNegativeClassification > avgPositiveClassification) {
				linguisticClassification = "negative";
			}
			else if (avgNeutralClassification > avgNegativeClassification && avgNeutralClassification > avgPositiveClassification) {
				linguisticClassification = "neutral";
			}
			else if (avgPositiveClassification > avgNegativeClassification && avgPositiveClassification > avgNeutralClassification) {
				linguisticClassification = "positive";
			}
			else if (avgPositiveClassification == avgNegativeClassification && avgPositiveClassification == avgNeutralClassification) {
				linguisticClassification = "neutral";
			}
			
			if (linguisticClassification.equals(data[2].toString())) {
				accuracy = 1;
				accuracyCount++;
			}
			else {
				accuracy = 0;
			}
			
			//Write the FLS output of sentiment analysis 
			outputFLS
			.append(x).append("; ")
			.append(data[2]).append("; ")
			.append(data[0]).append("; ")
			.append(Double.valueOf(data[3])).append("; ")
			.append(Double.valueOf(data[4])).append("; ").append(point).append("; ")
			.append(this.OutputXValue).append("; ").append(this.OutputYValue).append("; ")
			.append(lowerLowPositivity).append("; ")
			.append(upperLowPositivity).append("; ")
			.append(lowerModeratePositivity).append("; ")
			.append(upperModeratePositivity).append("; ")
			.append(lowerHighPositivity).append("; ")
			.append(upperHighPositivity).append("; ")
			.append(lowerLowNegativity).append("; ")
			.append(upperLowNegativity).append("; ")
			.append(lowerModerateNegativity).append("; ")
			.append(upperModerateNegativity).append("; ")
			.append(lowerHighNegativity).append("; ")
			.append(upperHighNegativity).append("; ")
			.append(lowerNegativeClassification).append("; ")
			.append(upperNegativeClassification).append("; ")
			.append(lowerNeutralClassification).append("; ")
			.append(upperNeutralClassification).append("; ")
			.append(lowerPositiveClassification).append("; ")
			.append(upperPositiveClassification).append("; ")
			.append(linguisticClassification).append("; ")
			.append(accuracy).append("\n");

			
			outputFLSFile.write(outputFLS.toString().getBytes());

		}
		outputFLSFile.close();
		lineScan.close();

		System.out.println("Output file generated!");
		
		double percent = ((double) accuracyCount / (double) x)*100.0;
		
		System.out.println(percent + "% accuracy!");
		
		
		// plot some sets, discretizing each input into 100 steps.
		plotMFs("Negativity Membership Functions",
				new IntervalT2MF_Interface [] { lowNegativityT2MF, moderateNegativityT2MF, highNegativityT2MF },
				negativity.getDomain(), 100);
		plotMFs("Positivity Membership Functions",
				new IntervalT2MF_Interface[] { lowPositivityT2MF, moderatePositivityT2MF, highPositivityT2MF },
				positivity.getDomain(), 100);
		plotMFs("Classification Membership Functions",
				new IntervalT2MF_Interface[] { negativeClassificationT2MF, neutralClassificationT2MF, positiveClassificationT2MF },
				classification.getDomain(), 100);

		// plot control surface
		// do either height defuzzification (false) or centroid d. (true)
		plotControlSurface(true, 10, 10);

		// print out the rules
		System.out.println("\n" + rulebase);
		 
		 
	}

	/**
	 * Basic method that prints the output for a given set of inputs.
	 * 
	 * @param negativityMeasure
	 * @param positivityMeasure
	 * @throws IOException
	 */

	private Double getClassification(double positivityMeasure, double negativityMeasure) {
		// first, set the inputs
		negativity.setInput(negativityMeasure);
		positivity.setInput(positivityMeasure);
		// now execute the FLS and print output
//		System.out.println("The negativity measure was: " + negativity.getInput());
//		System.out.println("The positivity measure was: " + positivity.getInput());
//		System.out.println("Using height defuzzification " + rulebase.evaluate(0).get(classification));
//		System.out.println("Using centroid defuzzification " + rulebase.evaluate(1).get(classification));

		//this.OutputXValue = rulebase.evaluateGetCentroid(0)

		TreeMap<Output, Object[]> centroid = rulebase.evaluateGetCentroid(1); //0 Center of sets, 1 Centroid
    	Object[] centroidTip = centroid.get(classification);
        Tuple centroidTipXValues = (Tuple)centroidTip[0];
				
        this.OutputXValue = centroidTipXValues.getLeft();
        this.OutputYValue = centroidTipXValues.getRight();
                
		//return rulebase.evaluate(1).get(classification);
		return centroidTipXValues.getAverage();
		
	}

	private void plotMFs(String name, IntervalT2MF_Interface[] sets, Tuple xAxisRange, int discretizationLevel) {
		JMathPlotter plotter = new JMathPlotter();
		for (int i = 0; i < sets.length; i++) {
			plotter.plotMF(sets[i].getName(), sets[i], discretizationLevel, null, false);
		}
		plotter.show(name);
	}

	private void plotControlSurface(boolean useCentroidDefuzzification, int input1Discs, int input2Discs) {
		double output;
		double[] x = new double[input1Discs];
		double[] y = new double[input2Discs];
		double[][] z = new double[y.length][x.length];
		double incrX, incrY;
		incrX = negativity.getDomain().getSize() / (input1Discs - 1.0);
		incrY = positivity.getDomain().getSize() / (input2Discs - 1.0);

		// first, get the values
		for (int currentX = 0; currentX < input1Discs; currentX++) {
			x[currentX] = currentX * incrX;
		}
		for (int currentY = 0; currentY < input2Discs; currentY++) {
			y[currentY] = currentY * incrY;
		}

		for (int currentX = 0; currentX < input1Discs; currentX++) {
			negativity.setInput(x[currentX]);
			for (int currentY = 0; currentY < input2Discs; currentY++) {
				positivity.setInput(y[currentY]);
				if (useCentroidDefuzzification)
					output = rulebase.evaluate(1).get(classification);
				else
					output = rulebase.evaluate(0).get(classification);
				z[currentY][currentX] = output;
			}
		}

		// now do the plotting
		JMathPlotter plotter = new JMathPlotter(17, 17, 14);
		plotter.plotControlSurface("Control Surface",
				new String[] { negativity.getName(), positivity.getName(), "Classification" }, x, y, z,
				new Tuple(0.0, 1.0), true);
		plotter.show("Type-1 Fuzzy Logic System Control Surface for Sentiment Analysis");
	}

	public static void main(String args[]) throws IOException {
		new Sentiment();
	}
}