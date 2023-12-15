package krypto;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MyVigenere extends JFrame {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private JTextArea obehandladText;
		private JTextArea behandladText;
		private JButton koda;
		private JButton avkoda;
		private JPasswordField nyckel;
		final private ArrayList<Character> alfabetet;
		final private String alfab = "abcdefghijklmnopqrstuvwxyzåäö";
		
		private JScrollPane obehandladTextscroll;
		private JScrollPane behandladTextscroll;

	private MyVigenere() {
		super("Vigenere krypto");
		setLayout(new GridLayout(1,0,5,5));
		setSize(1400,700);
		
		obehandladText = new JTextArea("Skriv texten du vill koda eller avkoda här");
		obehandladText.setLineWrap(true);
		obehandladText.setWrapStyleWord(true);
		obehandladText.setEditable(true);
		obehandladTextscroll = new JScrollPane(obehandladText);
			
		behandladText = new JTextArea();
		behandladText.setLineWrap(true);
		behandladText.setWrapStyleWord(true);
		behandladText.setEditable(false);
		behandladTextscroll = new JScrollPane(behandladText);
		
		nyckel = new JPasswordField("Skriv din hemliga nyckel här");
		nyckel.setEchoChar('\u0000');
		nyckel.setEditable(true);
		nyckel.setBackground(Color.GRAY);
		
		alfabetet = new ArrayList<>();
		for (int i=0; i<alfab.length(); i++) {
			alfabetet.add(alfab.charAt(i));
		}
				
		koda = new JButton("Koda");
		koda.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						koda(new String(nyckel.getPassword()), obehandladText.getText());						
					}
				}
		);
		
		avkoda = new JButton("Avkoda");
		avkoda.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						avKoda(new String(nyckel.getPassword()), obehandladText.getText());
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
		
		add(obehandladTextscroll);
		add(behandladTextscroll);
		add(nyckel);
		add(koda);
		add(avkoda);
		setVisible(true);
	}
	
	private void koda(String key, String text) {
		StringBuilder kodadText = new StringBuilder();
		text = text.toLowerCase();
		text = text.replaceAll("\\s", "");
		key = key.toLowerCase();
		
		for(int i=0; i<text.length(); i++) {
			int bokstavSomKodas = 0;
			int nyckelBokstav = 0;
			bokstavSomKodas = alfabetet.indexOf(text.charAt(i));
			nyckelBokstav = alfabetet.indexOf(key.charAt(i%key.length()));
			kodadText.append(alfabetet.get((bokstavSomKodas+nyckelBokstav)%alfab.length()));
		}
		behandladText.setText(kodadText.toString());
	}
	
	private void avKoda(String key, String text) {
		StringBuilder avkodadText = new StringBuilder();
		text = text.toLowerCase();
		key = key.toLowerCase();
		
		for(int i=0; i<text.length(); i++) {
			int bokstavSomAvkodas = 0;
			int nyckelBokstav = 0;
			
			bokstavSomAvkodas = alfabetet.indexOf(text.charAt(i));
			nyckelBokstav = alfabetet.indexOf(key.charAt(i%key.length()));
			avkodadText.append(alfabetet.get((bokstavSomAvkodas-nyckelBokstav+alfab.length())%alfab.length()));
		}
		behandladText.setText(avkodadText.toString().toUpperCase());
	}
	
	public static void main(String[] args) {
		new MyVigenere();
	}
}