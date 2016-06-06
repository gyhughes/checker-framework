import org.checkerframework.checker.nonneg.*;
import org.checkerframework.checker.nonneg.qual.*;

/*
 * These test cases check the use of binary operations. 
 */
public class Operations {

    void testAddition() {
    	@NonNegative int a = 0;
    	@NonNegative int b = 1;
    	
    	@Unknown int c = -10;
    	@Unknown int d = 3;
    	
    	@NonNegative int e = a + b;
    	@NonNegative int f = a + 1;
    	
    	@Unknown int g = c + b;
    	@Unknown int h = c + d;
    	@Unknown int i = c + 1;
    	@Unknown int j = c + -1;
    	
        //:: error: (assignment.type.incompatible)
    	@NonNegative int k = a + c;
    	//:: error: (assignment.type.incompatible)
    	@NonNegative int l = a + -10;	
    }
    
    void testSubtraction() {
    	@NonNegative int a = 0;
    	@NonNegative int b = 1;
    	
    	@Unknown int c = -10;
    	@Unknown int d = 3;
    	
    	@Unknown int e = c - d;
    	@Unknown int f = c - a;
    	@Unknown int g = a - c;
    	
        //:: error: (assignment.type.incompatible)
    	@NonNegative int h = a - b;
    	//:: error: (assignment.type.incompatible)
    	@NonNegative int i = a - c;
    	//:: error: (assignment.type.incompatible)
    	@NonNegative int j = c - a;
    	//:: error: (assignment.type.incompatible)
    	@NonNegative int k = a + -10;
    }
}
