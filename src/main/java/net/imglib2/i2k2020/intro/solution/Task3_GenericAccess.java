package net.imglib2.i2k2020.intro.solution;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Pair;
import net.imglib2.util.Util;
import net.imglib2.util.ValuePair;

public class Task3_GenericAccess {

	/**
	 * Using an Iterable and generic types to compute the maximal value in an
	 * image
	 * 
	 * @param iterable
	 * @return
	 */
	public static <T extends Comparable<T> & Type<T>> T max(final Iterable<T> iterable) {

		// will hold the maximal value, init with first value, important to make
		// a copy because of NativeTypes
		final T max = iterable.iterator().next().copy();

		// iterate all pixels
		for (final T pixelValue : iterable)
			if (pixelValue.compareTo(max) > 0)
				max.set(pixelValue);

		return max;
	}

	/**
	 * Using an IterableInterval and generic types to compute the maximal value
	 * in an image
	 * 
	 * @param iterable
	 * @return
	 */
	public static <T extends Comparable<T> & Type<T>> Pair<T, long[]> maxWithLocation(final IterableInterval<T> iterable) {

		// iterates the IterableInterval
		final Cursor<T> cursor = iterable.localizingCursor();

		// will hold the maximal value, init with first value, important to make
		// a copy because of NativeTypes
		final T max = cursor.next().copy();

		// the location of the max value, init with first value
		final long[] position = new long[iterable.numDimensions()];
		cursor.localize(position);

		while (cursor.hasNext()) {
			final T pixelValue = cursor.next();

			if (pixelValue.compareTo(max) > 0) {
				max.set(pixelValue);
				cursor.localize(position);
			}
		}

		return new ValuePair<>(max, position);
	}

	/**
	 * Using a RandomAccessibleInterval and generic types to return the value in
	 * the center of an image
	 * 
	 * @param rai
	 * @return
	 */
	public static <T> T centerValue(final RandomAccessibleInterval<T> rai) {

		// create a RandomAccess
		final RandomAccess<T> ra = rai.randomAccess();

		// set it to the center pixel in all dimensions
		for (int d = 0; d < rai.numDimensions(); ++d)
			ra.setPosition((rai.max(d) - rai.min(d)) / 2 + rai.min(d), d);

		return ra.get();
	}

	public static void main(String[] args) {

		// Img implements RandomAccessibleInterval and IterableInterval
		final Img<FloatType> imgF = Task1_CreateImg.createNewImg();

		// compute the maximal value
		System.out.println("\nMax value: " + max(imgF));

		// compute the maximal value and it's location
		final Pair<FloatType, long[]> max = maxWithLocation(imgF);
		System.out.println("\nMax value: " + max.getA() + " @ " + Util.printCoordinates(max.getB()));

		// output the value in the center of the image
		System.out.println("\nCenter value: " + centerValue(imgF));
	}
}
