import org.checkerframework.checker.index.qual.*;
import java.util.*;

class StringAccess {

	void accessIndexFor(String str, @IndexFor("str") int i) {
		char result = str.charAt(i);
	}
	
	void accessIndexOrHigh(String str, @IndexOrHigh("str") int i) {
		//:: warning: (string.access.unsafe)
		char result = str.charAt(i);
	}
	
	void accessIndexOrLow(String str, @IndexOrLow("str") int i) {
		//:: warning: (string.access.unsafe)
		char result = str.charAt(i);
	}
	
	void accessNonNegative(String str, @NonNegative int i) {
		//:: warning: (string.access.unsafe)
		char result = str.charAt(i);
	}
	
	void accessLTLength(String str, @LTLength("str") int i) {
		//:: warning: (string.access.unsafe)
		char result = str.charAt(i);
	}
	
	void accessUnknown(String str, @Unknown int i) {
		//:: warning: (string.access.unsafe)
		char result = str.charAt(i);
	}
	
	void accessIndexForOther(String str, String notstr, @IndexFor("notstr") int i) {
		//:: warning: (string.access.unsafe.name)
		char result = str.charAt(i);
	}

}