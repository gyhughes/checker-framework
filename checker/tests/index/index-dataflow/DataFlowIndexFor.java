import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.UnknownIndex;

class DataFlowIndexFor {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	//:: error: (assignment.type.incompatible)
	@IndexFor("arr") int i = 2;
	
	void compareIndexFor(@IndexFor("arr") int indexForComparison, @IndexFor("arr") int indexForReset) {
		@IndexFor("arr") int indexForResult;
		if (i < indexForComparison) {
			// Show i is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexForResult = indexForReset;
		if (i <= indexForComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexForResult = indexForReset;
		if (i > indexForComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexForResult = indexForReset;
		if (i >= indexForComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexForResult = indexForReset;
		if (i != indexForComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexForResult = indexForReset;
		if (i == indexForComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
	}

	void compareIndexOrLow(@IndexOrLow("arr") int indexOrLowComparison, @IndexFor("arr") int indexForReset, @IndexOrLow("arr") int indexOrLowReset) {
		@IndexFor("arr") int indexForResult;
		if (i < indexOrLowComparison) {
			// Show i is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrLowComparison = indexOrLowReset;
		if (i <= indexOrLowComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrLowComparison = indexOrLowReset;
		if (i > indexOrLowComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrLowComparison = indexOrLowReset;
		if (i >= indexOrLowComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrLowComparison = indexOrLowReset;
		if (i != indexOrLowComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrLowComparison = indexOrLowReset;
		if (i == indexOrLowComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
	}
	
	void compareIndexOrHigh(@IndexOrHigh("arr") int indexOrHighComparison, @IndexFor("arr") int indexForReset, @IndexOrHigh("arr") int indexOrHighReset) {
		@IndexFor("arr") int indexForResult;
		if (i < indexOrHighComparison) {
			// Show i is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrHighComparison = indexOrHighReset;
		if (i <= indexOrHighComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrHighComparison = indexOrHighReset;
		if (i > indexOrHighComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrHighComparison = indexOrHighReset;
		if (i >= indexOrHighComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrHighComparison = indexOrHighReset;
		if (i != indexOrHighComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		indexOrHighComparison = indexOrHighReset;
		if (i == indexOrHighComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
	}
	
	void compareNegative1(@IndexFor("arr") int indexForReset) {
		@IndexFor("arr") int indexForResult;
		if (i < arr.length) {
			// impossible
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		if (i <= arr.length) {
			// impossible
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		if (i > arr.length) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// impossible
		}
		i = indexForReset;
		if (i >= arr.length) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// impossible
		}
		i = indexForReset;
		if (i != arr.length) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// impossible
		}
		i = indexForReset;
		if (i == arr.length) {
			// impossible
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
	}
	
	void compareALength(@IndexFor("arr") int indexForReset) {
		@IndexFor("arr") int indexForResult;
		if (i < arr.length) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// impossible
		}
		i = indexForReset;
		if (i <= arr.length) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// impossible
		}
		i = indexForReset;
		if (i > arr.length) {
			// impossible
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		if (i >= arr.length) {
			// impossible
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		if (i != arr.length) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// impossible
		}
		i = indexForReset;
		if (i == arr.length) {
			// impossible
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
	}
	
	void compareLTLength(@LTLength("arr") int LTLengthComparison, @IndexFor("arr") int indexForReset, @LTLength("arr") int LTLengthReset) {
		@IndexFor("arr") int indexForResult;
		if (i < LTLengthComparison) {
			// Show i is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		LTLengthComparison = LTLengthReset;
		if (i <= LTLengthComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		LTLengthComparison = LTLengthReset;
		if (i > LTLengthComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		LTLengthComparison = LTLengthReset;
		if (i >= LTLengthComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		LTLengthComparison = LTLengthReset;
		if (i != LTLengthComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		LTLengthComparison = LTLengthReset;
		if (i == LTLengthComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
	}
	
	void compareNonNegative(@NonNegative int NonNegativeComparison, @IndexFor("arr") int indexForReset, @NonNegative int NonNegativeReset) {
		@IndexFor("arr") int indexForResult;
		if (i < NonNegativeComparison) {
			// Show i is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		NonNegativeComparison = NonNegativeReset;
		if (i <= NonNegativeComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		NonNegativeComparison = NonNegativeReset;
		if (i > NonNegativeComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		NonNegativeComparison = NonNegativeReset;
		if (i >= NonNegativeComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		NonNegativeComparison = NonNegativeReset;
		if (i != NonNegativeComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		NonNegativeComparison = NonNegativeReset;
		if (i == NonNegativeComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
	}

	void compareUnknownIndex(@UnknownIndex int UnknownIndexComparison, @IndexFor("arr") int indexForReset, @UnknownIndex int UnknownIndexReset) {
		@IndexFor("arr") int indexForResult;
		if (i < UnknownIndexComparison) {
			// Show i is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		UnknownIndexComparison = UnknownIndexReset;
		if (i <= UnknownIndexComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		UnknownIndexComparison = UnknownIndexReset;
		if (i > UnknownIndexComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		UnknownIndexComparison = UnknownIndexReset;
		if (i >= UnknownIndexComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		UnknownIndexComparison = UnknownIndexReset;
		if (i != UnknownIndexComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = indexForReset;
		UnknownIndexComparison = UnknownIndexReset;
		if (i == UnknownIndexComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
	}

	void compareIndexForB(@IndexFor("arrB") int v) {

	}
	
	void compareIndexOrLowB(@IndexOrLow("arrB") int v) {

	}

	void compareIndexOrHighB(@IndexOrHigh("arrB") int v) {

	}

	void compareLTLengthB(@LTLength("arrB") int v) {

	}
}