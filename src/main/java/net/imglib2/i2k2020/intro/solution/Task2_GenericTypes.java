package net.imglib2.i2k2020.intro.solution;

import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;

public class Task2_GenericTypes {

	/**
	 * Add a value from an image of {@link NumericType}
	 * 
	 * @param img
	 *            - the input
	 * @param value
	 *            - the value
	 */
	public static <T extends NumericType<T>> void add(final Img<T> img, final T value) {

		for (final T pixelValue : img)
			pixelValue.add(value);
	}

	/**
	 * Subtract a value from an image of {@link RealType}
	 * 
	 * @param img
	 *            - the input
	 */
	public static <T extends RealType<T>> void sqrt(final Img<T> img) {

		for (final T pixelValue : img)
			pixelValue.setReal(Math.sqrt(pixelValue.getRealDouble()));
	}

	public static void main(String[] args) {

		// create small images of various types
		final Img<FloatType> imgF = ArrayImgs.floats(5, 5);
		final Img<UnsignedByteType> imgUB = ArrayImgs.unsignedBytes(5, 5);
		final Img<ComplexDoubleType> imgC = ArrayImgs.complexDoubles(5, 5);
		final Img<ARGBType> imgARGB = ArrayImgs.argbs(5, 5);

		// adding works with all these types
		add(imgF, new FloatType(2.5f));
		add(imgUB, new UnsignedByteType(5));
		add(imgC, new ComplexDoubleType(1.0, 2.0));
		add(imgARGB, new ARGBType(ARGBType.rgba(128, 64, 64, 0)));

		for (final ARGBType type : imgARGB)
			System.out.println(type);

		// square root only with some of them
		sqrt(imgF);
		sqrt(imgUB);
		// sqrt( imgC ); <<< build error
		// sqrt( imgARGB ); <<< build error
	}
}
