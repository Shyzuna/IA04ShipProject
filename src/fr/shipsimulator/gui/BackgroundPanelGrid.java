package fr.shipsimulator.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class BackgroundPanelGrid extends JPanel {
	 private final BufferedImage img;
	 private int imgW, imgH, paneW, paneH;

    public BackgroundPanelGrid(Icon bg,LayoutManager layout) {
        super(layout);
        Icon icon = bg;
        imgW = icon.getIconWidth();
        imgH = icon.getIconHeight();
        this.setPreferredSize(new Dimension(imgW * 10, imgH * 10));
        img = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        paneW = this.getWidth();
        paneH = this.getHeight();
        g.drawImage(img, 0, 0, paneW, paneH, null);
    }
}
