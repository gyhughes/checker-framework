import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Unknown;

class TransferAddLTLength {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	// This error: can be suppressed
	//:: error: (assignment.type.incompatible)
	@LTLength("arr") int i = -10;
	
	void addIndexFor(@IndexFor("arr") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}

	void addIndexOrLow(@IndexOrLow("arr") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}
	
	void addIndexOrHigh(@IndexOrHigh("arr") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}
	
	void add1() {
		// Show result is of type Unknown
		@Unknown int nn1 = 1 + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 1 + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 1 + i;
	}
	
	void add0() {
		// Show result is of type LTLength
		@LTLength("arr") int lTLength1 = 0 + i;
		//:: error: (assignment.type.incompatible)
		@IndexOrLow("arr") int indexOrLow = 0 + i;
	}
	
	void addLTLength(@LTLength("arr") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}
	
	void addNonNegative(@NonNegative int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}

	void addUnknown(@Unknown int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}

	void addIndexForB(@IndexFor("arrB") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}
	
	void addIndexOrLowB(@IndexOrLow("arrB") int v) {
		@Unknown int unknown1;
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}

	void addIndexOrHighB(@IndexOrHigh("arrB") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}

	void addLTLengthB(@LTLength("arrB") int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + i;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = v + i;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = v + i;
	}
}