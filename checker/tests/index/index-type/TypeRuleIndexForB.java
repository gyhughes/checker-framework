import org.checkerframework.checker.index.qual.*;

class TypeRuleIndexForB {
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	int accessArray (@IndexFor("arrB") int i) {
		//:: warning: (array.access.unsafe.name)
		return arr[i];
	}
}
