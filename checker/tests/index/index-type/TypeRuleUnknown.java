import org.checkerframework.checker.index.qual.*;

class TypeRuleUnknownIndex {
	int[] arr = new int[5];
	
	int accessArray (@UnknownIndex int i) {
		//:: warning: (array.access.unsafe)
		return arr[i];
	}
}
