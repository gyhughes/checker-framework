import org.checkerframework.checker.index.qual.*;
import java.util.ArrayList;
import java.util.List;

class HierarchyIndexFor {
	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	List<Integer> lst = new ArrayList<Integer>();
	
	@UnknownIndex int i;
	
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
		i = v;
	}
	
	void assignLTLength(@LTLength("arr") int v) {
		i = v;
	}

	void assignUnknownIndex(@UnknownIndex int v) {
		i = v;
	}

	void assignIndexForB(@IndexFor("arrB") int v) {
		i = v;
	}

	void assignIndexOrHighB(@IndexOrHigh("arrB") int v) {
		i = v;
	}

	void assignIndexOrLowB(@IndexOrLow("arrB") int v) {
		i = v;
	}
	
	void assignLTLengthB(@LTLength("arrB") int v) {
		i = v;
	}
	
	void assignIndexForList(@IndexFor("lst") int v) {
		i = v;
	}

	void assignIndexOrHighList(@IndexOrHigh("arrB") int v) {
		i = v;
	}

	void assignIndexOrLowList(@IndexOrLow("List") int v) {
		i = v;
	}
	
	void assignLTLengthList(@LTLength("List") int v) {
		i = v;
	}
}