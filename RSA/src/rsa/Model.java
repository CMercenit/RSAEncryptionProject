package rsa;

import java.math.BigInteger;
import java.util.Random;

/**
 * MVC's Model. Generates public and private key
 * pairs using the standard RSA algorithm and
 * encrypts/decrypts messages.
 * 
 * @author Charles Mercenit
 */

public class Model
{
	private final int BLOCK_SIZE = 5;
	private final BigInteger TWO = new BigInteger("2");
	private final BigInteger E = new BigInteger("17");
	private final BigInteger ONE = BigInteger.ONE;
	private final BigInteger ZERO = BigInteger.ZERO;
	
	private BigInteger myP, myQ, myPhi, publicKey, privateKey;
	private View myView;
	private BigInteger[] myMessage;
	private String myDecryptedMessage = "";
	
	
	public Model(View view)
	{
		myView = view;
	}
	
	/**
	 * Generates a key pair using Java's BigInteger class by
	 * using the following methods: generate two prime numbers
	 * based on the bit length selected by the user; confirm
	 * that the primes will work (if not, call this method
	 * again and start over); multiply the two prime numbers
	 * to get the public key; calculate phi by multiplying each
	 * of the prime numbers minus one by each other; and calculate
	 * the private key using the inverse modulo of the constant
	 * E to phi. 
	 */
	
	public void generateKeys()
	{
		myP = BigInteger.probablePrime(myView.getBitLength(), rand());
		myQ = BigInteger.probablePrime(myView.getBitLength(), rand());
		if(confirmPrimes(myP, myQ))
		{
			publicKey = myP.multiply(myQ);
			myView.displayPublicKey(publicKey.toString());
			myPhi = (myP.subtract(BigInteger.ONE).multiply(myQ.subtract(BigInteger.ONE)));
			privateKey = E.modInverse(myPhi);
			myView.displayPrivateKey(privateKey.toString());
		}
		else
		{
			generateKeys();
		}
	}
	
	/**
	 * Encrypts the passed string using the public
	 * key.
	 * 
	 * @param string to encrypt
	 */
	
	public void encrypt(String s)
	{
		convertToNum(s);
		for(int i = 0; i < myMessage.length; i++)
		{
			myView.setEncryptText((myMessage[i].modPow(E, publicKey)).toString() + " ");
		}
	}
	
	/**
	 * Decrypts the passed string using the public
	 * and private keys.
	 * 
	 * @param string to decrypt
	 */
	
	public void decrypt(String s)
	{
		char chars[] = s.toCharArray();
		String message = "";
		for(int i = 0; i < chars.length; i++)
		{
			if(chars[i] == (char)32)
			{
				BigInteger encrypted = new BigInteger(message);
				convertToString(encrypted.modPow(privateKey, publicKey).toString());
				message = "";
			}
			else
			{
				message += chars[i];
			}
		}
		myView.setDecryptText(myDecryptedMessage);
		myDecryptedMessage = "";
	}
	
	/**
	 * Returns random bits for Java's BigInteger
	 * class to generate prime numbers.
	 * 
	 * @return random bits
	 */
	
	private Random rand()
	{
		return new Random();
	}
	
	/**
	 * Tests the generated prime numbers to confirm
	 * if they are suitable.
	 * 
	 * @param prime number p
	 * @param prime number q
	 * @return boolean of prime number suitability
	 */
	
	private boolean confirmPrimes(BigInteger p, BigInteger q)
	{		
		if(p.compareTo(q) == 0) return false;
		if(p.mod(TWO) == ZERO || q.mod(TWO) == ZERO) return false;
		if((p.subtract(ONE)).gcd(E) == ONE || (q.subtract(ONE)).gcd(E) == ONE) return false;
		
		return true;
	}
	
	/**
	 * Converts the passed in string to a string
	 * of ASCII characters for encryption.
	 * 
	 * @param string to convert
	 */
	
	private void convertToNum(String s)
	{
		char charArray[] = s.toCharArray();
		myMessage = new BigInteger[(int)Math.ceil(s.length()/(double)BLOCK_SIZE)];
		for(int i = 0; i < myMessage.length; i++)
		{
			String chars = "";
			for(int j = 0; j < BLOCK_SIZE; j++)
			{
				if(charArray.length > j+(i*BLOCK_SIZE))
				{
					chars += (int)charArray[j+(i*BLOCK_SIZE)];
				}
				else
				{
					chars += "256";
				}
			}
			myMessage[i] = new BigInteger(chars);
		}
	}
	
	/**
	 * Converts the passed in string of ASCII
	 * characters to a string of regular characters
	 * for decryption.
	 * 
	 * @param string to convert
	 */
	
	private void convertToString(String s)
	{
		char chars[] = s.toCharArray();
		for(int i = 0; i < chars.length; i+=3)
		{
			String temp = "";
			temp += chars[i];
			temp += chars[i+1];
			if(chars[i] < '3')
			{
				temp += chars[i+2];
			}
			else
			{
				i -= 1;
			}
			
			if(!temp.equals("256"))
			{
				myDecryptedMessage += (char)(Integer.parseInt(temp));
			}
		}
	}
}