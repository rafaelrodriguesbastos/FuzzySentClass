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

	public Sentiment() throws IOException {

		System.out.println("Starting system...");
		
		// Define the inputs
		negativity = new Input("Negativity degree", new Tuple(0, 1));
		positivity = new Input("Positivy degree", new Tuple(0, 1));
		classification = new Output("Tweet classification", new Tuple(0, 1));

		// Set up the membership functions (MFs) for each input and output
		// Negativity input
		double lowerLowNegLimits[] = { 0.0, 0.0, 0.3, 0.4333333333333333 };
		double upperLowNegLimits[] = { 0.0, 0.0, 0.3, 0.5 };
		double lowerModerateNegLimits[] = { 0.3666666666666666, 0.45, 0.55, 0.6333333333333333 };
		double upperModerateNegLimits[] = { 0.3, 0.45, 0.55, 0.7 };
		double lowerHighNegLimits[] = { 0.5666666666666666, 0.7, 1.0, 1.0 };
		double upperHighNegLimits[] = { 0.5, 0.7, 1.0, 1.0 };

		T1MF_Trapezoidal lowerLowNegativityMF = new T1MF_Trapezoidal("MF for lower low negativity", lowerLowNegLimits);
		T1MF_Trapezoidal upperLowNegativityMF = new T1MF_Trapezoidal("MF for upper low negativity", upperLowNegLimits);
		IntervalT2MF_Trapezoidal lowNegativityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for low negativity", upperLowNegativityMF, lowerLowNegativityMF);

		T1MF_Trapezoidal lowerModerateNegativityMF = new T1MF_Trapezoidal("MF for lower moderate negativity", lowerModerateNegLimits);
		T1MF_Trapezoidal upperModerateNegativityMF = new T1MF_Trapezoidal("MF for upper moderate negativity", upperModerateNegLimits);
		IntervalT2MF_Trapezoidal moderateNegativityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for moderate negativity", upperModerateNegativityMF, lowerModerateNegativityMF);
		
		T1MF_Trapezoidal lowerHighNegativityMF = new T1MF_Trapezoidal("MF for lower high negativity", lowerHighNegLimits);
		T1MF_Trapezoidal upperHighNegativityMF = new T1MF_Trapezoidal("MF for upper high negativity", upperHighNegLimits);
		IntervalT2MF_Trapezoidal highNegativityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for high negativity", upperHighNegativityMF, lowerHighNegativityMF);


		// Positivity input
		double lowerLowPosLimits[] = { 0.0, 0.0, 0.3, 0.4333333333333333 };
		double upperLowPosLimits[] = { 0.0, 0.0, 0.3, 0.5 };
		double lowerModeratePosLimits[] = { 0.3666666666666666, 0.45, 0.55, 0.6333333333333333 };
		double upperModeratePosLimits[] = { 0.3, 0.45, 0.55, 0.7 };
		double lowerHighPosLimits[] = { 0.5666666666666666, 0.7, 1.0, 1.0 };
		double upperHighPosLimits[] = { 0.5, 0.7, 1.0, 1.0 };

		T1MF_Trapezoidal lowerLowPositivityMF = new T1MF_Trapezoidal("MF for lower low positivity", lowerLowPosLimits);
		T1MF_Trapezoidal upperLowPositivityMF = new T1MF_Trapezoidal("MF for upper low positivity", upperLowPosLimits);
		IntervalT2MF_Trapezoidal lowPositivityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for low positivity", upperLowPositivityMF, lowerLowPositivityMF);

		T1MF_Trapezoidal lowerModeratePositivityMF = new T1MF_Trapezoidal("MF for lower moderate positivity", lowerModeratePosLimits);
		T1MF_Trapezoidal upperModeratePositivityMF = new T1MF_Trapezoidal("MF for upper moderate positivity", upperModeratePosLimits);
		IntervalT2MF_Trapezoidal moderatePositivityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for moderate positivity", upperModeratePositivityMF, lowerModeratePositivityMF);
		
		T1MF_Trapezoidal lowerHighPositivityMF = new T1MF_Trapezoidal("MF for lower high positivity", lowerHighPosLimits);
		T1MF_Trapezoidal upperHighPositivityMF = new T1MF_Trapezoidal("MF for upper high positivity", upperHighPosLimits);
		IntervalT2MF_Trapezoidal highPositivityT2MF = new IntervalT2MF_Trapezoidal("T2 MF for high positivity", upperHighPositivityMF, lowerHighPositivityMF);

		
		// Classification output
		double lowerNegativeLimits[] = { 0.0, 0.0, 0.3, 0.4333333333333333 };
		double upperNegativeLimits[] = { 0.0, 0.0, 0.3, 0.5 };
		double lowerNeutralLimits[] = { 0.3666666666666666, 0.45, 0.55, 0.6333333333333333 };
		double upperNeutralLimits[] = { 0.3, 0.45, 0.55, 0.7 };
		double lowerPositiveLimits[] = { 0.5666666666666666, 0.7, 1.0, 1.0 };
		double upperPositiveLimits[] = { 0.5, 0.7, 1.0, 1.0 };

		T1MF_Trapezoidal lowerNegativeClassificationMF = new T1MF_Trapezoidal("Lower negative classification", lowerNegativeLimits);
		T1MF_Trapezoidal upperNegativeClassificationMF = new T1MF_Trapezoidal("Upper negative classification", upperNegativeLimits);
		IntervalT2MF_Trapezoidal negativeClassificationT2MF = new IntervalT2MF_Trapezoidal("T2 negative classification", upperNegativeClassificationMF, lowerNegativeClassificationMF);
		
		T1MF_Trapezoidal lowerNeutralClassificationMF = new T1MF_Trapezoidal("Lower neutral classification", lowerNeutralLimits);
		T1MF_Trapezoidal upperNeutralClassificationMF = new T1MF_Trapezoidal("Upper neutral classification", upperNeutralLimits);
		IntervalT2MF_Trapezoidal neutralClassificationT2MF = new IntervalT2MF_Trapezoidal("T2 neutral classification", upperNeutralClassificationMF, lowerNeutralClassificationMF);

		T1MF_Trapezoidal lowerPositiveClassificationMF = new T1MF_Trapezoidal("Lower positive classification", lowerPositiveLimits);
		T1MF_Trapezoidal upperPositiveClassificationMF = new T1MF_Trapezoidal("Upper positive classification", upperPositiveLimits);
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
				new IT2_Rule(new IT2_Antecedent[] { lowNegativity, moderatePositivity }, negativeClassification));
		rulebase.addRule(new IT2_Rule(new IT2_Antecedent[] { lowNegativity, highPositivity }, negativeClassification));
		rulebase.addRule(
				new IT2_Rule(new IT2_Antecedent[] { moderateNegativity, highPositivity }, negativeClassification));
		rulebase.addRule(
				new IT2_Rule(new IT2_Antecedent[] { moderateNegativity, lowPositivity }, positiveClassification));
		rulebase.addRule(
				new IT2_Rule(new IT2_Antecedent[] { highNegativity, moderatePositivity }, positiveClassification));
		rulebase.addRule(new IT2_Rule(new IT2_Antecedent[] { highNegativity, lowPositivity }, positiveClassification));

		System.out.println("Rulebases setted...");
		
		// just an example of setting the discretisation level of an output - the usual
		// level is 100 classification.setDiscretisationLevel(50);

		
