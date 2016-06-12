import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.UnknownIndex;

class TransferAddNonNegative {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	// The NonNegative variable.
	@NonNegative int i = 2;
	
	void addIndexFor(@IndexFor("arr") int v) {
		// Show result is of type NonNegative
		@NonNegative int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh("a") int indexOrHigh = v + i;
	}

	void addIndexOrLow(@IndexOrLow("arr") int v) {
		// Show result is of type UnknownIndex
				@UnknownIndex int nn1 = v + i;
				//:: error: (assignment.type.incompatible)
				@NonNegative int nn = v + i;
				//:: error: (assignment.type.incompatible)
				@LTLength("arr") int lTLength = v + i;
	}
	
	void addIndexOrHigh(@IndexOrHigh("arr") int v) {
		// Show result is of type NonNegative
		@NonNegative int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh("a") int indexOrHigh = v + i;
	}
	
	void add1() {
		// Show result is of type NonNegative
		@NonNegative int nn1 = 1 + i;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh("a") int indexOrHigh = 1 + i;
	}
	
	void add0() {
		// Show result is of type NonNegative
		@NonNegative int nn1 = 0 + i;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh("a") int indexOrHigh = 0 + i;
	}
	
	void addLTLength(@LTLength("arr") int v) {
		// Show result is of type UnknownIndex
				@UnknownIndex int nn1 = v + i;
				//:: error: (assignment.type.incompatible)
				@NonNegative int nn = v + i;
				//:: error: (assignment.type.incompatible)
				@LTLength("arr") int lTLength = v + i;
	}
	
	void addNonNegative(@NonNegative int v) {
		// Show result is of type NonNegative
		@NonNegative int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh("a") int indexOrHigh = v + i;
	}

	void addUnknownIndex(@UnknownIndex int v) {
		// Show result is of type UnknownIndex
				@UnknownIndex int nn1 = v + i;
				//:: error: (assignment.type.incompatible)
				@NonNegative int nn = v + i;
				//:: error: (assignment.type.incompatible)
				@LTLength("arr") int lTLength = v + i;
	}
}
