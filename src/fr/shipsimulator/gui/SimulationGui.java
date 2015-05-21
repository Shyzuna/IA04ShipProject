package fr.shipsimulator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import fr.shipsimulator.agent.EnvironnementAgent;
import fr.shipsimulator.constantes.Constante;


public class SimulationGui extends JFrame implements ActionListener, Constante{

	Object[] actions = {""};
	private JButton quit;
	private EnvironnementAgent myAgent;
	
	
	public SimulationGui(EnvironnementAgent a) {
		
		super("ShipSimulator");
		
		this.myAgent = a;
        
        /*JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");

        menubar.add(file);
        setJMenuBar(menubar);

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        ImageIcon exit = new ImageIcon("exit.png");
        JButton bexit = new JButton(exit);
        bexit.setBorder(new EmptyBorder(0 ,0, 0, 0));
        toolbar.add(bexit);

        add(toolbar, BorderLayout.NORTH);

        JToolBar vertical = new JToolBar(JToolBar.VERTICAL);
        vertical.setFloatable(false);
        vertical.setMargin(new Insets(10, 5, 5, 5));

        add(vertical, BorderLayout.WEST);

        add(new JTextArea(), BorderLayout.CENTER);

        JLabel statusbar = new JLabel(" Statusbar");
        statusbar.setPreferredSize(new Dimension(-1, 22));
        statusbar.setBorder(LineBorder.createGrayLineBorder());
        add(statusbar, BorderLayout.SOUTH);*/

		BufferedImage map = null;
		
		try{
			map = ImageIO.read(new File(Constante.MAP_PATH));
		}catch(IOException ex){
			System.out.println("Cannot load Map !");
		}
		
		JLabel jl = new JLabel(new ImageIcon(map));
		this.add(jl);
		
		final int borderWidth = 1;
		final int rows = 50;
		final int cols = 50;
		BackgroundPanelGrid panel = new BackgroundPanelGrid(new ImageIcon(map),new GridLayout(rows, cols));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		for (int row = 0; row < rows; row++) {
		    for (int col = 0; col < cols; col++) {
		        final JLabel label = new JLabel();
		        if (row == 0) {
		            if (col == 0) {
		                // Top left corner, draw all sides
		                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		            }
		            else {
		                // Top edge, draw all sides except left edge
		                label.setBorder(BorderFactory.createMatteBorder(borderWidth, 
		                                                                0, 
		                                                                borderWidth, 
		                                                                borderWidth, 
		                                                                Color.BLACK));
		            }
		        }
		        else {
		            if (col == 0) {
		                // Left-hand edge, draw all sides except top
		                label.setBorder(BorderFactory.createMatteBorder(0, 
		                                                                borderWidth, 
		                                                                borderWidth, 
		                                                                borderWidth, 
		                                                                Color.BLACK));
		            }
		            else {
		                // Neither top edge nor left edge, skip both top and left lines
		                label.setBorder(BorderFactory.createMatteBorder(0, 
		                                                                0, 
		                                                                borderWidth, 
		                                                                borderWidth, 
		                                                                Color.BLACK));
		            }
		        }
		        panel.add(label);
		    }
		}
		this.add(panel);
		
		this.setSize(Constante.MAP_W,Constante.MAP_H);
		this.setTitle("ShipSimulator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == quit){
			
		}
	}

}
