import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.UnknownIndex;

class TransferSubIndexOrLow {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	// This error: can be suppressed
	//:: error: (assignment.type.incompatible)
	@IndexOrLow("arr") int i = -1;
	
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
	
	// TODO: Is this comment redundnant?
	// not valid ie a.length - -1 !< length
	void subIndexOrHigh(@IndexOrHigh("arr") int v) {
		// Show result is of type LTLength
		@LTLength("arr") int lTLength1 = i - v;
		//:: error: (assignment.type.incompatible)
		@IndexOrLow("arr") int indexOrLow = i - v;		
	}
	
	void sub1() {
		// Show result is of type LTLength
		@LTLength("arr") int lTLength1 = i - 1;
		//:: error: (assignment.type.incompatible)
		@IndexOrLow("arr") int indexOrLow = i - 1;		
	}
	
	void sub0() {
		// Show result is of type IndexOrLow
		@IndexOrLow("arr") int indexOrLow = i - 0;
        //:: error: (assignment.type.incompatible)
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
	// not valid NN could be larger that length
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
	
	// TODO: Redundant comment?
	// Not valid, IndexFor - IndexOrLow is UnknownIndex
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