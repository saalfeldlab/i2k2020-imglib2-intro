package net.imglib2.i2k2020.intro.tasks;

import ij.IJ;
import ij.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class Task10_Converter {

	/**
	 * Show how to use virtual converters to transform types (read-only) and
	 * perform pixel-wise computation, a cosine of the square root in this case
	 * 
	 * @param img
	 */
	public static <T extends RealType<T>> void displayCosine(final RandomAccessibleInterval<T> img) {

		// virtually convert to float
		RandomAccessibleInterval<FloatType> converted = Converters.convert(img, (i, o) -> o.set(i.getRealFloat()), new FloatType());

		// virtually compute with double precision
		//TODO: fill
		RandomAccessibleInterval<DoubleType> result = null;

		ImageJFunctions.show(result);
	}

	/**
	 * Using a writable converter to set all red values of an ARGB image to 0
	 * 
	 * @param img
	 */
	public static void writeConverter1(final RandomAccessibleInterval<ARGBType> img) {

		ImageJFunctions.show(img).setTitle("original");

		// uses a WriteConvertedRandomAccessibleInterval, setting values is
		// supported
		for (final UnsignedByteType red : Views.iterable(Converters.argbChannel(img, 1)))
			red.set(0);

		ImageJFunctions.show(img).setTitle("red=0");
	}

	public static void main(String[] args) {

		new ImageJ();

		final RandomAccessibleInterval<UnsignedByteType> blobs = ImagePlusImgs.from(
				IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));

		final RandomAccessibleInterval<ARGBType> clown = ImagePlusImgs.from(
				IJ.openImage("http://imagej.nih.gov/ij/images/clown.jpg"));

		// cosine of the blobs image
		displayCosine(blobs);

		// cosine of the green channel of the clown image
		displayCosine(Converters.convert(clown, (i, o) -> o.set(ARGBType.green(i.get())), new UnsignedByteType()));

		// set all red values of an ARGB image to 0
		writeConverter1(clown);
	}
}
