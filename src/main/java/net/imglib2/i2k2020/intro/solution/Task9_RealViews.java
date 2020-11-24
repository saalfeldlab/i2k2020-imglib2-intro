package net.imglib2.i2k2020.intro.solution;

import java.util.Random;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import ij.IJ;
import net.imglib2.Interval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealInterval;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.RealTransformRandomAccessible;
import net.imglib2.realtransform.RealViews;
import net.imglib2.realtransform.ThinplateSplineTransform;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class Task9_RealViews {

	/**
	 * Perform a 45 degree rotation on a 2d image and display the result in
	 * BigDataViewer and ImageJ. ImageJ requires to raster the RealRandomAccess
	 * and to estimate the bounding box.
	 * 
	 * @param img
	 */
	public static <T extends RealType<T>> void rotation(final RandomAccessibleInterval<T> img) {

		AffineTransform2D rotation = new AffineTransform2D();
		rotation.rotate(Math.toRadians(45));

		// linear interpolation
		RealRandomAccessible<T> linear = Views.interpolate(
				Views.extendZero(img),
				new NLinearInterpolatorFactory<>());

		RealRandomAccessible<T> transformed = RealViews.affine(linear, rotation);

		// show the realrandomaccessibles
		BdvStackSource<?> bdv;
		BdvOptions options = new BdvOptions();

		if (img.numDimensions() == 2)
			options = options.is2D();

		// BigDataViewer can show RealRandomAccessibles directly (ImageJ cannot)
		bdv = BdvFunctions.show(img, "input", options);
		bdv.setColor(new ARGBType(ARGBType.rgba(0, 255, 0, 0)));

		bdv = BdvFunctions.show(transformed, img, "transformed", options.addTo(bdv));
		bdv.setColor(new ARGBType(ARGBType.rgba(255, 0, 255, 0)));

		// find the boundaries of the rotated image
		RealInterval realInterval = rotation.estimateBounds(img);
		Interval intervalTransformed = Intervals.largestContainedInterval(realInterval);

		// the interval we want to display is the union of the interval from the
		// transformed
		// and the original image
		Interval interval = Intervals.union(intervalTransformed, img);

		// display as a rastered image in ImageJ
		RandomAccessible<T> rastered = Views.raster(transformed);
		RandomAccessibleInterval<T> rasteredInterval = Views.interval(rastered, interval);

		ImageJFunctions.show(Views.interval(Views.extendZero(img), interval)).setTitle("transformed");
		ImageJFunctions.show(rasteredInterval).setTitle("rotated");
	}

	/**
	 * Illustrate how to do a non-rigid n-dimensional ThinPlateSpline
	 * transformation and display with BigDataViewer
	 * 
	 * @param img
	 */
	public static <T extends RealType<T>> void thinPlateSpline(final RandomAccessibleInterval<T> img) {

		final double maxDist = 5;
		final int numPoints = 20;
		final int n = img.numDimensions();

		final double[][] p = new double[n][numPoints];
		final double[][] q = new double[n][numPoints];

		final Random rnd = new Random(2323);

		for (int i = 0; i < numPoints; ++i) {
			for (int d = 0; d < n; ++d) {
				p[d][i] = rnd.nextInt((int)img.dimension(d)) + img.min(d);
				q[d][i] = p[d][i] + (rnd.nextDouble() - 0.5) * maxDist * 2;
			}
		}

		ThinplateSplineTransform tps = new ThinplateSplineTransform(p, q);
		// WrappedIterativeInvertibleRealTransform<ThinplateSplineTransform>
		// tpsInv = new WrappedIterativeInvertibleRealTransform<>( tps );

		// linear interpolation
		RealRandomAccessible<T> linear = Views.interpolate(
				Views.extendZero(img),
				new NLinearInterpolatorFactory<>());

		RealRandomAccessible<T> transformed =
				// RealViews.transform( linear, tpsInv );
				new RealTransformRandomAccessible<>(linear, tps);

		// show the realrandomaccessibles
		BdvStackSource<?> bdv;
		BdvOptions options = new BdvOptions();

		if (img.numDimensions() == 2)
			options = options.is2D();

		// BigDataViewer can show RealRandomAccessibles directly (ImageJ cannot)
		bdv = BdvFunctions.show(img, "input", options);
		bdv.setColor(new ARGBType(ARGBType.rgba(0, 255, 0, 0)));

		bdv = BdvFunctions.show(transformed, img, "thin-plate transformed", options.addTo(bdv));
		bdv.setColor(new ARGBType(ARGBType.rgba(255, 0, 255, 0)));
	}

	public static void main(String[] args) {

		final Img<UnsignedByteType> blobs = ImagePlusImgs.from(
				IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));

		// apply a 45 degree rotation to a 2d image
		rotation(blobs);

		// random deformations to an nd image
		thinPlateSpline(blobs);
	}
}
