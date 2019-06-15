package rsa;

/**
 * MVC's Controller. Contains the main method and uses
 * reflection to process button presses during the
 * runtime of the program. 
 * 
 * @author Charles Mercenit
 */

public class Controller
{
	private View myView;
	private Model myModel;
	
	public Controller()
	{
		myView = new View(this);
		myModel = new Model(myView);
	}

	public static void main(String[] args)
	{
		new Controller();
	}
	
	/**
	 * When the Generate Keys button is pressed,
	 * executes this method. It calls the function
	 * of the same name in the Model and generates
	 * a key pair based on the selected bit length.
	 */
	
	public void generateKeys()
	{
		myModel.generateKeys();
	}
	
	/**
	 * When the Encrypt button is pressed,
	 * executes this method. It encrypts the text
	 * in the left most JTextArea.
	 */
	
	public void encrypt()
	{
		myView.resetEncryptText();
		myModel.encrypt(myView.getMessage());
	}
	
	/**
	 * When the Decrypt button is pressed, executes
	 * this method. It decrypts the text in
	 * the middle JTextArea.
	 */
	
	public void decrypt()
	{
		myView.resetDecryptText();
		myModel.decrypt(myView.getEncryptedMessage());
	}
}