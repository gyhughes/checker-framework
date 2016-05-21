import org.checkerframework.checker.index.qual.*;

class TypeRuleIndexOrLow {
	int[] arr = new int[5];
	
	int accessArray (@IndexOrLow("arr") int i) {
		//:: warning: (array.access.unsafe.low)
		return arr[i];
	}
}