//		String tweet = new String();
//		String stemmedWord;
//
		System.out.println("Reading Tweet dataset...");
		File dataset = new File("data" + File.separator + "final_clean.csv");
		String line = new String();
		String[] data = new String[6];
		Scanner lineScan = new Scanner(dataset, "UTF-8");
	    
		
	    //Creating the output file
	    String outputFileName = "output-" + java.time.LocalDateTime.now() + ".csv";
	    FileOutputStream outputFLSFile = new FileOutputStream("data/" + outputFileName);
	    
	    System.out.println("Starting FLS...");
	    

		StringBuffer outputFLS = new StringBuffer();
		outputFLS.append("sequencial; classification; tweetID; positivity; negativity; puntual; lowerLowPositivityMF; upperLowPositivityMF; lowerModeratePositivityMF; upperModeratePositivityMF; lowerHighPositivityMF; upperHighPositivityMF; lowerLowNegativityMF; upperLowNegativityMF; lowerModerateNegativityMF; upperModerateNegativityMF; lowerHighNegativityMF; upperHighNegativityMF; lowerNegativeClassificationMF; upperNegativeClassificationMF; neutralClassificationMF; lowerPositiveClassificationMF; upperPositiveClassificationMF").append("\n");
		outputFLSFile.write(outputFLS.toString().getBytes());
		
		int x = 0;
		while (lineScan.hasNextLine()) {
			x++;
			line = lineScan.nextLine();

			// split the dataset line by comma
			data = line.split(",");
			Double point = getClassification(Double.valueOf(data[4]), Double.valueOf(data[5]));
			
			outputFLS = new StringBuffer();
			//output with tweet
			outputFLS
			.append(x).append("; ")
			.append(data[3]).append("; ")
			.append(data[1]).append("; ")
			.append(Double.valueOf(data[4])).append("; ")
			.append(Double.valueOf(data[5])).append("; ").append(point).append("; ")
			.append(lowPositivityT2MF.getLowerBound(Double.valueOf(data[4]))).append("; ")
			.append(lowPositivityT2MF.getUpperBound(Double.valueOf(data[4]))).append("; ")
			.append(moderatePositivityT2MF.getLowerBound(Double.valueOf(data[4]))).append("; ")
			.append(moderatePositivityT2MF.getUpperBound(Double.valueOf(data[4]))).append("; ")
			.append(highPositivityT2MF.getLowerBound(Double.valueOf(data[4]))).append("; ")
			.append(highPositivityT2MF.getUpperBound(Double.valueOf(data[4]))).append("; ")
			.append(lowNegativityT2MF.getLowerBound(Double.valueOf(data[5]))).append("; ")
			.append(lowNegativityT2MF.getUpperBound(Double.valueOf(data[5]))).append("; ")
			.append(moderateNegativityT2MF.getLowerBound(Double.valueOf(data[5]))).append("; ")
			.append(moderateNegativityT2MF.getUpperBound(Double.valueOf(data[5]))).append("; ")
			.append(highNegativityT2MF.getLowerBound(Double.valueOf(data[5]))).append("; ")
			.append(highNegativityT2MF.getUpperBound(Double.valueOf(data[5]))).append("; ")
			.append(negativeClassificationT2MF.getLowerBound(point)).append("; ")
			.append(negativeClassificationT2MF.getUpperBound(point)).append("; ")
			.append(neutralClassificationT2MF.getLowerBound(point)).append("; ")
			.append(neutralClassificationT2MF.getUpperBound(point)).append("; ")
			.append(positiveClassificationT2MF.getLowerBound(point)).append("\n")
			.append(positiveClassificationT2MF.getUpperBound(point)).append("\n");
					
			outputFLSFile.write(outputFLS.toString().getBytes());

		}
		outputFLSFile.close();
		lineScan.close();

		System.out.println("Output file generated!");
		
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

	private Double getClassification(double negativityMeasure, double positivityMeasure) {
		// first, set the inputs
		negativity.setInput(negativityMeasure);
		positivity.setInput(positivityMeasure);
		// now execute the FLS and print output
//		System.out.println("The negativity measure was: " + negativity.getInput());
//		System.out.println("The positivity measure was: " + positivity.getInput());
//		System.out.println("Using height defuzzification " + rulebase.evaluate(0).get(classification));
//		System.out.println("Using centroid defuzzification " + rulebase.evaluate(1).get(classification));

		return rulebase.evaluate(1).get(classification);


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
