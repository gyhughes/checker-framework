import org.checkerframework.checker.index.qual.*;

class TypeRuleIndexOrHigh {
	int[] arr = new int[5];
	
	int accessArray (@IndexOrHigh("arr") int i) {
		//:: warning: (array.access.unsafe.high)
		return arr[i];
	}
}
