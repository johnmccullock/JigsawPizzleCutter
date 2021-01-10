package jigsaw;

import java.awt.geom.Rectangle2D;

public class TabHookGrip
{
	protected double mX = 0.0;
	protected double mY = 0.0;
	protected double mX1 = 0.0;
	protected double mY1 = 0.0;
	protected double mX2 = 0.0;
	protected double mY2 = 0.0;
	protected double mWidth = 0.0;
	
	public TabHookGrip(double x, double y, double width)
	{
		this.mWidth = width;
		this.setX(x);
		this.setY(y);
		return;
	}
	
	public void setX(double x)
	{
		this.mX = x;
		this.mX1 = x - (this.mWidth / 2.0);
		this.mX2 = x + (this.mWidth / 2.0);
		return;
	}
	
	public double getX()
	{
		return this.mX;
	}
	
	public void setY(double y)
	{
		this.mY = y;
		this.mY1 = y - (this.mWidth / 2.0);
		this.mY2 = y + (this.mWidth / 2.0);
		return;
	}
	
	public double getY()
	{
		return this.mY;
	}
	
	public boolean contains(int x, int y)
	{
		if(x < this.mX1 || x > this.mX2){
			return false;
		}
		if(y < this.mY1 || y > this.mY2){
			return false;
		}
		return true;
	}
	
	public Rectangle2D.Double getBounds()
	{
		return new Rectangle2D.Double(this.mX1, this.mY1, this.mX2 - this.mX1, this.mY2 - this.mY1);
	}
}
