package rsa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * MVC's View. Displays the application window,
 * all of its components, and uses reflection
 * to associate listeners with each button in
 * the application. 
 * 
 * @author Charles Mercenit
 */

public class View
{
	private final Color TEXT_BACKGROUND = Color.WHITE;
	private final Color BACKGROUND = Color.LIGHT_GRAY;
	private final int TEXT_X = 375;
	private final int TEXT_Y = 700;
	private final int KEY_X = 1110;
	private final int KEY_Y = 40;
	private final int DROPDOWN_OPTIONS = 11;
	private final int CHAR_WIDTH = 7;
	
	private Controller myController;
	private JFrame myFrame = new JFrame("RSA");
	private Container myContentPane;
	private JPanel myTextPanel,
				   myBackground;
	private JTextArea myEnter, 
					  myEncrypt, 
					  myDecrypt;
	private JTextField myPublicKey, 
					   myPrivateKey,
					   mySpoiler,
					   myPublicHeading,
					   myPrivateHeading,
					   myBitLengthHeading;
	private JButton myGenerate,
					myEncryptButton,
					myDecryptButton;
	private ButtonListener myGenerateListener,
						   myEncryptListener,
						   myDecryptListener;
	private JScrollPane myScrollEnter, 
						myScrollEncrypt, 
						myScrollDecrypt,
						myScrollPublic,
						myScrollPrivate;
	private JComboBox<Integer> myBitLength;
	
	
	public View(Controller controller)
	{
		myController = controller;
		
		//Creates the frame
		myFrame.setSize(1200, 900);
		myFrame.setLocation(375, 65);
		myFrame.setLayout(null);
		myFrame.setResizable(false);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create the ContentPane that holds everything
		myContentPane = myFrame.getContentPane();
		myContentPane.setLayout(new BorderLayout());
		
		//Creates the JPanel where everything is placed
		myTextPanel = new JPanel();
		myTextPanel.setBorder(BorderFactory.createEtchedBorder());
		myTextPanel.setLayout(null);
		
		//Creates the leftmost JTextArea for user input
		myEnter = new JTextArea("Enter a message to encrypt.");
		myEnter.setEditable(true);
		myEnter.setLineWrap(true);
		myEnter.setForeground(Color.GRAY);
		myEnter.setBackground(TEXT_BACKGROUND);
		myEnter.addFocusListener(new FocusListener()
		{
			public void focusGained(FocusEvent e)
			{
				if(myEnter.getText().equals("Enter a message to encrypt."))
				{
					myEnter.setForeground(Color.BLACK);
					myEnter.setText("");
				}
			}
			
			public void focusLost(FocusEvent e)
			{
				if(myEnter.getText().equals(""))
				{
					myEnter.setForeground(Color.GRAY);
					myEnter.setText("Enter a message to encrypt.");
				}
			}
		});
		myScrollEnter = new JScrollPane(myEnter);
		myTextPanel.add(myScrollEnter);
		myScrollEnter.setBounds(TEXT_X, TEXT_Y, TEXT_X, TEXT_Y);
		myScrollEnter.setVisible(true);
		myScrollEnter.setLocation(15, 150);
		
		//Creates the middle JTextArea that displays the encrypted message
		myEncrypt = new JTextArea();
		myEncrypt.setLineWrap(true);
		myEncrypt.setEditable(false);
		myEncrypt.setBackground(TEXT_BACKGROUND);
		myScrollEncrypt = new JScrollPane(myEncrypt);
		myTextPanel.add(myScrollEncrypt);
		myScrollEncrypt.setBounds(TEXT_X, TEXT_Y, TEXT_X, TEXT_Y);
		myScrollEncrypt.setVisible(true);
		myScrollEncrypt.setLocation(410, 150);
		
		//Creates the rightmost JTextArea that displays the decrypted message
		myDecrypt = new JTextArea();
		myDecrypt.setLineWrap(true);
		myDecrypt.setEditable(false);
		myDecrypt.setBackground(TEXT_BACKGROUND);
		myScrollDecrypt = new JScrollPane(myDecrypt);
		myTextPanel.add(myScrollDecrypt);
		myScrollDecrypt.setBounds(TEXT_X, TEXT_Y, TEXT_X, TEXT_Y);
		myScrollDecrypt.setVisible(true);
		myScrollDecrypt.setLocation(805, 150);
		
		//Creates the heading for the public key
		myPublicHeading = new JTextField("Public Key: ");
		myPublicHeading.setLayout(null);
		myPublicHeading.setSize(62, 30);
		myPublicHeading.setEditable(false);
		myPublicHeading.setLocation(9, 62);
		myPublicHeading.setBackground(BACKGROUND);
		myPublicHeading.setBorder(BorderFactory.createEmptyBorder());
		myPublicHeading.setFont(new Font("myFont", Font.BOLD, 12));
		myTextPanel.add(myPublicHeading);
		
		//Creates the area where the public key is displayed
		myPublicKey = new JTextField();
		myPublicKey.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		myPublicKey.setEditable(false);
		myScrollPublic = new JScrollPane(myPublicKey);
		myTextPanel.add(myScrollPublic);
		myScrollPublic.setBounds(KEY_X, KEY_Y, KEY_X, KEY_Y);
		myScrollPublic.setVisible(true);
		myScrollPublic.setLocation(75, 60);
		
		//Creates the heading for the private key
		myPrivateHeading = new JTextField("Private Key: ");
		myPrivateHeading.setLayout(null);
		myPrivateHeading.setSize(70, 30);
		myPrivateHeading.setEditable(false);
		myPrivateHeading.setLocation(5, 100);
		myPrivateHeading.setBackground(BACKGROUND);
		myPrivateHeading.setBorder(BorderFactory.createEmptyBorder());
		myPrivateHeading.setFont(new Font("myFont", Font.BOLD, 12));
		myTextPanel.add(myPrivateHeading);
		
		//Creates the area where the private key is displayed
		myPrivateKey = new JTextField();
		myPrivateKey.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		myPrivateKey.setEditable(false);
		myPrivateKey.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent e)
			{
				if(myPrivateKey.getText().length() > 0)
				{
					mySpoiler.setVisible(false);
				}
			}
			
