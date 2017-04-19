import java.util.LinkedList;
import java.util.List;

/*
 * This class includes methods for basic statistical analysis and list manipulation.
 * 
 * Author: Jonggi Hong
 * Last update: 04/19/2017
 */

public class MyMath {
	public static List<Double> removeOutliers(List<Double> values){
		LinkedList<Double> outlierList = (LinkedList<Double>) getOutliers(values);
		return complementList(values, outlierList);
	}
	
	// returns a list with elements that are in list1, but not in list 2 
	public static List<Double> complementList(List<Double> list1, List<Double> list2){
		LinkedList<Double> newList = new LinkedList<Double>();
		for(double v: list1){
			if (!list2.contains(v)) newList.add(v);
		}
		return newList;
	}
	
	public static List<Double> getOutliers(List<Double> values){
		double uQuartile = upperQuartile(values);
		double lQuartile = lowerQuartile(values);
		double iqr = uQuartile - lQuartile;
		LinkedList<Double> newList = new LinkedList<Double>();
		
		for(int i=0; i<values.size(); i++){
			double v = values.get(i);
			if (v > uQuartile + 3*iqr || v < lQuartile - 3*iqr) {
				newList.add(v);
			}
		}
		
		return newList;
	}

	public static double outerLowerFence(List<Double> values) {
		double uQuartile = upperQuartile(values);
		double lQuartile = lowerQuartile(values);
		double iqr = uQuartile - lQuartile;
		
		return lQuartile - 3*iqr;
	}
	
	public static double outerUpperFence(List<Double> values) {
		double uQuartile = upperQuartile(values);
		double lQuartile = lowerQuartile(values);
		double iqr = uQuartile - lQuartile;
		
		return uQuartile + 3*iqr;
	}
	
	public static double interQuartileRange(List<Double> values){
		LinkedList<Double> sortedList = (LinkedList<Double>) sortingList(values);
		int n = sortedList.size();
		
		if (n%2 == 0){
			List<Double> lowerList = sortedList.subList(0, n/2);
			List<Double> upperList = sortedList.subList(n/2, n);
			return median(upperList) - median(lowerList);
		} else {
			List<Double> lowerList = sortedList.subList(0, (n+1)/2 - 1);
			List<Double> upperList = sortedList.subList((n+1)/2, n);
			return median(upperList) - median(lowerList);
		}
	}
	
	public static double lowerQuartile(List<Double> values) {
		LinkedList<Double> sortedList = (LinkedList<Double>) sortingList(values);
		int n = sortedList.size();
		
		double quartileN = (n-1)*0.25;
		int n1 = (int)quartileN;
		double floating = quartileN - n1;
		
		return sortedList.get(n1) + floating * (sortedList.get(n1 + 1)-sortedList.get(n1));
	}
	
	public static double upperQuartile(List<Double> values) {
		LinkedList<Double> sortedList = (LinkedList<Double>) sortingList(values);
		int n = sortedList.size();

		double quartileN = (n-1)*0.75;
		int n1 = (int)quartileN;
		double floating = quartileN - n1;
		
		return sortedList.get(n1) + floating * (sortedList.get(n1 + 1)-sortedList.get(n1));
	}
	
	public static List<Double> sortingList(List<Double> values){
		LinkedList<Double> sortedList = new LinkedList<Double>();
		LinkedList<Double> copiedList = new LinkedList<Double>();
		
		//copy the values
		for(int i=0; i<values.size(); i++){
			copiedList.add(values.get(i));
		}
		
		//sort the values
		for(int i=0; i<values.size(); i++){
			int minIdx = minIndex(copiedList);
			sortedList.add(copiedList.remove(minIdx));
		}
		
		return sortedList;
	}
	
	public static double mean (List<Double> values){
		return mean (values, 0, values.size(), null);
	}

	public static double mean(List<Double> values, int from, int to, List<Integer> exclude){
		double sum = 0;
		int count = 0;
		
		from = from < 0 ? 0 : from;
		to = to > values.size()? values.size():to;
		
		for(int i=from; i<to; i++){
			if (exclude != null && exclude.contains(i)) continue;
			sum += values.get(i);
			count++;
		}
		return sum / count;
	}
	
	
	public static double median (List<Double> values) {
		LinkedList<Double> sortedList = (LinkedList<Double>) sortingList(values);
		int n = sortedList.size();
		
		if(n%2 == 0)
			return (sortedList.get(n/2 - 1).doubleValue() + sortedList.get(n/2).doubleValue())/2;
		else
			return sortedList.get((n+1)/2 - 1).doubleValue();
	}
	
	public static double stdev (List<Double> values) {
		return Math.sqrt(variance(values));
	}
	
	public static double variance (List<Double> values) {
		double result = 0;
		double meanValue = 0;
		for(Double value: values){
			meanValue += value.doubleValue();
			result += value.doubleValue() * value.doubleValue();
		}
		meanValue /= values.size();
		return result / values.size() - meanValue * meanValue;
	}
	
	public static int minIndex (List<Double> values) {
		int index = 0;
		double maxValue = Double.MAX_VALUE;
		for(int i=0; i<values.size(); i++){
			double cValue = values.get(i).doubleValue(); 
			if (maxValue > cValue){
				index = i;
				maxValue = cValue;
			}
		}
		return index;
	}
	
	public static double minValue (List<Double> values) {
		return values.get(minIndex(values)).doubleValue();
	}
	
	public static int maxIndex (List<Double> values) {
		int index = 0;
		double maxValue = Double.MIN_VALUE;
		for(int i=0; i<values.size(); i++){
			double cValue = values.get(i).doubleValue(); 
			if (maxValue < cValue){
				index = i;
				maxValue = cValue;
			}
		}
		return index;
	}
	
	public static double maxValue(List<Double> values){
		return values.get(MyMath.maxIndex(values)).doubleValue();
	}
	
	public static double doublSum (List<Double> values){
		double s = 0;
		for(double v: values) {
			s += v;
		}
		return s;
	}
	
	public static int intSum (List<Integer> values){
		int s = 0;
		for(int v: values) {
			s += v;
		}
		return s;
	}
}
