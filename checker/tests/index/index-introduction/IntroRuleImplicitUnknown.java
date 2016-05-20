import java.util.ArrayList;

import org.checkerframework.checker.index.qual.*;

class IntroRuleImplicitUnknown {
	
	int[] arr = new int[5];
	
	void foo() {
		
		int v1 = -2;
		int v2 = -10;
		int v3 = Integer.MIN_VALUE;
		float f = (float) -2.0;
		int v4 = (int) f;
		
		// Idea: prove type of variable v by assigning it to a variable of type t and assigning a variable of type t to v
		@Unknown int u1 = -20;
		@Unknown int u2 = -20;
		
		// Prove type of v1,v2,v2 is subtype of Unknown
		u1 = v1;
		u1 = v2;
		u1 = v3;
		u1 = v4;
		
		// Prove v is super both LTLength and NonNeg
		//:: error:(assignment.type.incompatible)
		@LTLength("") int lt = v1;
		//:: error:(assignment.type.incompatible)
		lt = v2;
		//:: error:(assignment.type.incompatible)
		lt = v3;
		//:: error:(assignment.type.incompatible)
		lt = v4;
		//:: error:(assignment.type.incompatible)
		@NonNegative int nn = v1;
		//:: error:(assignment.type.incompatible)
		nn = v2;
		//:: error:(assignment.type.incompatible)
		nn = v3;
		//:: error:(assignment.type.incompatible)
		nn = v4;
		
	}
	
	void list(ArrayList<Integer> lst) {
		@Unknown int unknown1 = lst.size();
		@Unknown int unknown2 = lst.size() - 1;
	}
	
}