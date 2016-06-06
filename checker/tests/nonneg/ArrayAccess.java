import org.checkerframework.checker.nonneg.*;
import org.checkerframework.checker.nonneg.qual.*;

/*
 * These test cases make sure that an @Unknown index cannot access an array.
 */
public class ArrayAccess {

    void testArrayAccess() {
    	int[] arr = new int[10];
    	
    	@NonNegative int a = 1;
    	@Unknown int b = -2;
    	
    	int c = arr[a];
    	
        //:: warning: (Potentially unsafe array access: only use NonNegative index)
    	int d = arr[b];
    }
}
