import org.checkerframework.checker.index.qual.*;

class TypeRuleIndexOrHighB {
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	int accessArray (@IndexOrHigh("arrB") int i) {
		//:: warning: (array.access.unsafe.high)
		return arr[i];
	}
}
