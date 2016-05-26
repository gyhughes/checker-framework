import org.checkerframework.checker.index.qual.*;
import java.util.*;

class TransferRuleListRemove {

	void listRemove(ArrayList<Integer> lst) {
		int i = 0;
		int result;
		
		if (i < lst.size()) {
			result = lst.get(i);
			lst.remove(0);
			//:: warning: (list.access.unsafe.high)
			result = lst.get(i);	
		}
	}

}