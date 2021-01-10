package jigsaw;

import java.awt.Color;
import java.awt.Graphics2D;

public class TabHookAnchor extends TabHookGrip
{
	private Color mColor = Color.BLACK;
	
	public TabHookAnchor(double x, double y, double width)
	{
		super(x, y, width);
		return;
	}
	
	public void render(Graphics2D g2d)
	{
		g2d.setPaint(this.mColor);
		g2d.fillRect((int)Math.round(this.mX1), 
					(int)Math.round(this.mY1), 
					(int)Math.round(this.mX2 - this.mX1), 
					(int)Math.round(this.mY2 - this.mY1));
		return;
	}
	
	public void setColor(Color color)
	{
		this.mColor = color;
		return;
	}
}
