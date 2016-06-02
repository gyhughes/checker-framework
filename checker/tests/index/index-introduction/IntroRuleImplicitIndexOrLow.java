import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.index.qual.*;

import lubglb.quals.E;

//Doesn't test anything yet.

class IntroRuleImplicitIndexOrLow {
	
	int[] arr = new int[5];
	
	void foo() {
		
		int v1 = -1;
		
		// Idea: prove type of variable v by assigning it to a variable of type t and assigning a variable of type t to v
		@IndexOrLow("arr") int indexOrLow1 = arr.length -1;
		@IndexOrLow("arr") int indexOrLow2 = arr.length -1;
		
		// Prove type of v1 is subtype of IndexOrLow
		//@IndexOrLow("") int indexOrLow1 = v1;
		//@IndexOrLow("") int indexOrLow2 = -1;
		// Prove IndexOrLow is subtype of type of v1
		//:: error:(assignment.type.incompatible)
		@IndexFor("arr") int m = v1;

		
	}
	
	void list(ArrayList<Integer> lst) {
		@IndexOrLow("lst") int indexOrLow1 = lst.size() - 1;
		
		//:: error:(assignment.type.incompatible)
		@IndexOrLow("lst") int indexOrLow2 = lst.size();
	}
	
	void indexOf(List<E> lst, String str) {
		int stri = str.indexOf('a');
		if (stri != -1) {
			char k = str.charAt(stri);
		}
		int lsti = lst.indexOf(str);
		if (lsti != -1) {
			Object o = lst.get(lsti);
		}
	}
	
}