package krypto;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.google.common.collect.*;

public class Frekvensanalys extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Rad 1, kolumn 1
    private JTextArea kodtext;
    
    //Rad 2, kolumn 1
    private JTextArea avkodtext;
    
    //Rad 1, kolumn 2
    private JPanel r1k2;
    private JPanel knappPanel;
    
	private JButton analysknapp;
    private JTextArea kodadbokstav;
    private JTextArea avkodadbokstav;
    private JButton ersättbokstav;
    private JTextArea resultat;
    private JTextArea historik;

    //Rad 2, kolumn 2 är grafen
    private JPanel graf;
    
    private Frekvensanalys () {
    	super("Frekvensanalys");
    	Container c = getContentPane();
        c.setLayout(new GridLayout(2,3,5,5));
        
        kodtext = new JTextArea("Skriv/klistra in kodad text här");
        kodtext.setLineWrap(true);
        kodtext.setWrapStyleWord(true);
        kodtext.setEditable(true);
        
        avkodtext = new JTextArea("Här visas texten som du försöker avkoda");
        avkodtext.setEditable(false);
        avkodtext.setLineWrap(true);
        avkodtext.setWrapStyleWord(true);
        
        analysknapp = new JButton("Frekvensanalysera texten");
        kodadbokstav = new JTextArea("Skriv in bokstaven som du vill ersätta");
        avkodadbokstav = new JTextArea("Skriv in bokstaven med vilken du ska ersätta");
        ersättbokstav = new JButton("Ersätt bokstav");
        
        resultat = new JTextArea("Här visas resultatet");
        resultat.setEditable(false);
        resultat.setBackground(Color.red);
        resultat.setLineWrap(true);
        resultat.setWrapStyleWord(true);

        historik = new JTextArea("");
        historik.setEditable(false);
        historik.setLineWrap(true);
        historik.setWrapStyleWord(true);
        
        kodtext.addFocusListener(
        		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						kodtext.selectAll();
					}
        		}        		
        );
        
        kodadbokstav.addFocusListener(
        		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						kodadbokstav.selectAll();
					}
        		}       		
        );
        
        avkodadbokstav.addFocusListener(
        		new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent f) {
						avkodadbokstav.selectAll();
					}
        		}       		
        );
        
        analysknapp.addActionListener(
        		new ActionListener() {
        				public void actionPerformed(ActionEvent e) {
        					analys(kodtext.getText());
        				}
        		}        
        );
        
        ersättbokstav.addActionListener(
        		new ActionListener() {
        				public void actionPerformed(ActionEvent e) {
        					ersättBokstav(avkodtext.getText(), kodadbokstav.getText(), avkodadbokstav.getText());
        				}
        		}        
        );
        
        knappPanel = new JPanel(new GridLayout(4,1,5,5));
        r1k2 = new JPanel(new GridLayout(1,3));
        graf = new JPanel();
        
        knappPanel.add(analysknapp); knappPanel.add(kodadbokstav); knappPanel.add(avkodadbokstav); knappPanel.add(ersättbokstav);
        r1k2.add(knappPanel); r1k2.add(new JScrollPane(resultat)); r1k2.add(new JScrollPane(historik));
        c.add(new JScrollPane(kodtext)); c.add(r1k2);
        c.add(new JScrollPane(avkodtext)); c.add(graf);
        
        setVisible(true);
        setSize(1000,1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void analys(String text) {
    	text = text.toLowerCase();
    	TreeMap<Character,Integer> map = new TreeMap<>();
        TreeMultimap<Integer, Character> map2 = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());
        
        for (int i=0;i<text.length();i++) {
            if (map.containsKey(text.charAt(i)))
                map.put(text.charAt(i), map.get(text.charAt(i))+1);
            else
                map.put(text.charAt(i),1);
        }        

        for (Map.Entry<Character,Integer> e : map.entrySet()) {
        	map2.put(e.getValue(), e.getKey());
        }
        
        CategoryDataset d = createDataset(map);
        skapaGraf(d);
        resultat.setText(map2.toString());
        avkodtext.setText(kodtext.getText());
    }
    
    private void ersättBokstav(String kodtxt, String text1, String text2) {
    	text2 = text2.toUpperCase();
    	avkodtext.setText(kodtxt.replaceAll(text1, text2));
    	historik.append(text1 + " --> " + text2 + "\n");
    }
    
    private CategoryDataset createDataset(TreeMap<Character,Integer> frekvensKarta) {        
    	final String svAlfabetet = "abcdefghijklmnopqrstuvwxyzåäö";
    	final double[] svFrekvens = {9.3, 1.3, 1.3, 4.5, 9.9, 2.0, 3.3, 2.1, 5.1, 0.7, 3.2, 5.2, 
    								3.5, 8.8, 4.1, 1.7, 0.007,	8.3, 6.3, 8.7, 1.8, 2.4, 0.03, 0.1, 0.6, 0.02, 1.6, 2.1, 1.5};
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (int i=0; i<svAlfabetet.length(); i++) {
	        dataset.addValue((Number) svFrekvens[i], "Svenska", svAlfabetet.charAt(i));
        }
        
        for (Map.Entry<Character,Integer> e : frekvensKarta.entrySet()) {
        	double a = e.getValue();
        	double b = kodtext.getText().length();
	        dataset.addValue(a/b*100, "Kodad text", e.getKey());
        }
        return dataset;
    }
    
    private void skapaGraf (CategoryDataset data) {
    	JFreeChart svChart = ChartFactory.createBarChart(
            "Bokstavsfrekvens",       // chart title
            "Bokstav",                // domain axis label
            "Procent",                // range axis label
            data,       			  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false);                   // URLs?
        CategoryPlot plot = svChart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setItemMargin(0.0);
        CategoryAxis xAxel = plot.getDomainAxis();
        xAxel.setCategoryMargin(0.4);
        
        final ChartPanel chartPanel = new ChartPanel(svChart);
        graf.removeAll(); graf.add(chartPanel); pack();
    }
    
    public static void main(String[] args) {
        new Frekvensanalys();
    }
}