package net.imglib2.i2k2020.intro.tasks;

import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

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

		// generate a random number generator

		// iterate all pixels of that image and set it to a random number

		// print out all pixel values together with their position using a
		// Cursor

		// random access to a central pixel

		return null;
	}

	/**
	 * Create an image from an existing float[] array and print out their
	 * locations and values.
	 * 
	 * @return
	 */
	public static Img<FloatType> createImgFromArray() {

		return null;
	}

	public static void main(String[] args) {

		// create a new image, iterate and perform random access
		createNewImg();

		// create a new image from an existing float[] array
		createImgFromArray();
	}
}
