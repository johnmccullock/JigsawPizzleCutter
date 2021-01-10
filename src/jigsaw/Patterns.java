package jigsaw;

import java.awt.Color;
import java.awt.Graphics2D;

public class Patterns
{
	/**
	 * 
	 * Version 1.1 correct row and column calculation errors.  Math.ceil was changed to Math.floor.
	 * 
	 * @param g2d
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param increment
	 * @param background
	 * @param foreground
	 * 
	 * @version 1.1 2016-10-03
	 */
	public static void drawCheckerPattern(Graphics2D g2d, int x, int y, int width, int height, int increment, Color background, Color foreground)
	{
		int rows = (int)Math.floor(width / (float)increment);
		int columns = (int)Math.floor(height / (float)increment);
		boolean flip = false;
		for(int j = 0; j < columns; j++)
		{
			boolean firstFlip = flip;
			for(int i = 0; i < rows; i++)
			{
				if(flip){
					g2d.setPaint(foreground);
				}else{
					g2d.setPaint(background);
				}
				g2d.fillRect((i * increment) + x, (j * increment) + y, increment, increment);
				flip = !flip;
			}
			flip = !firstFlip; // Need to ensure the next row starts with the opposite of what this row started with.
		}
		return;
	}
	
	/**
	 * Similar to drawCheckerPattern() except it's meant for covering areas slightly larger than the increment would evenly cover.
	 * @param g2d
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param increment
	 * @param background
	 * @param foreground
	 */
	public static void paintCheckerPattern(Graphics2D g2d, int x, int y, int width, int height, int increment, Color background, Color foreground)
	{
		int rows = (int)Math.ceil(width / (float)increment);
		int columns = (int)Math.ceil(height / (float)increment);
		boolean flip = false;
		for(int j = 0; j < columns; j++)
		{
			boolean firstFlip = flip;
			for(int i = 0; i < rows; i++)
			{
				if(flip){
					g2d.setPaint(foreground);
				}else{
					g2d.setPaint(background);
				}
				g2d.fillRect((i * increment) + x, (j * increment) + y, increment, increment);
				flip = !flip;
			}
			flip = !firstFlip; // Need to ensure the next row starts with the opposite of what this row started with.
		}
		return;
	}
}
