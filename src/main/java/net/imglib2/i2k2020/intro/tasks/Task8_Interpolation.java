package net.imglib2.i2k2020.intro.tasks;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import bdv.viewer.DisplayMode;
import ij.IJ;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealRandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.interpolation.randomaccess.NearestNeighborInterpolatorFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.view.Views;

public class Task8_Interpolation {

	/**
	 * Visualize different types of interplolation in BigDataViewer, which can
	 * show RealRandomAccesses(!) directly
	 * 
	 * @param img
	 */
	public static <T extends RealType<T>> void interpolation(final RandomAccessibleInterval<T> img) {

		// which area to render
		Interval interval = new FinalInterval(img);

		// nearest neighbor interpolation
		RealRandomAccessible<T> nn = Views.interpolate(
				Views.extendZero(img),
				new NearestNeighborInterpolatorFactory<>());

		// linear interpolation
		//TODO: fill

		// lanczos interpolation
		//TODO: fill

		// show the realrandomaccessibles
		BdvStackSource<?> bdv;
		BdvOptions options = new BdvOptions();

		if (img.numDimensions() == 2)
			options = options.is2D();

		// BigDataViewer can show RealRandomAccessibles directly (ImageJ cannot)
		bdv = BdvFunctions.show(nn, interval, "nearest neighbor", options);

		//TODO: fill

		// enable single-source mode
		bdv.getBdvHandle().getViewerPanel().setDisplayMode(DisplayMode.SINGLE);
	}

	public static void main(String[] args) {

		final Img<UnsignedByteType> blobs = ImagePlusImgs.from(
				IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));

		interpolation(blobs);
	}
}
