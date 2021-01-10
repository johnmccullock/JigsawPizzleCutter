package jigsaw;

public class ZoomIterator
{
	public final double[] ZOOM_STEPS = new double[]{0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.1, 0.12, 0.15, 0.2, 0.25, 0.3, 0.4, 0.5, 0.6, 0.8, 1.0, 1.2, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0, 12.0, 15.0, 20.0, 25.0, 30.0, 40.0, 50.0};
	public static final int DEFAULT_MAGNIFICATION_INDEX = 18; // 18th index is 1.0, or 1-to-1.
	
	private int mZoomIndex = DEFAULT_MAGNIFICATION_INDEX;
	
	public ZoomIterator() { return; }
	
	public ZoomIterator(double zoom)
	{
		this.setZoom(zoom);
		return;
	}
	
	public double getZoom()
	{
		return ZOOM_STEPS[this.mZoomIndex];
	}
	
	public void decrease()
	{
		this.mZoomIndex = this.mZoomIndex - 1 < 0 ? 0 : this.mZoomIndex - 1;
		return;
	}
	
	public void increase()
	{
		this.mZoomIndex = this.mZoomIndex >= ZOOM_STEPS.length - 1 ? ZOOM_STEPS.length - 1 : this.mZoomIndex + 1;
		return;
	}
	
	public void set1To1()
	{
		this.mZoomIndex = DEFAULT_MAGNIFICATION_INDEX;
		return;
	}
	
	public void setZoom(double zoom)
	{
		this.mZoomIndex = this.getClosest(zoom);
		return;
	}
	
	private int getClosest(double zoom)
	{
		if(zoom < ZOOM_STEPS[0]){
			return 0;
		}
		if(zoom > ZOOM_STEPS[ZOOM_STEPS.length - 1]){
			return ZOOM_STEPS.length - 1;
		}
		int results = DEFAULT_MAGNIFICATION_INDEX;
		for(int i = 0; i < ZOOM_STEPS.length - 1; i++)
		{
			if(zoom > ZOOM_STEPS[i] && zoom < ZOOM_STEPS[i + 1]){
				results = this.getClosest(i, i + 1, zoom);
				break;
			}
		}
		return results;
	}
	
	private int getClosest(int smaller, int larger, double zoom)
	{
		double middle = ZOOM_STEPS[larger] - ZOOM_STEPS[smaller] / 2.0;
		if(zoom < middle){
			return smaller;
		}else{
			return larger;
		}
	}
}
