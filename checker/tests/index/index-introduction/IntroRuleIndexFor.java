import org.checkerframework.checker.index.qual.*;

class IntroRuleIndexFor {
	
	void main() {
		int i = 4;
		int[] arr = new int[i + 1];
		int m = arr[i];
	}
	
	void foo() {
		int i = 4;
		int[] arr = new int[i + 1];
		@IndexFor("arr") int m = i;
		//:: error:(assignment.type.incompatible)
		@IndexBottom int bot = i;
		@IndexOrHigh("arr") int IOH = i;
		@IndexOrLow("arr") int IOL = i;
	}
}