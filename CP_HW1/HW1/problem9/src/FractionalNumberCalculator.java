public class FractionalNumberCalculator {
	static int gcd(int a, int b) {
		int tmp;
		if(a<b) {
			tmp = a;
			a = b;
			b = tmp;
		}
		while(b!=0) {
			tmp = a%b;
			a = b;
			b = tmp;
		}
		return a;
	}
	public static void printCalculationResult(String equation) {
		int firstBlank, secondBlank, numerator, denominator;
		firstBlank = equation.indexOf(' ');
		secondBlank = firstBlank+2;
		char symbol = equation.charAt(firstBlank+1);
		String substr1 = equation.substring(0, firstBlank);
		String substr2 = equation.substring(secondBlank+1);
		/*System.out.println(substr1);
		System.out.println(substr2);*/
		FractionalNumber fractionalNum1 = new FractionalNumber(substr1);
		FractionalNumber fractionalNum2 = new FractionalNumber(substr2);
		FractionalNumber fractionalNum3 = new FractionalNumber(1,1);
		switch (symbol) {
			case '+':
				fractionalNum3 = FractionalNumber.add(fractionalNum1, fractionalNum2);
				break;
			case '-':
				fractionalNum3 = FractionalNumber.minus(fractionalNum1, fractionalNum2);
				break;
			case '*':
				fractionalNum3 = FractionalNumber.multiply(fractionalNum1, fractionalNum2);
				break;
			case '/':
				fractionalNum3 = FractionalNumber.division(fractionalNum1, fractionalNum2);
				break;
		}
		/*fractionalNum1.Print();
		fractionalNum2.Print();*/
		fractionalNum3.Print();
		System.out.println();
	}
}

class FractionalNumber {
	private int numerator;
	private int denominator;
	FractionalNumber(String substr1) {
		int index = substr1.indexOf('/');
		int numerator, denominator;
		if(index == -1) {
			numerator = Integer.parseInt(substr1);
			denominator = 1;
		}
		else {
			numerator = Integer.parseInt( substr1.substring(0, index) );
			denominator = Integer.parseInt( substr1.substring( index+1) );
		}
		/*System.out.println(numerator);
		System.out.println(denominator);*/
		int gcd=FractionalNumberCalculator.gcd(numerator,denominator);
//		System.out.println(gcd);
		this.numerator = numerator/gcd;
		this.denominator = denominator/gcd;
		/*System.out.println(this.numerator);
		System.out.println(this.denominator);*/
		if(this.denominator<0) {
			this.numerator *= -1;
			this.denominator *= -1;
		}
	}
	FractionalNumber(int numerator,int denominator) {
		int gcd=FractionalNumberCalculator.gcd(numerator,denominator);
		this.numerator = numerator/gcd;
		this.denominator = denominator/gcd;
		if(this.denominator<0) {
			this.numerator *= -1;
			this.denominator *= -1;
		}
	}
	void setNumerator(int numerator) {
		this.numerator = numerator;

	}
	void setDenominator(int denominator) {
		this.denominator = denominator;
	}
	int getNumerator() {
		return this.numerator;
	}
	int getDenominator() {
		return this.denominator;
	}
	static FractionalNumber add(FractionalNumber fractionalNum1, FractionalNumber fractionalNum2) {
		int numerator = fractionalNum1.getNumerator()*fractionalNum2.getDenominator() + fractionalNum2.getNumerator()*fractionalNum1.getDenominator();
		int denominator = fractionalNum1.getDenominator()*fractionalNum2.getDenominator();
		return new FractionalNumber(numerator,denominator);
	}
	static FractionalNumber minus(FractionalNumber fractionalNum1, FractionalNumber fractionalNum2) {
		int numerator = fractionalNum1.getNumerator()*fractionalNum2.getDenominator() - fractionalNum2.getNumerator()*fractionalNum1.getDenominator();
		int denominator = fractionalNum1.getDenominator()*fractionalNum2.getDenominator();
		//System.out.println(numerator+" "+denominator);
		return new FractionalNumber(numerator,denominator);
	}
	static FractionalNumber multiply(FractionalNumber fractionalNum1, FractionalNumber fractionalNum2) {
		int numerator = fractionalNum1.getNumerator()*fractionalNum2.getNumerator();
		int denominator = fractionalNum1.getDenominator()*fractionalNum2.getDenominator();
		return new FractionalNumber(numerator,denominator);
	}
	static FractionalNumber division(FractionalNumber fractionalNum1, FractionalNumber fractionalNum2) {
		int numerator = fractionalNum1.getNumerator()*fractionalNum2.getDenominator();
		int denominator = fractionalNum1.getDenominator()*fractionalNum2.getNumerator();
		return new FractionalNumber(numerator,denominator);
	}
	void Print() {
		if(this.denominator==1) {
			System.out.println(this.numerator);
		}
		else {
			System.out.println(this.numerator+"/"+this.denominator);
		}
	}
}
