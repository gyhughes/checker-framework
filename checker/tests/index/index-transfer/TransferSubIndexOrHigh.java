import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Unknown;

class TransferSubIndexOrHigh {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	// This error: can be suppressed
	//:: error: (assignment.type.incompatible)
	@IndexOrHigh("arr") int i = 0;
	
	void subIndexFor(@IndexFor("arr") int v) {
		// Show result is of type LTLength
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		@LTLength("arr") int lTLength = i - v;
	}

	void subIndexOrLow(@IndexOrLow("arr") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
	
	void subIndexOrHigh(@IndexOrHigh("arr") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		@LTLength("arr") int lTLength = i - v;
	}
	
	void sub1() {
		// Show result is of type IndexOrLow
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
	
	void sub0(@IndexOrHigh("arr") int indexOrHigh) {
		// Show result is of type IndexOrHigh
		indexOrHigh = i - 0;
		//:: error: (assignment.type.incompatible)
		@IndexFor("arr") int indexFor = i - 0;
	}
	
	void subLTLength(@LTLength("arr") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
	
	void subNonNegative(@NonNegative int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		@LTLength("arr") int lTLength = i - v;
	}

	void subUnknown(@Unknown int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}

	void subIndexForB(@IndexFor("arrB") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		@LTLength("arr") int lTLength = i - v;
	}
	
	void subIndexOrLowB(@IndexOrLow("arrB") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}

	void subIndexOrHighB(@IndexOrHigh("arrB") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		@LTLength("arr") int lTLength = i - v;
	}

	void subLTLengthB(@LTLength("arrB") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = i - v;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = i - v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = i - v;
	}
}