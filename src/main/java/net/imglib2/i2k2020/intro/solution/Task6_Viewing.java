package net.imglib2.i2k2020.intro.solution;

import java.io.IOException;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import ij.IJ;
import ij.ImageJ;
import net.imglib2.img.Img;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.img.imageplus.ImagePlusImgs;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.UnsignedByteType;

public class Task6_Viewing {

	/**
	 * View different images in ImageJ
	 */
	public static <T extends RealType<T> & NativeType<T>, S extends NumericType<S> & NativeType<S>> void viewingImageJ() {

		// show the ImageJ window
		new ImageJ();

		// display the image from Task1
		ImageJFunctions.show(Task1_CreateImg.createImgFromArray());

		// display blobs example (8 bit)
		final Img<T> img1 = ImagePlusImgs.from(IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));
		ImageJFunctions.show(img1);

		// display cloun example (RGB)
		final Img<S> img2 = ImagePlusImgs.from(IJ.openImage("http://imagej.nih.gov/ij/images/clown.jpg"));
		ImageJFunctions.show(img2);

		// display 3D image stack and adjust the 3rd dimension properties
		final Img<UnsignedByteType> img3 = ImagePlusImgs.from(IJ.openImage("https://preibischlab.mdc-berlin.de/download/lightsheet0.tif.zip"));
		ImageJFunctions.show(img3);
	}

	/**
	 * View different images in BigDataViewer
	 */
	public static <T extends RealType<T> & NativeType<T>> void viewingBDV() {

		// load and display first image
		final Img<T> img1 = ImagePlusImgs.from(IJ.openImage("https://preibischlab.mdc-berlin.de/download/lightsheet0.tif.zip"));
		BdvStackSource<?> bdv1 = BdvFunctions.show(img1, "stack 1");

		// load and display second image, add to same BDV instance
		final Img<T> img2 = ImagePlusImgs.from(IJ.openImage("https://preibischlab.mdc-berlin.de/download/lightsheet1.tif.zip"));
		bdv1 = BdvFunctions.show(img2, "stack 2", new BdvOptions().addTo(bdv1));

		// display blobs example (8 bit) in new BDV instance
		final Img<UnsignedByteType> img3 = ImagePlusImgs.from(IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif"));
		BdvStackSource<?> bdv2 = BdvFunctions.show(img3, "2d UnsignedByteType image", new BdvOptions().is2D());

		// display cloun example (RGB)
		final Img<ARGBType> img4 = ImagePlusImgs.from(IJ.openImage("http://imagej.nih.gov/ij/images/clown.jpg"));
		bdv2 = BdvFunctions.show(img4, "2d ARGBType image", new BdvOptions().is2D().addTo(bdv2));
	}

	public static void main(String[] args) throws IOException {

		// various images displayed with ImageJ
		viewingImageJ();

		// various images displayed with BigDataViewer
		viewingBDV();
	}
}
