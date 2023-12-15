package krypto;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyCeasar extends JFrame {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private JTextArea obehandladText;
		private JTextArea behandladText;
		private JButton koda;
		private JButton avkoda;
		private JSlider slider;
		
		private JScrollPane obehandladTextscroll;
		private JScrollPane behandladTextscroll;

	public MyCeasar(){
		super("Ceasar krypto");
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
		
		koda = new JButton("Koda");
		koda.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						koda(obehandladText.getText());
					}
				}
		);
		
		avkoda = new JButton("Avkoda");
		avkoda.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						avKoda(obehandladText.getText());
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
		
		slider = new JSlider(JSlider.VERTICAL,1,25,1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(1);
		
		add(obehandladTextscroll);
		add(behandladTextscroll);
		add(slider);
		add(koda);
		add(avkoda);
		setVisible(true);
	}
	
	public void koda(String text) {
		String kodadText = "";
		int theshift = slider.getValue();
		text = text.toLowerCase();
		
		for(int i=0; i<text.length(); i++) {
			int ascii = (int) text.charAt(i) + theshift;
			if (ascii>122) {
				ascii = 96-122+ascii;
				kodadText = kodadText + (char) ascii;
			}
			else
				kodadText = kodadText + (char) ascii;
		}
		behandladText.setText(kodadText);
	}
	
	public void avKoda(String text) {
		String avkodadText = "";
		int theshift = slider.getValue();
		text = text.toLowerCase();
		
		for(int i=0; i<text.length(); i++) {
			int ascii = (int) text.charAt(i) - theshift;
			if (ascii<97) {
				ascii = 122-96+ascii;
				avkodadText = avkodadText + (char) ascii;
			}
			else
				avkodadText = avkodadText + (char) ascii;
		}
		behandladText.setText(avkodadText);
	}
	
	public static void main(String[] args) {
		new MyCeasar();
	}
}