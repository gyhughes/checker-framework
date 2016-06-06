import org.checkerframework.checker.nonneg.*;
import org.checkerframework.checker.nonneg.qual.*;

/*
 * These test cases test to make sure that the qualifier hierarchy is
 * correctly enforced using inline annotations.
 */
public class QualifierHierarchy {

    void testUserWrittenDeclaration() {
    	@NonNegative int a = 0;
    	@NonNegative int b = 1;
    	
    	@Unknown int c = -1;
    	@Unknown int d = 3;
    	
        //:: error: (assignment.type.incompatible)
    	@NonNegative int e = -1;
    	testNN(e);
    }
    
    void testUserWrittenAssignment() {
    	@NonNegative int a = 1;
    	@NonNegative int b = a;
    	
    	@Unknown int c = -1;
    	@Unknown int d = c;
    	@Unknown int e = a;
    	
    	//:: error: (assignment.type.incompatible)
    	@NonNegative int f = c;
    }
    
    void testUserWritten () {
    	
    }

    void testImplicitAnnotations() {
    	@NonNegative int a = 1;

        @NonNegative int n1 = 1;
        testUK(n1);
        @Unknown int n2 = -1;
        //:: error: (argument.type.incompatible)
        testNN(n2);
        
        @Unknown int n3 = 2;
        @NonNegative int n4 = n3;

    }
    
    void testUK(@Unknown int a) {
    	a = -1;
    }
    
    void testNN(@NonNegative int a) {
    	//:: error: (assignment.type.incompatible)
    	a = -1;
    }
    
}
