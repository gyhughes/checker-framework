import org.checkerframework.checker.index.qual.*;

class StringLength {
	void intro(String s) {
		int len = s.length();
		@IndexOrHigh("s") int IOH = len;
		//:: error: (assignment.type.incompatible)
		@IndexFor("s") int InF = len;
	}
}
