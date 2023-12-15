package krypto;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.google.common.collect.TreeMultimap;



public class BreakingVigenere extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Rad 1, kolumn 1
	private JPanel r1k1;
	private JPanel r1k1_knappar;
	
	private JTextArea kodtext;    
    private JButton analysknapp;
    private JTextField nyckellängd; 
    private JTextField nyckelposition;
    private JButton analysknapp2;
        
    //Rad 1, kolumn 2
    private JTextArea resultat;
    
    //Rad 2, kolumn 1
    private JTextArea avkodtext;

    //Rad 2, kolumn 2
    final Character[] alfabetet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P',	
    							  'Q','R','S','T','U','V','W','X','Y','Z','Å','Ä','Ö'};
    final JTable vigeneretabell;
    
    private BreakingVigenere() {
    	super("Breaking Vigenere");
    	Container c = getContentPane();
        c.setLayout(new GridLayout(2,2,5,5));
        
        kodtext = new JTextArea("Skriv/klistra in kodad text här");
        kodtext.setLineWrap(true);
        kodtext.setWrapStyleWord(true);
        kodtext.setEditable(true);

        analysknapp = new JButton("Analysera texten");
        
        nyckellängd  = new JTextField("Skriv in den troliga nyckellängden");
        nyckellängd.setEditable(false);
        
        nyckelposition = new JTextField("Skriv nyckelpositionen du vill analysera här");
        nyckelposition.setEditable(true);
        
        analysknapp2 = new JButton("Analysera nyckelpositionen"); 

        resultat = new JTextArea("Här visas resultatet");
        resultat.setEditable(false);
        resultat.setBackground(Color.red);
        resultat.setLineWrap(true);
        resultat.setWrapStyleWord(true);
        
        avkodtext = new JTextArea("Här visas texten från den valda nyckelpositionen");
        avkodtext.setEditable(false);
        avkodtext.setLineWrap(true);
        avkodtext.setWrapStyleWord(true);
        
        vigeneretabell = new JTable(vigenereTabell(alfabetet), alfabetet) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int data, int columns) {
        		return false;
        	}
        	
//        	public Component prepareRenderer(TableCellRenderer renderer, int rad, int kolumn) {
//        		Component c = super.prepareRenderer(renderer, rad, kolumn);
//        		if (isCellSelected(rad, kolumn))
//        			c.setBackground(Color.BLUE);
//				return c;
//        	}
        };
        vigeneretabell.setCellSelectionEnabled(true);
        vigeneretabell.getTableHeader().setReorderingAllowed(false);
        vigeneretabell.setSelectionBackground(Color.RED);     
        
//        vigeneretabell.addMouseListener(
//        	new MouseAdapter() {
//        		@Override
//        		public void mouseClicked(MouseEvent m) {
//        			
//        		}
//        	}
//        );
        
        kodtext.addFocusListener(
        		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						kodtext.selectAll();
					}
        		}        		
        );
        
        nyckelposition.addFocusListener(
        		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						nyckelposition.selectAll();
					}
        		}       		
        );
        
        analysknapp.addActionListener(
        		new ActionListener() {
        				public void actionPerformed(ActionEvent e) {
        					analys1(kodtext.getText());
        					nyckellängd.setEditable(true);
        				}
        		}        
        );
        
        nyckellängd.addFocusListener(
        		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						nyckellängd.selectAll();
					}
        		}       		
        );
        
        analysknapp2.addActionListener(
        		new ActionListener() {
        				public void actionPerformed(ActionEvent e) {
        					analys2(kodtext.getText(), Integer.parseInt(nyckellängd.getText()));
        				}
        		}        
        );
        
        avkodtext.addFocusListener(
        		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						avkodtext.selectAll();
					}
        		}        		
        );
        
        r1k1_knappar = new JPanel(new GridLayout(4,1));
        r1k1 = new JPanel(new GridLayout(1,2));
        r1k1_knappar.add(analysknapp); r1k1_knappar.add(nyckellängd); r1k1_knappar.add(nyckelposition); r1k1_knappar.add(analysknapp2);
        r1k1.add(new JScrollPane(kodtext)); r1k1.add(r1k1_knappar);
        c.add(r1k1); c.add(new JScrollPane(resultat));
        c.add(new JScrollPane(avkodtext)); c.add(new JScrollPane(vigeneretabell));
        
        setVisible(true);
        setSize(1000,1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void analys1(String text) {
    	text = text.toLowerCase();
    	TreeMultimap<String, Integer> map = TreeMultimap.create();
    	TreeMultimap<String, Integer> map2 = TreeMultimap.create();
    	TreeMap<Integer,Integer> map3 = new TreeMap<Integer,Integer>();
    	
    	int z = 0;
    	String lastkey = "";
    	   	
    	for (int i=0; i<text.length()-1; i++) {
    		if (text.charAt(i) == text.charAt(i+1))
    			map.put(text.substring(i, i+2), i);	
    	}
    	
    	for (Map.Entry<String, Integer> e : map.entries()) {
    		if (e.getKey()!=lastkey) {
    			lastkey = e.getKey();
    			z = 0;
    		}
    		map2.putAll(e.getKey(), faktorera(e.getValue()-z));
    		z = e.getValue();    		
    	}
    	
    	for (Map.Entry<String, Integer> e : map2.entries()) {
    		if (map3.containsKey(e.getValue()))
    			map3.put(e.getValue(), map3.get(e.getValue())+1);
    		else 
    			map3.put(e.getValue(), 1);
    	}
    	
    	resultat.setText(map.toString() + "\n\n" + map2.toString() + "\n\n" + map3.headMap(20));
    }
    
    private void analys2 (String text, int nyckel) {
    	StringBuilder b = new StringBuilder();
    	for (int i=0; i<text.length(); i+=nyckel) {
    		b.append(text.charAt(i));
    	}
    	avkodtext.setText(b.toString());
    }
    
    private HashSet<Integer> faktorera(Integer tal) {
    	HashSet<Integer> faktorer = new HashSet<Integer>();
    	int gräns = (int) Math.sqrt(tal);
    	
    	for (int i=2; i<gräns; i++) {
    		if(tal%i == 0) {
    			faktorer.add(i);
//    			tal = tal/i;
    		}
    	}
    	
    	if (faktorer.isEmpty())
    		faktorer.add(tal);
    	
    	return faktorer;
    }
    
    private Character[][] vigenereTabell (Character[] a) {
    	Character [][] tabell = new Character[a.length][a.length];
    	
    	for (int r=0; r<a.length; r++) {
    		for (int k=0; k<a.length; k++) {
    			tabell[r][k] = a[(r+k)%a.length];
    		}
    	}
    	return tabell;
    }
    
    public static void main(String[] args) {
        new BreakingVigenere();
    }
}
