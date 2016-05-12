import org.checkerframework.checker.index.qual.*;

class DataFlowIndexHighEqualsToIndexLow {

	//:: error: (assignment.type.incompatible)
	@IndexFor("arr") int i = 2;
	
	void compareEqual(@IndexOrLow("arr") int IOL , @IndexOrHigh("arr") int IOH) {
		if (IOL == IOH) {
			// test left results in indexFor
			i = IOL;
		}
		if (IOL == IOH) {
			// test right results in indexFor
			i = IOH;
		}
		if (IOH == IOL) {
			// test left results in indexFor
			i = IOH;
		}
		if (IOH == IOL) {
			// test Right results in indexFor
			i = IOL;
		}
	}
	
}
