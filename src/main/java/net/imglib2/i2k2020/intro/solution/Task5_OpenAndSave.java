package net.imglib2.i2k2020.intro.solution;

import java.io.IOException;

import ij.IJ;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class Task5_OpenAndSave {

	/**
	 * Open an existing file as NumericType
	 */
	public static <T extends NumericType<T> & NativeType<T>> void testNumericTypeOpening() {

		// RGB image
		final String imgFN = "http://imagej.nih.gov/ij/images/clown.jpg";

		// open as NumericType
		final Img<T> img = ImagePlusImgs.from(IJ.openImage(imgFN));

		System.out.println("Type=" + img.firstElement().getClass().getName());
	}

	/**
	 * Open an existing file as RealType
	 */
	public static <T extends RealType<T> & NativeType<T>> void testRealTypeOpening() {

		// 8-bit unsigned image
		final String imgFN = "http://imagej.nih.gov/ij/images/blobs.gif";

		// open as RealType
		final Img<T> img = ImagePlusImgs.from(IJ.openImage(imgFN));

		System.out.println("Type=" + img.firstElement().getClass().getName());
	}

	/**
	 * Open an existing file as UnsignedByteType
	 */
	public static void testUnsignedByteTypeOpening() {

		// 8-bit unsigned image
		final String imgFN = "http://imagej.nih.gov/ij/images/blobs.gif";

		// open as RealType
		final Img<UnsignedByteType> img = ImagePlusImgs.from(IJ.openImage(imgFN));

		System.out.println("Type=" + img.firstElement().getClass().getName());
	}

	/**
	 * Fail to open an existing RGB image as RealType
	 */
	public static <T extends RealType<T> & NativeType<T>> void failToOpen() {

		// RGB image
		final String imgFN = "http://imagej.nih.gov/ij/images/clown.jpg";

		// open as RealType
		final Img<T> img = ImagePlusImgs.from(IJ.openImage(imgFN));

		System.out.println("Type=" + img.firstElement().getClass().getName());
	}

	public static <T extends RealType<T> & NativeType<T>> void main(String[] args) throws IOException {

		// open generic images using ImageJ
		testNumericTypeOpening();
		testRealTypeOpening();

		// open specifically types images using ImageJ
		testUnsignedByteTypeOpening();

		// example of a crash when demanding a type for opening that the image
		// does not have
		// failToOpen();

		// save an image using ImageJ
		IJ.save(ImageJFunctions.wrap(Task1_CreateImg.createImgFromArray(), "test"), "test.tif");
		IJ.save(ImageJFunctions.wrap(Task1_CreateImg.createImgFromArray(), "test"), "test.jpg");
	}
}
