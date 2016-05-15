import org.checkerframework.checker.index.qual.*;

class TypeRuleNonNegative {
	int[] arr = new int[5];
	
	int accessArray (@NonNegative int i) {
		//:: warning: (Potentially unsafe array access: only use @IndexFor as index. Found: @NonNegative int which could be to high)
		return arr[i];
	}
}
