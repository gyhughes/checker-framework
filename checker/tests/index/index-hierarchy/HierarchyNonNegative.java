import org.checkerframework.checker.index.qual.*;
import java.util.ArrayList;
import java.util.List;

class HiearchyNonNegative {
	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	List<Integer> lst = new ArrayList<Integer>();
	
	@NonNegative int i;
	
	void assignIndexFor(@IndexFor("arr") int v) {
		i = v;
	}
	
	void assignIndexOrHigh(@IndexOrHigh("arr") int v) {
		i = v;
	}
	
	void assignNonNegative(@NonNegative int v) {
		i = v;
	}

	void assignIndexOrLow(@IndexOrLow("arr") int v) {
		//:: error: (assignment.type.incompatible)
		i = v;
	}
	
	void assignLTLength(@LTLength("arr") int v) {
		//:: error: (assignment.type.incompatible)
		i = v;
	}

	void assignUnknownIndex(@UnknownIndex int v) {
		//:: error: (assignment.type.incompatible)
		i = v;
	}

	void assignIndexForB(@IndexFor("arrB") int v) {
		i = v;
	}

	void assignIndexOrHighB(@IndexOrHigh("arrB") int v) {
		i = v;
	}

	void assignIndexOrLowB(@IndexOrLow("arrB") int v) {
		//:: error: (assignment.type.incompatible)
		i = v;
	}
	
	void assignLTLengthB(@LTLength("arrB") int v) {
		//:: error: (assignment.type.incompatible)
		i = v;
	}
	
	void assignIndexForList(@IndexFor("lst") int v) {
		i = v;
	}

	void assignIndexOrHighList(@IndexOrHigh("lst") int v) {
		i = v;
	}

	void assignIndexOrLowList(@IndexOrLow("lst") int v) {
		//:: error: (assignment.type.incompatible)
		i = v;
	}

	void assignLTLengthList(@LTLength("lst") int v) {
		//:: error: (assignment.type.incompatible)
		i = v;
	}
}