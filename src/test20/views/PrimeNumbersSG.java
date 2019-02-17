package test20.views;

import java.math.BigInteger;
import java.util.ArrayList;

public class PrimeNumbersSG implements Runnable{
	private int num = 0;
	private final int START_NUM = 2;
	private ArrayList <Integer> primeNumbers = new ArrayList <Integer> ();
	private NumCallBack numSgOutput = null;
	
	public PrimeNumbersSG() {
		
	}
	
	public void registerCallBack(NumCallBack obj) {
		numSgOutput = obj;
	}
	
	public void setNumberToCheck(int i) {
		num = i;
	}
	
	// Long check for Sophie Germain number
	public void run() {
		boolean isPrime;
		int lastValue = 2*num+1;
		if (num<2)
			return;
		primeNumbers.clear();
		primeNumbers.add(2);
		// sieve of Eratosthenes
		for (int i=START_NUM; i<=lastValue; i++) {
			isPrime = true;
			for(Integer x: primeNumbers) {
				if(i%x==0) {
					isPrime = false;
					break;
				}
			}
			if (isPrime) 
				primeNumbers.add(i);
			if ((isPrime) && (i==lastValue))
				numSgOutput.callingBackSG(num);
		}
		
	}
	
	// Quick check
	// Miller–Rabin primality test
	public void run_() {
		BigInteger bigInteger = BigInteger.valueOf((num*2+1));
		if (bigInteger.isProbablePrime((int) Math.log(num)))
			numSgOutput.callingBackSG(num);
	}
}
