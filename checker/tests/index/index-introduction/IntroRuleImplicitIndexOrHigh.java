import org.checkerframework.checker.index.qual.*;
import java.util.*;

class IntroRuleImplicitIndexOrHigh {
	
	int[] arr = new int[5];
	List<Integer> lst = new ArrayList<>();
	
	void array() {
		int[] arrB = new int[5];
		int v2 = arr.length;
		
		// Idea: prove type of variable v by assigning it to a variable of type t and assigning a variable of type t to v
		@IndexOrHigh("arr") int indexOrHigh1 = arr.length;

		@IndexOrHigh("arrB") int indexOrHigh3 = arrB.length;
		
		// Prove type of v1,v2 is subtype of IndexOrHigh
		//indexOrHigh1 = v1; //vi is not intro'd with arr
		indexOrHigh1 = v2;
		
		// prove that v isnt a subtype of indexorHigh

		//:: error:(assignment.type.incompatible)
		@IndexFor("arr") int r = v2;
		
	}
	
	void list() {
		@IndexOrHigh("lst") int indexOrHigh1 = lst.size();
	}
	
	void arrayCreation() {
		int size = 10;
		int[] a = new int[size];
		@IndexOrHigh("a") int IOH = size;
		//:: error:(assignment.type.incompatible)
		@IndexFor("a") int InF = size;
	}
}