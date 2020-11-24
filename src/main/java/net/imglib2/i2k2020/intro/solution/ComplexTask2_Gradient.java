package net.imglib2.i2k2020.intro.solution;

import java.util.ArrayList;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import ij.IJ;
import ij.ImageJ;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.converter.Converters;
import net.imglib2.converter.RealFloatConverter;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

public class ComplexTask2_Gradient {

	/**
	 * virtually create a RAI that is mirrored and shifted according to the
	 * translation vector
	 * 
	 * @param img
	 * @param translation
	 * @return
	 */
	public static <T> RandomAccessibleInterval<T> shiftMirrored(final RandomAccessibleInterval<T> img, final long[] translation) {

		return Views.interval(
				Views.translate(
						Views.extendMirrorSingle(img),
						translation),
				img);
	}

	/**
	 * virtually create a RAI that is mirrored and inversely shifted according
	 * to the translation vector
	 * 
	 * @param img
	 * @param translation
	 * @return
	 */
	public static <T> RandomAccessibleInterval<T> shiftInverseMirrored(final RandomAccessibleInterval<T> img, final long[] translation) {

		return Views.interval(
				Views.translateInverse(
						Views.extendMirrorSingle(img),
						translation),
				img);
	}

	/**
	 * Compute the gradient in a specific dimension
	 * 
	 * @param img
	 *            - the input
	 * @param type
	 *            - the precision (e.g. FloatType)
	 * @param dim
	 *            - which dimension
	 * @return
	 */
	public static <T extends RealType<T>, S extends RealType<S>> RandomAccessibleInterval<S> gradient(
			final RandomAccessibleInterval<T> img,
			final S type,
			final int dim) {

		final long[] translation = new long[img.numDimensions()];
		translation[dim] = 1;

		return Converters.convert(
				shiftMirrored(img, translation),
				shiftInverseMirrored(img, translation),
				(i1, i2, o) -> o.setReal(i1.getRealDouble() - i2.getRealDouble()),
				type);
	}

	/**
	 * Compute the magnitude of the gradient in all dimensions
	 * 
	 * @param img
	 * @param type
	 *            - the precision (e.g. FloatType)
	 * @return
	 */
	public static <T extends RealType<T>, S extends RealType<S>> RandomAccessibleInterval<S> gradientMagnitude(
			final RandomAccessibleInterval<T> img,
			final S type) {

		if (img.numDimensions() == 1) {
			return gradient(img, type, 0);
		} else {
			// a bi-converter for the sqr(gradients) in x and y
			RandomAccessibleInterval<S> gradients = Converters.convert(
					gradient(img, type, 0),
					gradient(img, type, 1),
					(i1, i2, o) -> o.setReal(Math.pow(i1.getRealDouble(), 2) + Math.pow(i2.getRealDouble(), 2)),
					type);

			// we covered the first two dimensions
			for (int d = 2; d < img.numDimensions(); ++d) {
				// add the sqr(gradient) of the next dimensions to it
				gradients = Converters.convert(
						gradients,
						gradient(img, type, d),
						(i1, i2, o) -> o.setReal(i1.getRealDouble() + Math.pow(i2.getRealDouble(), 2)),
						type);
			}

			// still do Math.sqrt
			return Converters.convert(gradients, (i, o) -> o.setReal(Math.sqrt(i.getRealDouble())), type);
		}
	}

	public static void main(String[] args) {

		new ImageJ();

		final RandomAccessibleInterval<UnsignedByteType> blobs = ImagePlusImgs.from(
				IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));

		ArrayList<RandomAccessibleInterval<FloatType>> imgs = new ArrayList<>();

		imgs.add(Converters.convert(blobs, new RealFloatConverter<UnsignedByteType>(), new FloatType()));
		imgs.add(gradient(blobs, new FloatType(), 0));
		imgs.add(gradient(blobs, new FloatType(), 1));
		imgs.add(gradientMagnitude(blobs, new FloatType()));

		ImageJFunctions.show(Views.stack(imgs));

		final Img<UnsignedByteType> img = ImagePlusImgs.from(
				IJ.openImage("https://preibischlab.mdc-berlin.de/download/lightsheet0.tif.zip"));

		BdvStackSource<?> bdv;
		bdv = BdvFunctions.show(img, "input");
		bdv = BdvFunctions.show(gradientMagnitude(img, new FloatType()), "gradient", new BdvOptions().addTo(bdv));
		bdv.setColor(new ARGBType(ARGBType.rgba(0, 255, 0, 0)));
		bdv.setDisplayRange(0, 255);
	}
}
