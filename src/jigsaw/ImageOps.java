package jigsaw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageOps
{
	public static BufferedImage resizeNearestNeighbor(BufferedImage source, double factor)
	{
		AffineTransform at = new AffineTransform();
		at.scale(factor, factor);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(source, null);
	}
	
	public static BufferedImage resizeNearestNeighbor(BufferedImage source, double horizontalFactor, double verticalFactor)
	{
		AffineTransform at = new AffineTransform();
		at.scale(horizontalFactor, verticalFactor);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(source, null);
	}
}
