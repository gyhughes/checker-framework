import org.checkerframework.checker.index.qual.*;

class TypeRuleIndexFor {
	void foo(int[][] arr) {
		for (int i = 0; i< arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				int x = arr[i][j];
			}
		}
	}
}
