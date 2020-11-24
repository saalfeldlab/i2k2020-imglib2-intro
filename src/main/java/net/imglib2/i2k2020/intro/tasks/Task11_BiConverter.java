package net.imglib2.i2k2020.intro.tasks;

import ij.IJ;
import ij.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class Task11_BiConverter {

	/**
	 * Virtually compute a Difference-of-Gaussian from two gaussian convolution using a BiConverter
	 *
	 * @param img
	 * @param sigma1
	 * @param sigma2
	 */
	public static <T extends RealType<T>> void differenceOfGaussian(final RandomAccessibleInterval<T> img, final double sigma1, final double sigma2) {

		final RandomAccessibleInterval<FloatType> img1 = ArrayImgs.floats(Intervals.dimensionsAsLongArray(img));
		final RandomAccessibleInterval<FloatType> img2 = ArrayImgs.floats(Intervals.dimensionsAsLongArray(img));

		Gauss3.gauss(sigma1, Views.extendMirrorSingle(img), img1);
		Gauss3.gauss(sigma2, Views.extendMirrorSingle(img), img2);

		ImageJFunctions.show(img1);
		ImageJFunctions.show(img2);

		//TODO: fill
		/*
		final static public < A, B, C extends Type< C > > RandomAccessibleInterval< C > convert(
				final RandomAccessibleInterval< A > sourceA,
				final RandomAccessibleInterval< B > sourceB,
				final BiConverter< ? super A, ? super B, ? super C > converter,
				final C c )
		*/
		final RandomAccessibleInterval<DoubleType> dog = null;

		ImageJFunctions.show(dog);
	}

	public static void main(String[] args) {

		new ImageJ();

		final RandomAccessibleInterval<UnsignedByteType> blobs = ImagePlusImgs.from(
				IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));

		// 2d DoG
		differenceOfGaussian(blobs, 8, 12);

		final Img<UnsignedByteType> img = ImagePlusImgs.from(
				IJ.openImage("https://preibischlab.mdc-berlin.de/download/lightsheet0.tif.zip"));

		// 3d DoG
		differenceOfGaussian(img, 3, 4);

	}
}
