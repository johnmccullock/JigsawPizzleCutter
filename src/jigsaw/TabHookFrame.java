package jigsaw;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class TabHookFrame
{
	private TabHookData mPrototype = null;
	private Path2D.Double[] mFrame = null;
	private int mWidth = 0;
	private int mHeight = 0;
	private int mRows = 0;
	private int mCols = 0;
	
	public TabHookFrame(int width, int height, int rows, int cols, double gripWidth)
	{
		this.mWidth = width;
		this.mHeight = height;
		this.mRows = rows;
		this.mCols = cols;
		return;
	}
	
	public void setPrototype(TabHookData data)
	{
		this.mPrototype = data;
		return;
	}
	
	public void layoutFrame(double upperLeftX, double upperLeftY, double prototypeHeight)
	{
		this.mFrame = new Path2D.Double[this.mCols * this.mRows];
		double colInc = this.mWidth / (double)this.mCols;
		double rowInc = this.mHeight / (double)this.mRows;
		TabHookData even = this.prepareEven(colInc, rowInc, prototypeHeight);
		TabHookData odd = this.prepareOdd(even);
		
		int k = 0;
		for(int j = 0; j < this.mRows; j++)
		{
			boolean orientation = j % 2 == 0;
			for(int i = 0; i < this.mCols; i++)
			{
				TabHookData temp = orientation ? new TabHookData(even) : new TabHookData(odd);
				if(j == 0){
					// Trim tabhook off top edge.
					temp.north = null;
				}
				if(j == this.mRows - 1){
					// Trim tabhook off bottom edge.
					temp.south = null;
				}
				if(i == 0){
					// Trim tabhook off left edge.
					temp.west = null;
				}
				if(i == this.mCols - 1){
					// Trim tabhook off right edge.
					temp.east = null;
				}
				temp.translate((colInc * i) + upperLeftX, (rowInc * j) + upperLeftY);
				this.mFrame[k] = temp.getShape();
				orientation = !orientation;
				k++;
			}
		}
		return;
	}
	
	private TabHookData prepareEven(double width, double height, double prototypeHeight)
	{
		double w = width / prototypeHeight;
		double h = height / prototypeHeight;
		TabHookData item = new TabHookData();
		
		item.northwest.x = w * this.mPrototype.northwest.x;
		item.northwest.y = h * this.mPrototype.northwest.y;
		item.northeast.x = w * this.mPrototype.northeast.x;
		item.northeast.y = h * this.mPrototype.northeast.y;
		item.southeast.x = w * this.mPrototype.southeast.x;
		item.southeast.y = h * this.mPrototype.southeast.y;
		item.southwest.x = w * this.mPrototype.southwest.x;
		item.southwest.y = h * this.mPrototype.southwest.y;
		
		for(int i = 0; i < 9; i++)
		{
			item.north[i] = new Point2D.Double(this.mPrototype.north[i].getX() * w, this.mPrototype.north[i].getY() * h);
			item.east[i] = new Point2D.Double(this.mPrototype.east[i].getX() * w, this.mPrototype.east[i].getY() * h);
			item.south[i] = new Point2D.Double(this.mPrototype.south[i].getX() * w, this.mPrototype.south[i].getY() * h);
			item.west[i] = new Point2D.Double(this.mPrototype.west[i].getX() * w, this.mPrototype.west[i].getY() * h);
		}
		return item;
	}
	
	private TabHookData prepareOdd(TabHookData even)
	{
		TabHookData odd = new TabHookData();
		odd.northwest.x = even.northwest.x;
		odd.northwest.y = even.northwest.y;
		odd.northeast.x = even.northeast.x;
		odd.northeast.y = even.northeast.y;
		odd.southeast.x = even.southeast.x;
		odd.southeast.y = even.southeast.y;
		odd.southwest.x = even.southwest.x;
		odd.southwest.y = even.southwest.y;
		
		double width = even.northeast.x - even.northwest.x;
		double height = even.southwest.y - even.northwest.y;
		for(int i = 0, j = 8; i < 9; i++, j--)
		{
			odd.north[i] = new Point2D.Double(even.south[j].x, even.south[j].y - height);
			odd.east[i] = new Point2D.Double(even.west[j].x + width, even.west[j].y);
			odd.south[i] = new Point2D.Double(even.north[j].x, even.north[j].y + height);
			odd.west[i] = new Point2D.Double(even.east[j].x - width, even.east[j].y);
		}
		
		return odd;
	}
	
	public Path2D.Double[] getFrame()
	{
		return this.mFrame;
	}
}
