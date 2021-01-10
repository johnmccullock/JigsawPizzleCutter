package jigsaw;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public class FontTool
{
	public static Font getFont(String face, int size, boolean isBold, boolean isItalic)
	{
		Font aFont = null;
		if(isBold && isItalic){
			aFont = new Font(face, Font.BOLD + Font.ITALIC, size);
		}else if(isBold && !isItalic){
			aFont = new Font(face, Font.BOLD, size);
		}else if(!isBold && isItalic){
			aFont = new Font(face, Font.ITALIC, size);
		}else{
			aFont = new Font(face, Font.PLAIN, size);
		}
		return aFont;
	}
	
	public static int maxLength(String[] items, Font font)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage temp = gc.createCompatibleImage(100, 100);
		Graphics2D g2d = (Graphics2D)temp.getGraphics();
		int longest = 0;
		for(int i = 0; i < items.length; i++)
		{
			longest = Math.max(g2d.getFontMetrics(font).stringWidth(items[i]), longest);
		}
		return longest;
	}
	
	public static int maxLength(String[] items, Graphics gr)
	{
		int longest = 0;
		for(int i = 0; i < items.length; i++)
		{
			longest = Math.max(gr.getFontMetrics(gr.getFont()).stringWidth(items[i]), longest);
		}
		return longest;
	}
	
	public static int maxHeight(String test, Font font)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage temp = gc.createCompatibleImage(100, 100);
		Graphics2D g2d = (Graphics2D)temp.getGraphics();
		return g2d.getFontMetrics(font).getHeight();
	}
}
