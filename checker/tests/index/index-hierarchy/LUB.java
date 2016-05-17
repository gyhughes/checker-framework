import org.checkerframework.checker.index.qual.*;

public class LUB {
	int[] arr = new int[10];
	//:: error: (assignment.type.incompatible)
	@IndexFor("arr") int InF = 3;
	//:: error: (assignment.type.incompatible)
	@IndexOrLow("arr") int IOL = 4;
	//:: error: (assignment.type.incompatible)
	@IndexOrHigh("arr") int IOH = 2;
	@Unknown int Unk = -4;
	@NonNegative int NN = 8;
	//:: error: (assignment.type.incompatible)
	@LTLength("arr") int LTL = 6; 
	
	void IOHandIOL(boolean bool) {
		int index = bool ? IOH : IOL;
		//:: error: (assignment.type.incompatible)
		IOH = index;
		//:: error: (assignment.type.incompatible)
		IOL = index;
		//:: error: (assignment.type.incompatible)
		LTL = index;
		//:: error: (assignment.type.incompatible)
		NN = index;
		// should be unknown
		Unk = index;
	}
}
