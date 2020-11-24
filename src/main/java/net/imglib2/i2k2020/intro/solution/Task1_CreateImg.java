package net.imglib2.i2k2020.intro.solution;

import java.util.Random;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

/**
 * Illustrates how to use an array in ImgLib2, how to use iteration, random
 * access
 * 
 * @author Stephan Preibisch
 *
 */
public class Task1_CreateImg {

	/**
	 * Use ArrayImg to create a 5x5 float image and fill with random numbers,
	 * then print out its values and locations. Finally set the center pixel to
	 * 1000 and print out again.
	 *
	 * @return
	 */
	public static Img<FloatType> createNewImg() {

		// create a 5x5 pixel image of type float
		final Img<FloatType> img = ArrayImgs.floats(5, 5);

		// generate a random number generator
		Random rnd = new Random();

		// iterate all pixels of that image and set it to a random number
		for (final FloatType pixelValue : img)
			pixelValue.set(rnd.nextFloat());

		// print out all pixel values together with their position using a
		// Cursor
		final Cursor<FloatType> cursor = img.localizingCursor();

		System.out.println("random values: ");

		while (cursor.hasNext())
			System.out.println("value=" + cursor.next().get() + " @ " + Util.printCoordinates(cursor));

		// random access to a central pixel
		final RandomAccess<FloatType> ra = img.randomAccess();

		ra.setPosition(new long[]{2, 2});
		ra.get().set(1000.0f);

		System.out.println("\ncenter value reset: ");

		cursor.reset();

		while (cursor.hasNext())
			System.out.println("value=" + cursor.next().get() + " @ " + Util.printCoordinates(cursor));

		return img;
	}

	/**
	 * Create an image from an existing float[] array and print out their
	 * locations and values.
	 * 
	 * @return
	 */
	public static Img<FloatType> createImgFromArray() {

		final float[] array = new float[5 * 5];

		for (int i = 0; i < array.length; ++i)
			array[i] = i;

		final Img<FloatType> img = ArrayImgs.floats(array, 5, 5);

		// print out all pixel values together with their position using a
		// Cursor
		final Cursor<FloatType> cursor = img.cursor();

		System.out.println("\nincreasing numbers: ");

		while (cursor.hasNext())
			System.out.println("value=" + cursor.next().get() + " @ " + Util.printCoordinates(cursor));

		return img;
	}

	public static void main(String[] args) {

		// create a new image, iterate and perform random access
		createNewImg();

		// create a new image from an existing float[] array
		createImgFromArray();
	}
}
