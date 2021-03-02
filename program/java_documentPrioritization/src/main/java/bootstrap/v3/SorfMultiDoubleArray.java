package bootstrap.v3;

import java.util.Arrays;
import java.util.Comparator;

public class SorfMultiDoubleArray {

	public static void main(String[] args) {

		double ch1[][] = new double[][] { { 0.25, 100 }, { 0.75, 80 }, { 0.5, 120 }, { 0.8, 150 } };
		double ch2[][] = new double[][] { { 0.25, 100,1 }, { 0.75, 80,2 }, { 0.5, 120,3 }, { 0.8, 150,4 } };
		double ch3[][] = new double[][] { { 0.8, 1 }, { 0.8, 0 }, { 0.8, 0 }, { 0.9, 1 } };
//
//		AryItem[] itemAry = new AryItem[ch1.length];
//
//		for (int i = 0; i < ch1.length; i++) {
//			itemAry[i] = new AryItem(ch1[i][0], ch1[i][1]);
//		}
//
//		Arrays.sort(itemAry, new DoubleArrayComparator2());
//
//		for (AryItem item : itemAry) {
//			System.out.println(item.toString());
//		}

		SorfMultiDoubleArray s = new SorfMultiDoubleArray();
		ch3 = s.soft_second_ASC(ch3);
		ch3 = s.soft(ch3);
		ch1 = s.soft(ch1);
		ch2 = s.soft_threeItems(ch2);
	}
	
	public double[][] soft(double[][] data)
	{
//		double data[][] = new double[][] { { 0.25, 100 }, { 0.75, 80 }, { 0.5, 120 }, { 0.8, 150 } };
		
		AryItem[] itemAry = new AryItem[data.length];

		for (int i = 0; i < data.length; i++) {
			itemAry[i] = new AryItem(data[i][0], data[i][1]);
		}

		Arrays.sort(itemAry, new DoubleArrayComparator());
		
		int i = 0;
		for (AryItem item : itemAry) {
			data[i][0] = item.firstElement;
			data[i][1] = item.secondElement;
//			System.out.printf("%f   %f\n",data[i][0],data[i][1]);
			i++;
		}
		
		return data;
	}
	
	public double[][] soft_second_ASC(double[][] data)
	{
//		double data[][] = new double[][] { { 0.25, 100 }, { 0.75, 80 }, { 0.5, 120 }, { 0.8, 150 } };
		
		AryItem[] itemAry = new AryItem[data.length];

		for (int i = 0; i < data.length; i++) {
			itemAry[i] = new AryItem(data[i][0], data[i][1]);
		}

		Arrays.sort(itemAry, new DoubleArrayComparator_secondElement_ASC());
		
		int i = 0;
		for (AryItem item : itemAry) {
			data[i][0] = item.firstElement;
			data[i][1] = item.secondElement;
//			System.out.printf("%f   %f\n",data[i][0],data[i][1]);
			i++;
		}
		
		return data;
	}
	
	public double[][] soft_threeItems(double[][] data)
	{
		AryItem[] itemAry = new AryItem[data.length];

		for (int i = 0; i < data.length; i++) {
			itemAry[i] = new AryItem(data[i][0], data[i][1], data[i][2]);
		}

		Arrays.sort(itemAry, new DoubleArrayComparator());
		
		int i = 0;
		for (AryItem item : itemAry) {
			data[i][0] = item.firstElement;
			data[i][1] = item.secondElement;
			data[i][2] = item.thirdElement;
//			System.out.printf("%f   %f   %f\n",data[i][0],data[i][1],data[i][2]);
			i++;
		}
		
		return data;
	}
	
}

class DoubleArrayComparator implements Comparator<AryItem> {

	public int compare(AryItem o1, AryItem o2) {
		if (o1.getFirstElement() < o2.getFirstElement()) {
			return 1;
		} else if (o1.getFirstElement() == o2.getFirstElement()) {
			return 0;
		} else {
			return -1;
		}
	}

}

class DoubleArrayComparator_secondElement_ASC implements Comparator<AryItem> {

	public int compare(AryItem o1, AryItem o2) {
		if (o1.getSecondElement() > o2.getSecondElement()) {
			return 1;
		} else if (o1.getSecondElement() == o2.getSecondElement()) {
			return 0;
		} else {
			return -1;
		}
	}

}

class AryItem {
	public double firstElement;

	public double secondElement;
	
	public double thirdElement;

	public AryItem(double first, double second) {
		this.firstElement = first;
		this.secondElement = second;
	}
	
	public AryItem(double first, double second, double third) {
		this.firstElement = first;
		this.secondElement = second;
		this.thirdElement = third;
	}

	public double getFirstElement() {
		return firstElement;
	}
	
	public double getSecondElement() {
		return secondElement;
	}
	
	public double getThirdElement() {
		return thirdElement;
	}

	public String toString() {
		return firstElement + "\t" + secondElement + "\t" + thirdElement;
	}
}
