import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.UnknownIndex;

class TransferSubIndexFor {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	// This error: can be suppressed
	//:: error: (assignment.type.incompatible)
	@IndexFor("arr") int i = 2;
	
	void subIndexFor(@IndexFor("arr") int v) {
		// Show result is of type LTLength
		@LTLength("arr") int lTLength1 = i - v;
		//:: error: (assignment.type.incompatible)
		@IndexOrLow("arr") int indexOrLow = i - v;		
	}

	void subIndexOrLow(@IndexOrLow("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
	
	// TODO: Is this comment redundant?
	// not a valid Transfer, ex. v = a.length and i = 0; v is not < length;
	void subIndexOrHigh(@IndexOrHigh("arr") int v) {
		// Show result is of type LTLength
		@LTLength("arr") int lTLength1 = i - v;
		//:: error: (assignment.type.incompatible)
		@IndexOrLow("arr") int indexOrLow = i - v;
	}
	
	void sub1() {
		@IndexOrLow("arr") int indexOrLow = i - 1;
		//:: error: (assignment.type.incompatible)
		@IndexFor("arr") int indexFor = i - 1;
	}
	
	void subCompound1() {
		int aux = i;
		aux -= 1;
		@IndexOrLow("arr") int indexOrLow = aux;
		//:: error: (assignment.type.incompatible)
		@IndexFor("arr") int indexFor = aux;
	}
	
	void unaryMinus() {
		int aux = i;
		aux--;
		@IndexOrLow("arr") int indexOrLow = aux;
		//:: error: (assignment.type.incompatible)
		@IndexFor("arr") int indexFor = aux;
	}
	
	void sub0() {
		@IndexFor("arr") int indexFor = i - 0;
	}
	
	void subLTLength(@LTLength("arr") int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
	
	// TODO: Is this comment redundant?
	// ex. v = 2* a.length and i = 0, v is not < length
	void subNonNegative(@NonNegative int v) {
		// Show result is of type LTLength
		@LTLength("arr") int lTLength1 = i - v;
		//:: error: (assignment.type.incompatible)
		@IndexOrLow("arr") int indexOrLow = i - v;
	}

	void subUnknownIndex(@UnknownIndex int v) {
		// Show result is of type UnknownIndex
		@UnknownIndex int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
	
	// TODO: Is this comment redundant?
	// not valid transfer, different arrays are different bounds
	void subIndexForB(@IndexFor("arrB") int v) {
		// Show result is of type LTLength
		@LTLength("arr") int lTLength1 = i - v;
		//:: error: (assignment.type.incompatible)
		@IndexOrLow("arr") int indexOrLow = i - v;
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
		// Show result is of type LTLength
		@LTLength("arr") int lTLength1 = i - v;
		//:: error: (assignment.type.incompatible)
		@IndexOrLow("arr") int indexOrLow = i - v;
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