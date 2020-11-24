package net.imglib2.i2k2020.intro.solution;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ij.IJ;
import ij.ImageJ;
import net.imglib2.FinalDimensions;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.fft2.FFT;
import net.imglib2.algorithm.fft2.FFTConvolution;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.converter.Converters;
import net.imglib2.converter.RealFloatConverter;
import net.imglib2.converter.readwrite.RealFloatSamplerConverter;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.RealSum;
import net.imglib2.view.Views;

public class ComplexTask4_FourierTemplateMatching {

	/**
	 * Makes a physical copy of the RAI
	 * 
	 * @param input
	 *            - typically virtually converted input
	 * @param outputFactory
	 *            - factory for the output
	 * @return
	 */
	public static <T extends Type<T>> RandomAccessibleInterval<T> materialize(final Interval input, final ImgFactory<T> outputFactory) {

		final Img<T> output = outputFactory.create(input);

		return Views.isZeroMin(input) ? output : Views.translate(output, input.minAsLongArray());
	}

	/**
	 * normalizes so the sum of all pixels is 1 (this is necessary for
	 * (de)convolution
	 * 
	 * @param img
	 *            - the PSF
	 * @return a virtually normalized PSF
	 */
	public static <T extends RealType<T>> RandomAccessibleInterval<FloatType> normalize(
			final RandomAccessibleInterval<T> img) {

		RealSum sum = new RealSum();

		for (final T type : Views.iterable(img))
			sum.add(type.getRealDouble());

		final double s = sum.getSum();

		return Converters.convert(img, (i, o) -> o.setReal(i.getRealDouble() / s), new FloatType());
	}

	public static <T extends RealType<T>> Img<ComplexFloatType> fft(
			final RandomAccessibleInterval<T> img) {

		final int numDimensions = img.numDimensions();

		final long[] dim = new long[numDimensions];
		img.dimensions(dim);

		// compute the size of the complex-valued output and the required
		// padding based on the prior extended input image
		final long[] paddedDimensions = new long[numDimensions];
		final long[] fftDimensions = new long[numDimensions];

		FFTMethods.dimensionsRealToComplexFast(FinalDimensions.wrap(dim), paddedDimensions, fftDimensions);

		// compute the new interval for the input image
		final Interval fftInterval = FFTMethods.paddingIntervalCentered(img, FinalDimensions.wrap(paddedDimensions));

		// use mirroing outofbounds
		final RandomAccessibleInterval<T> imgInput = Views.interval(Views.extendMirrorSingle(img), fftInterval);

		// compute the FFT
		final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		final Img<ComplexFloatType> fftImg = FFT.realToComplex(imgInput, new ArrayImgFactory<>(new ComplexFloatType()), service);
		service.shutdown();

		return fftImg;
	}

	/**
	 * Simulates an image, convolves it, and finds the points again
	 */
	public static void simulate() {

		// create an empty image
		Img<FloatType> img = ArrayImgs.floats(400, 400, 200);

		// put random noise and dots
		Random rnd = new Random();

		for (FloatType t : img)
			t.set(rnd.nextFloat());

		RandomAccess<FloatType> ra = img.randomAccess();

		for (int i = 0; i < 500; ++i) {
			for (int d = 0; d < img.numDimensions(); ++d)
				ra.setPosition(rnd.nextInt((int)img.dimension(d)), d);

			ra.get().add(new FloatType(100));
		}

		ImageJFunctions.show(img).setTitle("image");

		// create a gaussian kernel
		final RandomAccessibleInterval<FloatType> kernel = ImagePlusImgs.from(
				IJ.openImage("https://preibischlab.mdc-berlin.de/download/psf-lightsheet.tif"));

		// blur the kernel a bit to have a more visible effect
		Gauss3.gauss(2, Views.extendZero(kernel), kernel);

		ImageJFunctions.show(kernel).setTitle("kernel");

		Img<FloatType> convolved = img.factory().create(img);

		// FFT Convolution
		FFTConvolution<FloatType> conv = new FFTConvolution<>(
				img,
				normalize(kernel), // virtually normalize the kernel
				convolved,
				new ArrayImgFactory<>(new ComplexFloatType()));

		conv.convolve();

		ImageJFunctions.show(convolved).setTitle("convolved");

		// FFT Deconvolution / Correlation
		Img<FloatType> deconvolved = img.factory().create(img);

		FFTConvolution<FloatType> deconv = new FFTConvolution<>(
				convolved,
				normalize(kernel), // virtually normalize the kernel
				deconvolved,
				new ArrayImgFactory<>(new ComplexFloatType()));

		deconv.setDiv(true);
		deconv.convolve();

		// blur the result a little
		Gauss3.gauss(0.75, Views.extendMirrorSingle(deconvolved), deconvolved);

		ImageJFunctions.show(deconvolved).setTitle("deconv/restored");
	}

	public static void main(String[] args) {

		new ImageJ();

		final RandomAccessibleInterval<UnsignedByteType> blobs = ImagePlusImgs.from(
				IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));

		ImageJFunctions.show(blobs).setTitle("blobs");

		/**
		 * illustrate how to compute an FFT
		 */
		ImageJFunctions.show(fft(blobs)).setTitle("power spectrum of fft of blobs");

		/**
		 * illustrate how to perform FFT-based convolution
		 */
		// mean filtering
		final Img<UnsignedByteType> kernel = ArrayImgs.unsignedBytes(13, 13);

		for (final UnsignedByteType t : kernel)
			t.setOne();

		// has to be computed with FloatType as the kernel needs to be
		// normalized
		// (alternatively use LongType or IntType and divide by the sum of the
		// kernel afterwards)
		FFTConvolution<FloatType> conv = new FFTConvolution<>(
				Converters.convert( // use a writeable converter
						blobs,
						new RealFloatSamplerConverter<>()),
				normalize(kernel), // virtually normalize the kernel
				new ArrayImgFactory<>(new ComplexFloatType()));

		conv.convolve();

		ImageJFunctions.show(blobs).setTitle("blobs convolved with mean kernel 13x13");

		/**
		 * illustrate how to perform FFT-based correlation/deconvolution
		 */
		final RandomAccessibleInterval<UnsignedByteType> imgIn = ImagePlusImgs.from(
				IJ.openImage("https://preibischlab.mdc-berlin.de/download/lightsheet0.tif.zip"));

		// problem: the range of unsignedbytetype is not sufficient for the
		// output of the
		// correlation/deconvolution (lies between -2000 and 2000),
		// therefore we materialize a float image of the same size and location
		final RandomAccessibleInterval<FloatType> imgFloat = materialize(imgIn, new ArrayImgFactory<>(new FloatType()));

		final RandomAccessibleInterval<FloatType> template = ImagePlusImgs.from(
				IJ.openImage("https://preibischlab.mdc-berlin.de/download/psf-lightsheet.tif"));

		FFTConvolution<FloatType> fc = new FFTConvolution<>(
				Converters.convert(imgIn, new RealFloatConverter<>(), new FloatType()),
				normalize(template),
				imgFloat,
				new ArrayImgFactory<>(new ComplexFloatType()));

		fc.setDiv(true);
		fc.convolve();

		// blur the very unstable result of the FFT-based deconvolution
		Gauss3.gauss(0.75, Views.extendMirrorSingle(imgFloat), imgFloat);

		ImageJFunctions.show(imgIn).setTitle("input");
		ImageJFunctions.show(template).setTitle("template");
		ImageJFunctions.show(imgFloat).setDisplayRange(-2000, 2000);

		/**
		 * Run a simulation that first convolves and then deconvolves the image
		 */
		simulate();
	}
}
