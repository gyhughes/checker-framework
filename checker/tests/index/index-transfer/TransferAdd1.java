import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.UnknownIndex;

class TransferAdd1 {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	void addIndexFor(@IndexFor("arr") int v) {
		// Show result is of type IndexOrHigh
		@IndexOrHigh("arr") int result = 1 + v;
        //:: error: (assignment.type.incompatible)
        @IndexFor("arr") int indexFor = 1 + v;
	}

	void addIndexOrLow(@IndexOrLow("arr") int v) {
		// Show result is of type IndexOrHigh
		@IndexOrHigh("arr") int result = 1 + v;
        //:: error: (assignment.type.incompatible)
        @IndexFor("arr") int indexFor = 1 + v;
	}
	
	void addIndexOrHigh(@IndexOrHigh("arr") int v) {
		// Show result is of type NonNegative
		@NonNegative int nn1 = 1 + v;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh int indexOrHigh = 1 + v;
	}
	
	void add1() {
		// Show result is of type NonNegative
		@NonNegative int nn1 = 1 + 1;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh int indexOrHigh = 1 + 1;
	}
	
	void add0() {
		// Show result is of type NonNegative
		@NonNegative int nn1 = 1 + 0;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh int indexOrHigh = 1 + 0;
	}
	
	void addLTLength(@LTLength("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = v + 1;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 + v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 + v;
	}
	
	void addNonNegative(@NonNegative int v) {
		// Show result is of type NonNegative
		@NonNegative int nn1 = 1 + v;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh int indexOrHigh = 1 + v;
	}

	void addUnknownIndex(@UnknownIndex int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = v + 1;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 + v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 + v;
	}
}
