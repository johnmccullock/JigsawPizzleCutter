package jigsaw;

import java.awt.geom.Rectangle2D;

public class TabHook
{
	private TabHookGrip[] mGrips = new TabHookGrip[9];
	
	public TabHook(double gripWidth)
	{
		this.mGrips[0] = new TabHookAnchor(0.0, 0.0, gripWidth);
		this.mGrips[1] = new TabHookControl(0.0, 0.0, gripWidth);
		this.mGrips[2] = new TabHookAnchor(0.0, 0.0, gripWidth);
		this.mGrips[3] = new TabHookControl(0.0, 0.0, gripWidth);
		this.mGrips[4] = new TabHookAnchor(0.0, 0.0, gripWidth);
		this.mGrips[5] = new TabHookControl(0.0, 0.0, gripWidth);
		this.mGrips[6] = new TabHookAnchor(0.0, 0.0, gripWidth);
		this.mGrips[7] = new TabHookControl(0.0, 0.0, gripWidth);
		this.mGrips[8] = new TabHookAnchor(0.0, 0.0, gripWidth);
		return;
	}
	
	public void moveGrip(int index, double x, double y)
	{
		if(index < 0 || index > 8){
			return;
		}
		this.mGrips[index].setX(x);
		this.mGrips[index].setY(y);
		return;
	}
	
	public double getGripX(int index)
	{
		return this.mGrips[index].getX();
	}
	
	public double getGripY(int index)
	{
		return this.mGrips[index].getY();
	}
	
	public Rectangle2D.Double getGripBounds(int index)
	{
		return this.mGrips[index].getBounds();
	}
	
	public boolean gripContains(int index, int x, int y)
	{
		return this.mGrips[index].contains(x, y);
	}
	
	public TabHookGrip getGrip(int index)
	{
		return this.mGrips[index];
	}
	
	@SuppressWarnings("rawtypes")
	public Class getGripClass(int index)
	{
		return this.mGrips[index].getClass();
	}
}
