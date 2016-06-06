class literalPrevDeclaredArrayIndexOrHigh {

	public void foo() {
		int[] array = new int[6];
		for (int i = 0; i < 6; i++) {
			int k = array[i];
		}
		int index = 4;
		int sum = array[index];
	}

}
