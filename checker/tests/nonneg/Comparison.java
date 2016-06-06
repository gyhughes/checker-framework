import org.checkerframework.checker.nonneg.*;
import org.checkerframework.checker.nonneg.qual.*;

/*
 * These test cases make sure that an @Unknown index can be proven to be 
 * a @NonNegative and successfully access an array.
 */
public class Comparison {

    void testGreaterThanOrEqualTo() {
    	int[] arr = new int[6];
    	@Unknown int a = 1;
    	
    	if (a >= 0) {
    		int b = arr[a];
    		
    		@NonNegative int c = a;
    	}
    }

    void testGreaterThan() {
    	int[] arr = new int[6];
    	@Unknown int a = 1;
    	
    	if (a > -1) {
    		int b = arr[a];
    		
    		@NonNegative int c = a;
    	}
    }
    
}