			public void mouseExited(MouseEvent e)
			{
				if(myPrivateKey.getText().length() > 0)
				{
					mySpoiler.setVisible(true);
				}
			}
		});
		myScrollPrivate = new JScrollPane(myPrivateKey);
		myTextPanel.add(myScrollPrivate);
		myScrollPrivate.setBounds(KEY_X, KEY_Y, KEY_X, KEY_Y);
		myScrollPrivate.setVisible(true);
		myScrollPrivate.setLocation(75, 100);
		
		//Creates a privacy banner that covers the private key
		mySpoiler = new JTextField();
		mySpoiler.setLayout(null);
		mySpoiler.setSize(myScrollPrivate.getWidth(), 20);
		mySpoiler.setBackground(Color.BLACK);
		mySpoiler.setEditable(false);
		mySpoiler.setLocation(5, 5);
		mySpoiler.setVisible(false);
		myPrivateKey.add(mySpoiler);
		
		//Creates the Generate Keys button
		myGenerate = new JButton("Generate Keys");
		myGenerate.setLayout(null);
		myGenerate.setSize(240, 45);
		myGenerate.setLocation(250, 5);
		myTextPanel.add(myGenerate);
		
		//Creates the Encrypt button
		myEncryptButton = new JButton("Encrypt");
		myEncryptButton.setLayout(null);
		myEncryptButton.setSize(240, 45);
		myEncryptButton.setLocation(550, 5);
		myTextPanel.add(myEncryptButton);
		
		//Creates the Decrypt button
		myDecryptButton = new JButton("Decrypt");
		myDecryptButton.setLayout(null);
		myDecryptButton.setSize(240, 45);
		myDecryptButton.setLocation(850, 5);
		myTextPanel.add(myDecryptButton);
		
		//Creates the bit length JComboBox
		Integer[] ints = new Integer[DROPDOWN_OPTIONS];
		for(int i = 0; i < DROPDOWN_OPTIONS; i++)
		{
			ints[i] = (int)Math.pow(2, i+5);
		}
		myBitLength = new JComboBox<Integer>(ints);
		myBitLength.setSize(60, 25);
		myBitLength.setBackground(TEXT_BACKGROUND);
		myBitLength.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		myBitLength.setEditable(false);
		myBitLength.setLocation(100, 25);
		myTextPanel.add(myBitLength);
		
		//Creates the heading for the JComboBox
		myBitLengthHeading = new JTextField("Choose a bit length");
		myBitLengthHeading.setLayout(null);
		myBitLengthHeading.setSize(140, 20);
		myBitLengthHeading.setEditable(false);
		myBitLengthHeading.setLocation(60, 2);
		myBitLengthHeading.setBackground(BACKGROUND);
		myBitLengthHeading.setBorder(BorderFactory.createEmptyBorder());
		myBitLengthHeading.setFont(new Font("myFont", Font.BOLD, 15));
		myTextPanel.add(myBitLengthHeading);		
		
		//Associates button listeners
		this.associateListeners(myController);
		
		//Places the JPanel in the ContentPane
		myContentPane.add(myTextPanel, BorderLayout.CENTER);
		
		//Creates the background
		myBackground = new JPanel();
		myBackground.setSize(1200, 900);
		myBackground.setLocation(0, 0);
		myBackground.setBackground(BACKGROUND);
		myTextPanel.add(myBackground);
		
		//Displays the frame
		myFrame.setVisible(true);
		myFrame.setResizable(false);
	}
	
	/**
	 * Associates each button with a ButtonListener,
	 * and each ButtonListener with the appropriate
	 * method from the Controller class.
	 * 
	 * @param controller class that includes the methods
	 */
	
	private void associateListeners(Controller controller)
	{
		Class<? extends Controller> controllerClass;
		Method generateMethod, encryptMethod, decryptMethod;
		
		controllerClass = myController.getClass();
		
		generateMethod = null;
		encryptMethod = null;
		decryptMethod = null;
		
		try
		{
			generateMethod = controllerClass.getMethod("generateKeys", (Class<?>[])null);
			encryptMethod = controllerClass.getMethod("encrypt", (Class<?>[])null);
			decryptMethod = controllerClass.getMethod("decrypt", (Class<?>[])null);
		}
		catch(SecurityException e)
		{
			String error = e.toString();
			System.out.println(error);
		}
		catch(NoSuchMethodException e)
		{
			String error = e.toString();
			System.out.println(error);
		}
		
		myGenerateListener = new ButtonListener(myController, generateMethod, null);
		myGenerate.addMouseListener(myGenerateListener);
		
		myEncryptListener = new ButtonListener(myController, encryptMethod, null);
		myEncryptButton.addMouseListener(myEncryptListener);
		
		myDecryptListener = new ButtonListener(myController, decryptMethod, null);
		myDecryptButton.addMouseListener(myDecryptListener);
	}
	
	/**
	 * Displays the public key
	 * 
	 * @param public key
	 */
	
	public void displayPublicKey(String key)
	{
		myPublicKey.setText(key);
	}
	
	/**
	 * Displays the private key
	 * @param private key
	 */
	
	public void displayPrivateKey(String key)
	{
		myPrivateKey.setText(key);
		updateSpoiler();
	}
	
	/**
	 * Returns the message entered by the user
	 * 
	 * @return message
	 */
	
	public String getMessage()
	{
		return myEnter.getText();
	}
	
	/**
	 * Updates the encryption JTextArea with the
	 * encrypted message
	 * 
	 * @param encrypted message
	 */
	
	public void setEncryptText(String s)
	{
		myEncrypt.append(s);
	}
	
	/**
	 * Clears the encryption JTextArea
	 */
	
	public void resetEncryptText()
	{
		myEncrypt.setText("");
	}
	
	/**
	 * Updates the decryption JTextArea with the
	 * decrypted message
	 * 
	 * @param decrypted message
	 */
	
	public void setDecryptText(String s)
	{
		myDecrypt.append(s);
	}
	
	/**
	 * Clears the decryption JTextArea
	 */
	
	public void resetDecryptText()
	{
		myDecrypt.setText("");
	}
	
	/**
	 * Returns the encrypted message from the
	 * encryption JTextArea
	 * 
	 * @return encrypted message
	 */
	
	public String getEncryptedMessage()
	{
		return myEncrypt.getText();
	}
	
	/**
	 * Returns the selected bit length from
	 * the JComboBox
	 * 
	 * @return bit length
	 */
	
	public int getBitLength()
	{
		return (int)myBitLength.getSelectedItem();
	}
	
	/**
	 * Updates the privacy banner to cover the
	 * private key as it changes
	 */
	
	private void updateSpoiler()
	{
		mySpoiler.setVisible(true);
		mySpoiler.setSize(myPrivateKey.getText().length() * CHAR_WIDTH, 20);
	}
}