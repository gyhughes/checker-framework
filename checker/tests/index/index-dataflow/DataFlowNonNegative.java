import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexOrLow;
import org.checkerframework.checker.index.qual.LTLength;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Unknown;

class DataFlowNonNegative {

	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	@NonNegative int i = 3;

	void compareIndexFor(@IndexFor("arr") int indexForComparison, @IndexFor("arr") int indexForReset, @IndexOrHigh("arr") int indexOrHighReset, @NonNegative int nonNegativeReset) {
		
		@NonNegative int nonNegativeResult;
		@IndexOrHigh("arr") int indexOrHighResult;
		@IndexFor("arr") int indexForResult;

		if (i < indexForComparison) {
			// Show i is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i <= indexForComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i > indexForComparison) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i >= indexForComparison) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i != indexForComparison) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type indexForResult
			indexForResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i == indexForComparison) {
			// Show result is of type indexForResult
			indexForResult = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}

	}

	void compareIndexOrLow(@IndexOrLow("arr") int indexOrLowComparison, @IndexFor("arr") int indexForReset, @IndexOrHigh("arr") int indexOrHighReset, @NonNegative int nonNegativeReset) {
		
		@NonNegative int nonNegativeResult;
		@IndexOrHigh("arr") int indexOrHighResult;
		@IndexFor("arr") int indexForResult;
		
		if (i < indexOrLowComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i <= indexOrLowComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i > indexOrLowComparison) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i >= indexOrLowComparison) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i != indexOrLowComparison) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i == indexOrLowComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
	}
	
	void compareIndexOrHigh(@IndexOrHigh("arr") int indexOrHighComparison, @IndexFor("arr") int indexForReset, @IndexOrHigh("arr") int indexOrHighReset, @NonNegative int nonNegativeReset) {
		
		@NonNegative int nonNegativeResult;
		@IndexOrHigh("arr") int indexOrHighResult;
		@IndexFor("arr") int indexForResult;
		
		if (i < indexOrHighComparison) {
			// Show result is of type IndexFor
			indexForResult = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i <= indexOrHighComparison) {
			// Show result is of type IndexOrHigh
			@IndexOrHigh("arr") int indexOrHigh = i;
			//:: error: (assignment.type.incompatible)
			@IndexFor("arr") int indexFor = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i > indexOrHighComparison) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type IndexOrHigh
			@IndexOrHigh("arr") int indexOrHigh = i;
			//:: error: (assignment.type.incompatible)
			@IndexFor("arr") int indexFor = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i >= indexOrHighComparison) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i != indexOrHighComparison) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type IndexOrHigh
			@IndexOrHigh("arr") int indexOrHigh = i;
			//:: error: (assignment.type.incompatible)
			@IndexFor("arr") int indexFor = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i == indexOrHighComparison) {
			// Show result is of type IndexOrHigh
			@IndexOrHigh("arr") int indexOrHigh = i;
			//:: error: (assignment.type.incompatible)
			@IndexFor("arr") int indexFor = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
	}

	void compareALength(@IndexFor("arr") int indexForReset, @IndexFor("arr") int indexForReset, @IndexOrHigh("arr") int indexOrHighReset, @NonNegative int nonNegativeReset) {

		@NonNegative int nonNegativeResult;
		@IndexOrHigh("arr") int indexOrHighResult;
		@IndexFor("arr") int indexForResult;
		
		if (i < arr.length) {
			// Show result is of type IndexFor
			indexForResult = i;	
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i <= arr.length) {
			// Show result is of type IndexOrHigh
			@IndexOrHigh("arr") int indexOrHigh = i;
			//:: error: (assignment.type.incompatible)
			@IndexFor("arr") int indexFor = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i > arr.length) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type IndexOrHigh
			@IndexOrHigh("arr") int indexOrHigh = i;
			//:: error: (assignment.type.incompatible)
			@IndexFor("arr") int indexFor = i;
		}
 		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i >= arr.length) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type IndexFor
			indexForResult = i;	
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i != arr.length) {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		} else {
			// Show result is of type IndexOrHigh
			@IndexOrHigh("arr") int indexOrHigh = i;
			//:: error: (assignment.type.incompatible)
			@IndexFor("arr") int indexFor = i;
		}
		i = nonNegativeReset;
		nonNegativeResult = nonNegativeReset;
		indexOrHighResult = indexOrHighReset;
		indexForResult = indexForReset;
		if (i == arr.length) {
			// Show result is of type IndexOrHigh
			@IndexOrHigh("arr") int indexOrHigh = i;
			//:: error: (assignment.type.incompatible)
			@IndexFor("arr") int indexFor = i;
		} else {
			// Show result is of type NonNegative
			nonNegativeResult = i;
			//:: error: (assignment.type.incompatible)
			indexOrHighResult = i;
		}
	}

}