package net.imglib2.i2k2020.intro.tasks;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import ij.IJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class ComplexTask1_Thresholding {

	/**
	 * Using a converter to implement a thresholding operation
	 *
	 * @param img
	 * @param threshold
	 * @return
	 */
	public static <T extends Comparable<T>> RandomAccessibleInterval<BitType> threshold(
			final RandomAccessibleInterval<T> img,
			final T threshold) {

		// virtual thresholding with a converter

		// TODO: fill
		return null;
	}

	/**
	 * Visualize the result of the thresholding
	 * 
	 * @param img
	 * @param threshold
	 */
	public static <T extends RealType<T>> void testThresholding(
			final RandomAccessibleInterval<T> img,
			final T threshold) {

		RandomAccessibleInterval<BitType> segmented = threshold(img, threshold);

		// show the realrandomaccessibles
		BdvStackSource<?> bdv;
		BdvOptions options = new BdvOptions();

		if (img.numDimensions() == 2)
			options = options.is2D();

		// BigDataViewer can show RealRandomAccessibles directly (ImageJ cannot)
		bdv = BdvFunctions.show(img, "input", options);
		bdv.setColor(new ARGBType(ARGBType.rgba(0, 255, 0, 0)));

		bdv = BdvFunctions.show(segmented, "segmented", options.addTo(bdv));
		bdv.setColor(new ARGBType(ARGBType.rgba(255, 0, 255, 0)));
	}

	public static void main(String[] args) {

		final Img<UnsignedByteType> blobs = ImagePlusImgs.from(
				IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));

		testThresholding(blobs, new UnsignedByteType(128));
	}
}
