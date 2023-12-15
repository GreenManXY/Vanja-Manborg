package krypto;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TwoPadAnalysis extends JFrame{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Font f = new Font("GNU Unifront", Font.PLAIN, 14);
	private static int key;
	
	private JTextArea obehandladText;
	private JTextArea behandladText;
	private JButton koda;
	private JButton avkoda;
	private JPasswordField nyckel;
	
	private TwoPadAnalysis() {
		super("My Stream Cipher");
		setLayout(new GridLayout(1,0,5,5));
		setSize(1400,700);
		
		obehandladText = new JTextArea("skriv");
		obehandladText.setFont(f);
		obehandladText.setLineWrap(true);
		obehandladText.setWrapStyleWord(true);
		obehandladText.setEditable(true);
			
		behandladText = new JTextArea();
		behandladText.setFont(f);
		behandladText.setLineWrap(true);
		behandladText.setWrapStyleWord(true);
		behandladText.setEditable(false);
		
		nyckel = new JPasswordField("nyckel");
		nyckel.setEchoChar('\u0000');
		nyckel.setEditable(true);
		nyckel.setBackground(Color.GRAY);
				
		koda = new JButton("Koda");
		koda.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							koda(new String(nyckel.getPassword()), obehandladText.getText());
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}						
					}
				}
		);
		
		avkoda = new JButton("Avkoda");
		avkoda.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							koda(new String(nyckel.getPassword()), obehandladText.getText());
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
		);
		
		obehandladText.addFocusListener(
	    		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						obehandladText.selectAll();
					}
	    		}        		
	    );
		
		behandladText.addFocusListener(
	    		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						behandladText.selectAll();
					}
	    		}        		
	    );
		
		nyckel.addFocusListener(
	    		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						nyckel.selectAll();
						nyckel.setEchoChar('*');
					}
	    		}
	    );
		
		add(new JScrollPane(obehandladText));
		add(new JScrollPane(behandladText));
		add(nyckel);
		add(koda);
		add(avkoda);
		setVisible(true);
	}
	
	private static boolean checkBit(int x, int k) {
		return (x & 1 << k) != 0;
	}
	
	private static int shift() {
		for (int i=0; i<8; i++) {
			boolean tap1 = checkBit(key, 23);
			boolean tap2 = checkBit(key, 11);
			boolean tap3 = checkBit(key, 7);
			boolean output = checkBit(key, 0);
			boolean input = ((output^tap3)^tap2)^tap1;
							
			if (input) {
				key = key >>> 1;
				key = key | (1 << 31);
			}
			else 
				key = key >>> 1;		
		}
		return key;
	}
	
	private void koda(String nyckel, String text) throws UnsupportedEncodingException {
		key = (nyckel.codePointAt(0) << 24) | (nyckel.codePointAt(1) << 16) | (nyckel.codePointAt(2) << 8) | nyckel.codePointAt(3);
		byte[] byteText = text.getBytes("ISO-8859-1");
		StringBuilder c = new StringBuilder();
		
		shift(); shift(); shift(); shift(); 					//Hoppa över de första 32 bitarna som är samma som nyckelns fyra utvalda char
		for (int i=0; i<byteText.length; i++) { 	
			c.append((char) ((byteText[i]^shift()) & 0xFF));	//Klipp av de första 24 bitar från int variablen som shift() skickar och xora de sista 8
		}
		behandladText.setText(c.toString());		
	}
	
	public static void main(String[] args) {
		new TwoPadAnalysis();
	}
}