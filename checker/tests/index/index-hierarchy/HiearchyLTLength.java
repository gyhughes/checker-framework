import org.checkerframework.checker.index.qual.*;

class HierarchyLTLength {
	
	int[] arr = new int[5];
	int[] arrB = new int[5];
	
	@LTLength("arr") int i;
	
	void assignIndexFor(@IndexFor("arr") int v) { 
		i = v;
	}
	
	void assignIndexOrHigh(@IndexOrHigh("arr") int v) { 
		//:: error: (assignment.type.incompatible)
		i = v;
	}
	
	void assignNonNegative(@NonNegative int v) { 
		//:: error: (assignment.type.incompatible)
		i = v;
	}

	void assignIndexOrLow(@IndexOrLow("arr") int v) { 
		i = v;
	}
	
	void assignLTLength(@LTLength("arr") int v) { 
		i = v;
	}

	void assignUnknown(@Unknown int v) { 
		//:: error: (assignment.type.incompatible)
		i = v;
	}

	void assignIndexForB(@IndexFor("arrB") int v) { 
		//:: error: (assignment.type.incompatible)
		i = v;
	}

	void assignIndexOrHighB(@IndexOrHigh("arrB") int v) { 
		//:: error: (assignment.type.incompatible)
		i = v;
	}

	void assignIndexOrLowB(@IndexOrLow("arrB") int v) { 
		//:: error: (assignment.type.incompatible)
		i = v;
	}
	
	void assignLTLengthB(@LTLength("arrB") int v) { 
		//:: error: (assignment.type.incompatible)
		i = v;
	}
}