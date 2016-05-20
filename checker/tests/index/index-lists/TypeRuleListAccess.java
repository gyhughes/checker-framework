import org.checkerframework.checker.index.qual.*;
import java.util.*;

class ListAccess {

	void accessIndexFor(List<Integer> lst, @IndexFor("lst") int i) {
		Integer result = lst.get(i);
	}
	
	void accessIndexOrHigh(List<Integer> lst, @IndexOrHigh("lst") int i) {
		//:: warning: (Potentially unsafe list access: only use @IndexFor as index. Found: @IndexOrHigh("lst") int)
		Integer result = lst.get(i);
	}
	
	void accessIndexOrLow(List<Integer> lst, @IndexOrLow("lst") int i) {
		//:: warning: (Potentially unsafe list access: only use @IndexFor as index. Found: @IndexOrLow("lst") int)
		Integer result = lst.get(i);
	}
	
	void accessNonNegative(List<Integer> lst, @NonNegative int i) {
		//:: warning: (Potentially unsafe list access: only use @IndexFor as index. Found: @NonNegative int)
		Integer result = lst.get(i);
	}
	
	void accessLTLength(List<Integer> lst, @LTLength("lst") int i) {
		//:: warning: (Potentially unsafe list access: only use @IndexFor as index. Found: @LTLength("lst") int)
		Integer result = lst.get(i);
	}
	
	void accessUnknown(List<Integer> lst, @Unknown int i) {
		//:: warning: (Potentially unsafe list access: only use @IndexFor as index. Found: @Unknown int)
		Integer result = lst.get(i);
	}
	
	void accessIndexForOther(List<Integer> lst, List<Integer> notlst, @IndexFor("notlst") int i) {
		//:: warning: (Potentially unsafe list access: only use @IndexFor("lst") index. Found: @IndexFor("notlst") int)
		Integer result = lst.get(i);
	}

}