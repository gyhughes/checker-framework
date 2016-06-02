import org.checkerframework.checker.index.qual.*;


public class LUBIndexFor {

	//:: error: (assignment.type.incompatible)
	@IndexFor("a") int InF = 3;
	//:: error: (assignment.type.incompatible)
	@IndexOrLow("a") int IOL = 4;
	//:: error: (assignment.type.incompatible)
	@IndexOrHigh("a") int IOH = 2;
	//:: error: (assignment.type.incompatible)
	@LTLength("a") int LTL = 6;
	
	//:: error: (assignment.type.incompatible)
	@IndexFor("b") int InFb = 3;
	//:: error: (assignment.type.incompatible)
	@IndexOrLow("b") int IOLb = 4;
	//:: error: (assignment.type.incompatible)
	@IndexOrHigh("b") int IOHb = 2;
	//:: error: (assignment.type.incompatible)
	@LTLength("b") int LTLb = 6;

	@UnknownIndex int Unk = -4;
	@NonNegative int NN = 8;
	boolean bool;
	
	// should be NonNegative
	void InFandIOHb() {
		int i = bool ? InF : IOHb;
		//:: error: (assignment.type.incompatible)
		IOH = i;
		//:: error: (assignment.type.incompatible)
		IOL = i;
		//:: error: (assignment.type.incompatible)
		LTL = i;
		NN = i;
		Unk = i;
	}
	
	// should be UnknownIndex
	void InFandIOLb() {
		int i = bool ? InF : IOLb;
		//:: error: (assignment.type.incompatible)
		IOH = i;
		//:: error: (assignment.type.incompatible)
		IOL = i;
		//:: error: (assignment.type.incompatible)
		LTL = i;
		//:: error: (assignment.type.incompatible)
		NN = i;
		Unk = i;
	}
	
}