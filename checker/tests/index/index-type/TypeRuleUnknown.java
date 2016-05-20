import org.checkerframework.checker.index.qual.*;

class TypeRuleUnknown {
	int[] arr = new int[5];
	
	int accessArray (@Unknown int i) {
		//:: warning: (array.access.unsafe)
		return arr[i];
	}
}
