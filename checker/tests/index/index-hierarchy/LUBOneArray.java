import org.checkerframework.checker.index.qual.*;

public class LUBOneArray {
	int[] arr = new int[10];
	//:: error: (assignment.type.incompatible)
	@IndexFor("arr") int InF = 3;
	//:: error: (assignment.type.incompatible)
	@IndexOrLow("arr") int IOL = 4;
	//:: error: (assignment.type.incompatible)
	@IndexOrHigh("arr") int IOH = 2;
	@UnknownIndex int Unk = -4;
	@NonNegative int NN = 8;
	//:: error: (assignment.type.incompatible)
	@LTLength("arr") int LTL = 6;
	boolean bool;
	
	void IOHandIOL() {
		int index = bool ? IOH : IOL;
		//:: error: (assignment.type.incompatible)
		IOH = index;
		//:: error: (assignment.type.incompatible)
		IOL = index;
		//:: error: (assignment.type.incompatible)
		LTL = index;
		//:: error: (assignment.type.incompatible)
		NN = index;
		// should be UnknownIndex
		Unk = index;
	}
	void IOLandIOL() {
		int index = bool ? IOL : IOL;
		//:: error: (assignment.type.incompatible)
		IOH = index;
		IOL = index;
		LTL = index;
		//:: error: (assignment.type.incompatible)
		NN = index;
		// should be UnknownIndex
		Unk = index;
	}
	void IOHandIOH() {
		int index = bool ? IOH : IOH;
		IOH = index;
		//:: error: (assignment.type.incompatible)
		IOL = index;
		//:: error: (assignment.type.incompatible)
		LTL = index;
		NN = index;
		// should be UnknownIndex
		Unk = index;
	}
	void InFandIOL() {
		int index = bool ? InF : IOL;
		//:: error: (assignment.type.incompatible)
		IOH = index;
		IOL = index;
		LTL = index;
		//:: error: (assignment.type.incompatible)
		NN = index;
		// should be UnknownIndex
		Unk = index;
	}

}
