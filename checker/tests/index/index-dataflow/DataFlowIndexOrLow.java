import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Unknown;

class DataFlowIndexFor {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	//:: error: (assignment.type.incompatible)
	@IndexOrLow("arr") int i = 2;
	
	void compareIndexFor(@IndexFor("arr") int indexForComparison, @IndexFor("arr") int indexForReset, @IndexOrLow("arr") int indexOrLowReset) {
		@IndexOrLow("arr") int indexOrLowResult;
		@IndexFor("arr") int indexForResult;
		if (i < indexForComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
	        //:: error: (assignment.type.incompatible)
	        indexForResult = i;
		} else {
			// Show result is of type IndexFor
						indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i <= indexForComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i > indexForComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i >= indexForComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i != indexForComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i == indexForComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
	}
	
	void compareIndexOrLow(@IndexOrLow("arr") int indexOrLowComparison, @IndexOrLow("arr") int indexOrLowReset) {
		@IndexFor("arr") int indexForResult;
		@IndexOrLow("arr") int indexOrLowResult;
		if (i < indexOrLowComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowComparison = indexOrLowReset;
		if (i <= indexOrLowComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowComparison = indexOrLowReset;
		if (i > indexOrLowComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowComparison = indexOrLowReset;
		if (i >= indexOrLowComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowComparison = indexOrLowReset;
		if (i != indexOrLowComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowComparison = indexOrLowReset;
		if (i == indexOrLowComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
	}
	
	void compareIndexOrHigh(@IndexOrHigh("arr") int indexOrHighComparison, @IndexFor("arr") int indexForReset, @IndexOrLow("arr") int indexOrLowReset) {
		@IndexOrLow("arr") int indexOrLowResult;
		@IndexFor("arr") int indexForResult;
		if (i < indexOrHighComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
	        //:: error: (assignment.type.incompatible)
	        indexForResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i <= indexOrHighComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i > indexOrHighComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i >= indexOrHighComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i != indexOrHighComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i == indexOrHighComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
	}
	
	void compareNegative1(@IndexFor("arr") int indexForReset, @IndexOrLow("arr") int indexOrLowReset) {
		@IndexFor("arr") int indexForResult;
		@IndexOrLow("arr") int indexOrLowResult;
		if (i < -1) {
			// impossible
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i <= -1) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i > -1) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;			
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i >= -1) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		} else {
			// impossible
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i != -1) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i == -1) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
	}

	void compareALength(@IndexFor("arr") int indexForReset, @IndexOrLow("arr") int indexOrLowReset) {
		@IndexFor("arr") int indexForResult;
		@IndexOrLow("arr") int indexOrLowResult;
		if (i < arr.length) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		} else {
			// impossible
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i <= arr.length) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		} else {
			// impossible
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i > arr.length) {
			// impossible
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i >= arr.length) {
			// index
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i != arr.length) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i == arr.length) {
			// impossible
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;	
		}
	}
	
	void compareLTLength(@LTLength("arr") int LTLengthComparison, @IndexOrLow("arr") int indexOrLowReset, @IndexFor("arr") int indexForReset) {
		@IndexFor("arr") int indexForResult;
		@IndexOrLow("arr") int indexOrLowResult;
		if (i < LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i <= LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i > LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i >= LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i != LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i == LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
	}
	
	void compareNonNegative(@NonNegative int nonNegativeComparison, @IndexFor("arr") int indexForReset, @IndexOrLow("arr") int indexOrLowReset) {
		@IndexOrLow("arr") int indexOrLowResult;
		@IndexFor("arr") int indexForResult;
		if (i < nonNegativeComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
	        //:: error: (assignment.type.incompatible)
	        indexForResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i <= nonNegativeComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i > nonNegativeComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i >= nonNegativeComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i != nonNegativeComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i == nonNegativeComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
	}
	
	void compareUnknown(@Unknown int unknownComparison, @IndexOrLow("arr") int indexOrLowReset, @IndexFor("arr") int indexForReset) {
		@IndexFor("arr") int indexForResult;
		@IndexOrLow("arr") int indexOrLowResult;
		if (i < LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i <= LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i > LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i >= LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i != LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
		i = indexOrLowReset;
		indexOrLowResult = indexOrLowReset;
		indexForResult = indexForReset;
		if (i == LTLengthComparison) {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		} else {
			// Show result is of type IndexOrLow
			indexOrLowResult = i;
			//:: error: (assignment.type.incompatible)
			indexForResult = i;
		}
	}
}
