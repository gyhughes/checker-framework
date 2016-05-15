import org.checkerframework.checker.index.qual.*;

class IntroRuleIndexOrHigh {
	
	int[] arr = new int[5];
	
	void foo() {
		@IndexOrHigh("arr") int v1 = arr.length;
		//@IndexOrHigh("") int v2 = 0;
		
		// prove super indexfor
		//:: error:(assignment.type.incompatible)
		@IndexFor("arr") int IoF = v1;
	}
	
	void foo2() {
		@NonNegative int v1 = arr.length;
		@Unknown int v3 = arr.length;
	}
}