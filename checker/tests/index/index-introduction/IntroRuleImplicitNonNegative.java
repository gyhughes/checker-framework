import java.util.ArrayList;

import org.checkerframework.checker.index.qual.*;

class IntroRuleImplicitNonNegative {
	
	void foo() {
		
		// int v1 = 0; This case is to be discusses. Unsure whether 0 should be IndexOrHigh or NonNegative.
		int v1 = 1;
		// Integer.Max_Value cannot be used here as it is only known at run-time
		int v2 = 10000;
		
		// Idea: prove type of variable v by assigning it to a variable of type t and assigning a variable of type t to v
		@NonNegative int nn1 = 10;
		@NonNegative int nn2 = 10;
		
		// Prove type of v1,v2 is subtype of NonNegative
		nn1 = v1;
		nn1 = v2;
		
		// Prove NonNegative is subtype of type of v1,v2
		//:: error:(assignment.type.incompatible)
		@IndexOrHigh() int nn = v1;
		//:: error:(assignment.type.incompatible)
		nn = v2;
		
	}
	
	void list(ArrayList<Integer> lst) {
		@NonNegative int nonNeg1 = lst.size();
		
		//:: error:(assignment.type.incompatible)
		@NonNegative int nonNeg2 = lst.size() - 1;
	}
	
}