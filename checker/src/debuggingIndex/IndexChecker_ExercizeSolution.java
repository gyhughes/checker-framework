package debuggingIndex;

import java.util.Random;
import java.util.Scanner;
import org.checkerframework.checker.index.qual.*;

public class IndexChecker_ExercizeSolution {

	/**
	 * There are two errors in this class related to indexing. Try to find them.
	 * There is one false warning. Try to suppress it.
	 */
	public static void main(String[] args) {
		Random r = new Random();
		int[] arr = new int[100];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = r.nextInt(9)+1;
		}
		Scanner con = new Scanner(System.in);
		System.out.println("input a number between 1 and 10");
		int num = con.nextInt();
		firstocc(arr, num);
		lastOcc(arr, num);
		numOcc(arr, num);
		firstTen(arr);
	}

	private static void numOcc(int[] arr, int num) {
		int count = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == num) {
				count++;
			}
		}
		System.out.println(num + " occurs " + count + " times");
		
	}

	//print the index of the first occurance of num in arr
	private static void firstocc(int[] arr, int num) {
		//ERROR: incrementing up to arr.length instead of arr.length - 1
//		for (int i = 0; i <= arr.length; i++) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == num) {
				System.out.println("First Occurence of " + num + " is at " + i);
				return;
			}
		}		
	}

	//print the index of the last occurance of num in arr
	private static void lastOcc(int[] arr, int num) {
		//ERROR: trying to increment up whilst at the end of the array
//		for (int i = arr.length - 1; i > -1; i++) {
		for (int i = arr.length - 1; i > -1; i--) {
			if (arr[i] == num) {
				System.out.println("Last Occurence of" + num + " is at " + i);
				return;
			}
		}
	}

	//print the first 10 values in arr. arr.length must be at least 10
	private static void firstTen(int[] arr) {
		if (arr.length < 10)
			throw new IllegalArgumentException();

		for (int i = 0; i < 10; i++) {

			//FALSE WARNING: the IndexChecker does not keep track of array lengths,
			//	just whether or not indecies were compared against array bounds.
			//System.out.println("arr[" + i + "] == " + arr[i]);

			// Option 1: add redudant bound checking
			//if (i < arr.length)
			//	System.out.println("arr[" + i + "] == " + arr[i]);

			// Option 2: Cast to local @IndexFor variable and suppress warning
			@SuppressWarnings("index")
			@IndexFor("arr") int j = i;
			System.out.println("arr[" + i + "] == " + arr[j]);
		}		
	}
}
