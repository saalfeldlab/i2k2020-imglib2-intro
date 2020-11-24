package net.imglib2.i2k2020.intro.tasks;

import java.util.ArrayList;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import bdv.viewer.DisplayMode;
import ij.IJ;
import ij.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.converter.RealFloatConverter;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class ComplexTask3_MeanFilter {

	/**
	 * Virtually compute the mean filter (d=3) in a specific dimension
	 * 
	 * @param img
	 * @param type
	 *            - precision (e.g. FloatType)
	 * @param dim
	 *            - which dimension
	 * @param normalize
	 *            - whether to normalize the output (divide by 3) or not
	 * @return
	 */
	public static <T extends RealType<T>, S extends RealType<S>> RandomAccessibleInterval<S> meanFilter3(
			final RandomAccessibleInterval<T> img,
			final S type,
			final int dim,
			final boolean normalize) {

		final long[] translation = new long[img.numDimensions()];
		translation[dim] = 1;

		// sum of left and right pixel
		// TODO: fill

		// sum of center + (left + right)
		// TODO: fill

		if (normalize)
			return null;
		else
			return null;
	}

	/**
	 * Virtually compute the mean filter (d=3) in all dimensions
	 * 
	 * @param img
	 * @param type
	 *            - precision (e.g. FloatType)
	 */
	public static <T extends RealType<T>, S extends RealType<S>> RandomAccessibleInterval<S> meanFilter3(
			final RandomAccessibleInterval<T> img,
			final S type) {

		// loop over all dimensions consecutively computing the meanFilter3
		// TODO: fill

		return null;
	}

	public static void main(String[] args) {

		new ImageJ();

		final FloatType precision = new FloatType();

		final RandomAccessibleInterval<UnsignedByteType> blobs = ImagePlusImgs.from(
				IJ.openImage("http://imagej.nih.gov/ij/images/boats.gif"));

		ArrayList<RandomAccessibleInterval<FloatType>> imgs = new ArrayList<>();

		imgs.add(Converters.convert(blobs, new RealFloatConverter<UnsignedByteType>(), precision));
		imgs.add(meanFilter3(blobs, precision));

		ImageJFunctions.show(Views.stack(imgs));

		final Img<UnsignedByteType> img = ImagePlusImgs.from(
				IJ.openImage("https://preibischlab.mdc-berlin.de/download/lightsheet0.tif.zip"));

		BdvStackSource<?> bdv;
		bdv = BdvFunctions.show(img, "input");
		bdv = BdvFunctions.show(meanFilter3(img, precision), "mean 3x3x3", new BdvOptions().addTo(bdv));
		bdv.getBdvHandle().getViewerPanel().setDisplayMode(DisplayMode.SINGLE);
		bdv.setDisplayRange(0, 255);
		bdv.setCurrent();
	}
}
