package deduggingIndex;

import java.util.Random;
import java.util.Scanner;

public class IndexDemo {
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

	private static void lastOcc(int[] arr, int num) {
		for (int i = arr.length - 1; i > -1; i++) {
			if (arr[i] == num) {
				System.out.println("Last Occurence of" + num + " is at " + i);
				return;
			}
		}
	}

	private static void firstocc(int[] arr, int num) {
		for (int i = 0; i <= arr.length; i++) {
			if (arr[i] == num) {
				System.out.println("First Occurence of " + num + " is at " + i);
				return;
			}
		}		
	}
}
