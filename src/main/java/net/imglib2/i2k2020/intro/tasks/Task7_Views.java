package net.imglib2.i2k2020.intro.tasks;

import java.util.ArrayList;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import ij.IJ;
import ij.ImageJ;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class Task7_Views {

	/**
	 * Crop an image using Views and display it overlaid with the original using
	 * BigDataViewer
	 * 
	 * @param img
	 */
	public static <T extends NumericType<T>> void crop(final RandomAccessibleInterval<T> img) {

		// show the full image
		BdvStackSource<?> bdv = BdvFunctions.show(img, "img");

		// make an interval instance from the image that we modify
		Interval displayInterval = new FinalInterval(img);

		// define an interval that shrinks the image by 1/3 of its dimension on
		// each side
		for (int d = 0; d < img.numDimensions(); ++d)
			displayInterval = Intervals.expand(displayInterval, -img.dimension(d) / 3, d);

		System.out.println("full interval: " + Util.printInterval(img));

		// apply the interval to the img
		RandomAccessibleInterval<T> cropped = Views.interval(img, displayInterval);

		System.out.println("cropped interval: " + Util.printInterval(cropped));

		// show the cropped image
		BdvFunctions.show(cropped, "cropped", new BdvOptions().addTo(bdv)).setColor(new ARGBType(ARGBType.rgba(0, 255, 0, 0)));
	}

	/**
	 * Permute the axes of a 3d image and show them with ImageJ
	 * 
	 * @param img
	 */
	public static <T extends NumericType<T>> void permuteAxes(final RandomAccessibleInterval<T> img) {

		if (img.numDimensions() != 3) {
			System.out.println("this is an example for a 3d image.");
			return;
		}

		ImageJFunctions.show(img).setTitle("xy");
		ImageJFunctions.show(Views.permute(img, 0, 1)).setTitle("yx");
		ImageJFunctions.show(Views.permute(img, 0, 2)).setTitle("xz");
		ImageJFunctions.show(Views.permute(img, 1, 2)).setTitle("yz");
	}

	/**
	 * show central hyperslices of all dimensions with ImageJ
	 * 
	 * @param img
	 */
	public static <T extends NumericType<T>> void hyperSlicing(final RandomAccessibleInterval<T> img) {

		// show the central slice in all dimensions
		for (int d = 0; d < img.numDimensions(); ++d)
			ImageJFunctions.show(
					Views.hyperSlice(img, d, (img.max(d) - img.min(d)) / 2 + img.min(d))).setTitle("cut " + d);
	}

	/**
	 * Illustrate how to reduce an RAI to 2 dimensions by hyperslicing, and then
	 * add a 3d dimension with size 50
	 * 
	 * @param img
	 */
	public static <T extends NumericType<T>> void addRemoveDimensions(final RandomAccessibleInterval<T> img) {

		System.out.println("img: " + Util.printInterval(img));

		RandomAccessibleInterval<T> sliced = img;

		// hyperslice the image until it is a 2d image
		while (sliced.numDimensions() > 2)
			sliced = Views.hyperSlice(
					sliced,
					0,
					(img.max(0) - img.min(0)) / 2 + img.min(0));

		System.out.println("sliced: " + Util.printInterval(sliced));

		// make it a 2d image with size==50 in y
		RandomAccessibleInterval<T> expanded = Views.addDimension(sliced, 0, 49);

		System.out.println("expanded: " + Util.printInterval(sliced));

		ImageJFunctions.show(expanded);
	}

	/**
	 * Illustrate how to make an RAI iterable and compute its max, then show
	 * that same iteration order can be enforced with Views.flatIterable
	 * 
	 * @param img
	 */
	public static <T extends RealType<T>> void makeIterable(final RandomAccessibleInterval<T> img) {

		// iterate a RandomAccessibleInterval and use the max method
		System.out.println("max intensity: " + Task3_GenericAccess.max(Views.iterable(img)));

		// iterate a CellImg and an ArrayImg the same way
		// array image
		final Img<UnsignedByteType> imgA = ArrayImgs.unsignedBytes(5, 5);

		// cell image with blocksize 2
		final ImgFactory<UnsignedByteType> cellImgFactory = new CellImgFactory<>(new UnsignedByteType(), 2);
		final Img<UnsignedByteType> imgB = cellImgFactory.create(5, 5);

		// iterate as in task4
		Task4_LargerImages.testIteration(imgA, imgB);

		System.out.println();

		// iterate CellImg as flatIterable
		Task4_LargerImages.testIteration(imgA, Views.flatIterable(imgB));
	}

	/**
	 * Illustrate outofbounds strategies for NumericType images and display
	 * using ImageJ
	 * 
	 * Note: once the deprecated method for extendValue is removed, this code
	 * will not be deprecated anymore.
	 * 
	 * Can you figure out know why?
	 * 
	 * @param img
	 */
	public static <T extends NumericType<T>> void outOfBounds(final RandomAccessibleInterval<T> img) {

		ImageJFunctions.show(img).setTitle("input image");

		// an interval that adds 250 pixels all around
		Interval expansion = Intervals.expand(img, 250);

		// will crash, no image data
		// ImageJFunctions.show( Views.interval( img, expansion ) );

		// use an expanded interlval and try following outofbounds: zero,
		// mirrorsingle, periodic, value and show with ImageJ

		// TODO: fill

		// random out of bounds does not work as it requires a RealType (see
		// below)
	}

	/**
	 * Illustrate outofbounds strategy that only works with RealType and display
	 * using ImageJ
	 * 
	 * @param img
	 */
	public static <T extends RealType<T>> void outOfBoundsRealType(final RandomAccessibleInterval<T> img) {

		ImageJFunctions.show(img).setTitle("input image");

		// an interval that adds 250 pixels all around
		Interval expansion = Intervals.expand(img, 250);

		//TODO: fill
	}

	/**
	 * Illustrate what subsampling does: leaving out pixels
	 * 
	 * @param img
	 */
	public static <T extends NumericType<T>> void subsampling(final RandomAccessibleInterval<T> img) {

		for (int step = 1; step <= 10; ++step)
			ImageJFunctions.show(Views.subsample(img, step)).setTitle("subsampling=" + step);
	}

	/**
	 * Shows how to mirror images using Views.translate, Views.interval and
	 * MirrorOutOfBoundsStrategy
	 * 
	 * @param img
	 */
	public static <T extends NumericType<T>> void mirroring(final RandomAccessibleInterval<T> img) {

		ImageJFunctions.show(img).setTitle("original");

		// mirror in each dimension using mirror outofbounds
		for (int d = 0; d < img.numDimensions(); ++d) {
			final long[] translation = new long[img.numDimensions()];
			translation[d] = img.dimension(d);

			Interval mirrorInterval = Intervals.translate(img, translation);
			ImageJFunctions.show(
					Views.interval(
							Views.extendMirrorSingle(img),
							mirrorInterval))
					.setTitle("mirror d=" + d);
		}

		// mirror in all dimensions
		final long[] translation = new long[img.numDimensions()];
		img.dimensions(translation);
		Interval mirrorInterval = Intervals.translate(img, translation);

		ImageJFunctions.show(
				Views.interval(
						Views.extendMirrorSingle(img),
						mirrorInterval))
				.setTitle("mirror all dims");
	}

	/**
	 * Illustrate how to use the Views.stack method for stacking n-1 dimensional
	 * RAIs into an n-dimensional RAI
	 * 
	 * @param img
	 * @param extraImg
	 * @param step
	 */
	public static <T extends NumericType<T>> void stacking(final RandomAccessibleInterval<T> img, RandomAccessibleInterval<T> extraImg, final int step) {

		ImageJFunctions.show(img).setTitle("original");

		// restack every step-th (n-1 dim) image of the n-dim image (e.g. every
		// 10th slice of a 3d image)
		final ArrayList<RandomAccessibleInterval<T>> hyperSlices = new ArrayList<>();

		for (int pos = 0; pos < img.dimension(img.numDimensions() - 1); pos += step)
			hyperSlices.add(Views.hyperSlice(img, img.numDimensions() - 1, pos));

		// add an extra image at the end, assuming it has n-1 dimensions
		// compared to img
		while (extraImg.numDimensions() >= img.numDimensions())
			extraImg = Views.hyperSlice(
					extraImg,
					0,
					(img.max(0) - img.min(0)) / 2 + img.min(0));

		hyperSlices.add(Views.interval(Views.extendMirrorSingle(extraImg), hyperSlices.get(0)));

		ImageJFunctions.show(Views.stack(hyperSlices)).setTitle("restacked and extra image");
	}

	public static void main(String[] args) {

		new ImageJ();

		final Img<UnsignedByteType> img = ImagePlusImgs.from(IJ.openImage("https://preibischlab.mdc-berlin.de/download/lightsheet0.tif.zip"));
		final Img<ARGBType> clown = ImagePlusImgs.from(IJ.openImage("http://imagej.nih.gov/ij/images/clown.jpg"));
		final Img<UnsignedByteType> blobs = ImagePlusImgs.from(IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));

		// test cropping
		crop(img);

		// permute axes
		permuteAxes(img);

		// hyperslices
		hyperSlicing(img);

		// removing and adding dimensions
		addRemoveDimensions(img);

		// iterate a RandomAccessbileInterval and introduce flatiterable
		makeIterable(blobs);

		// illustrate outofbounds
		outOfBounds(clown);
		outOfBoundsRealType(blobs);

		// subsample image with integer steps
		subsampling(clown);

		// flip/mirror an image
		mirroring(clown);

		// stacking
		stacking(img, blobs, 10);
	}
}
