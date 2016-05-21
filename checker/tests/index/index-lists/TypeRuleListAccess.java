import org.checkerframework.checker.index.qual.*;
import java.util.*;

class ListAccess {

	void accessIndexFor(List<Integer> lst, @IndexFor("lst") int i) {
		Integer result = lst.get(i);
	}
	
	void accessIndexOrHigh(List<Integer> lst, @IndexOrHigh("lst") int i) {
		//:: warning: (list.access.unsafe)
		Integer result = lst.get(i);
	}
	
	void accessIndexOrLow(List<Integer> lst, @IndexOrLow("lst") int i) {
		//:: warning: (list.access.unsafe)
		Integer result = lst.get(i);
	}
	
	void accessNonNegative(List<Integer> lst, @NonNegative int i) {
		//:: warning: (list.access.unsafe)
		Integer result = lst.get(i);
	}
	
	void accessLTLength(List<Integer> lst, @LTLength("lst") int i) {
		//:: warning: (list.access.unsafe)
		Integer result = lst.get(i);
	}
	
	void accessUnknown(List<Integer> lst, @Unknown int i) {
		//:: warning: (list.access.unsafe)
		Integer result = lst.get(i);
	}
	
	void accessIndexForOther(List<Integer> lst, List<Integer> notlst, @IndexFor("notlst") int i) {
		//:: warning: (list.access.unsafe.name)
		Integer result = lst.get(i);
	}

}