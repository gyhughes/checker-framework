import org.checkerframework.checker.index.qual.*;

class TypeRuleIndexOrLowB {
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	int accessArray (@IndexOrLow("arrB") int i) {
		//:: warning: (array.access.unsafe.low)
		return arr[i];
	}
}
