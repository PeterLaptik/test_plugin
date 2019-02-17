package test20.views;

import java.util.ArrayList;

public class PrimeNumbers implements Runnable{
	private boolean isStopped = true;
	private final int START_NUM = 2;
	private final int END_NUM = 800;
	private ArrayList <Integer> primeNumbers = new ArrayList <Integer> ();
	NumCallBack numOutput = null; // writer for numbers
	
	public PrimeNumbers() {
		
	}
	
	// Callback to output numbers
	public void registerCallback(NumCallBack writer) {
		numOutput = writer;
	}
	
	// Prime number computing
	public void run() {
		boolean isPrime;
		isStopped = false;
		primeNumbers.clear();
		// sieve of Eratosthenes
		for (int i=START_NUM; i<=END_NUM; i++) {
			isPrime = true;
			for(Integer x: primeNumbers) {
				if(i%x==0) {
					isPrime = false;
					break;
				}
				if (isStopped)
					return;
			}
			if (isPrime) {
				primeNumbers.add(i);
				if (numOutput!=null)
					numOutput.callingBack(i);
			}
		}
		isStopped = true;
	}
	
	public void stopAll() {
		isStopped = true;
	}
	
	public boolean getIsStopped() {
		return isStopped;
	}
}
