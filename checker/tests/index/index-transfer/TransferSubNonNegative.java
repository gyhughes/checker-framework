import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.UnknownIndex;

class TransferSubNonNegative {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	@NonNegative int i = 10;
	
	void subIndexFor(@IndexFor("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}

	void subIndexOrLow(@IndexOrLow("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
	
	void subIndexOrHigh(@IndexOrHigh("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
	
	void sub1() {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - 1;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - 1;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - 1;
	}
	
	void sub0() {
		// Show result is of type NonNegative
		@NonNegative int nn1 = i - 0;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh int indexOrHigh = i - 0;
	}
	
	void subLTLength(@LTLength("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
	
	void subNonNegative(@NonNegative int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}

	void subUnknownIndex(@UnknownIndex int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}

	void subIndexForB(@IndexFor("arrB") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
	
	void subIndexOrLowB(@IndexOrLow("arrB") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}

	void subIndexOrHighB(@IndexOrHigh("arrB") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}

	void subLTLengthB(@LTLength("arrB") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
}