import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.UnknownIndex;

class TransferSub1 {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];

	void subIndexFor(@IndexFor("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}

	void subIndexOrLow(@IndexOrLow("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}
	
	void subIndexOrHigh(@IndexOrHigh("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}
	
	void sub1() {
		// TODO: TO BE DISCUSSED
//		// Show result is of type IndexOrHigh
//		@IndexOrHigh("") int indexOrHigh = 1 - 1;
//		//:: error: (assignment.type.incompatible)
//		@IndexFor("arr") int indexFor = 1 - 1;
	}
	
	void sub0(@IndexOrHigh("arr") int indexOrHigh) {
		// Show result is of type NonNegative
		@NonNegative int nn1 = 1 - 0;
		//:: error: (assignment.type.incompatible)
		indexOrHigh = 1 - 0;
	}
	
	void subLTLength(@LTLength("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}
	
	void subNonNegative(@NonNegative int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}

	void subUnknownIndex(@UnknownIndex int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}

	void subIndexForB(@IndexFor("arrB") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}
	
	void subIndexOrLowB(@IndexOrLow("arrB") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}

	void subIndexOrHighB(@IndexOrHigh("arrB") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}

	void subLTLengthB(@LTLength("arrB") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = 1 - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 - v;
	}
}
