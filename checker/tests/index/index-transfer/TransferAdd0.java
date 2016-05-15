import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Unknown;

class TransferAdd0 {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	void addIndexFor(@IndexFor("arr") int v) {
		// Show result is of type IndexFor
		@IndexFor("arr") int IndexFor = 0 + v;
	}

	void addIndexOrLow(@IndexOrLow("arr") int v) {
		// Show result is of type IndexOrHigh
		@IndexOrLow("arr") int indexOrLow = 0 + v;
        //:: error: (assignment.type.incompatible)
        @IndexFor("arr") int indexFor = 0 + v;
	}
	
	void addIndexOrHigh(@IndexOrHigh("arr") int v, @IndexOrHigh("arr") int indexOrHigh1, @IndexOrHigh("arr") int indexOrHigh2) {
		// Show result is of type IndexOrHigh
		@IndexOrHigh("arr") int indexOrHigh = 0 + v;
		//:: error: (assignment.type.incompatible)
		@IndexFor("arr") int indexFor = 0 + v;
	}
	
	void add1() {
		// Show result is of type NonNegative
		@NonNegative int nn1 = 0 + 1;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh int indexOrHigh = 0 + 1;
	}
	
	void add0() {
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh("") int IOH = 0;
		// Show result is of type IndexOrHigh
		@IndexOrHigh("") int indexOrHigh = IOH + 0;
		//:: error: (assignment.type.incompatible)
		@IndexFor("") int indexFor = 0 + 0;
	}
	
	void addLTLength(@LTLength("arr") int v) {
		// Show result is of type LTLength
		@LTLength("arr") int lTLength1 = 0 + v;
		//:: error: (assignment.type.incompatible)
		@IndexOrLow("arr") int indexOrLow = 0 + v;
	}
	
	void addNonNegative(@NonNegative int v) {
		// Show result is of type NonNegative
		@NonNegative int nn1 = 0 + v;
		//:: error: (assignment.type.incompatible)
		@IndexOrHigh int indexOrHigh = 0 + v;
	}

	void addUnknown(@Unknown int v) {
		// Show result is of type Unknown
		@Unknown int nn1 = v + 0;
		//:: error: (assignment.type.incompatible)
		@NonNegative int nn = 0 + v;
		//:: error: (assignment.type.incompatible)
		@LTLength("arr") int lTLength = 0 + v;
	}
}