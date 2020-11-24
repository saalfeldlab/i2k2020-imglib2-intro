package net.imglib2.i2k2020.intro.tasks;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.planar.PlanarImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Util;

public class Task4_LargerImages {

	/**
	 * compare how a CellImg and ArrayImg are iterated, make two 5x5 images and
	 * test
	 */
	public static void testIteration() {

		Img<UnsignedByteType> imgA = null, imgB = null;

		//TODO: fill

		testIteration(imgA, imgB);
	}

	/**
	 * Print out the iteration order for two types of images
	 * 
	 * @param imgA
	 * @param imgB
	 */
	public static void testIteration(final IterableInterval<?> imgA, final IterableInterval<?> imgB) {

		final Cursor<?> cursorA = imgA.localizingCursor();
		final Cursor<?> cursorB = imgB.localizingCursor();

		while (cursorA.hasNext() && cursorB.hasNext()) {
			cursorA.fwd();
			cursorB.fwd();

			System.out.println(Util.printCoordinates(cursorA) + " <> " + Util.printCoordinates(cursorB) + " equal? " + Util.locationsEqual(cursorA, cursorB));
		}
	}

	/**
	 * try to instantiate images of different sizes with ArrayImg, PlanarImg and
	 * CellImg
	 * 
	 * @param dim
	 *            - the size of image
	 */
	public static void instantiateImgs(final long[] dim) {

		System.out.println("\n--- Instantiating different Img types for dim: " + Util.printCoordinates(dim) + ":");

		// ArrayImg
		try {
			final Img<UnsignedByteType> arrayImg = ArrayImgs.unsignedBytes(dim);
			System.out.println("  ArrayImg instantiated successfully: " + arrayImg);
		} catch (RuntimeException e) {
			System.out.println("  ArrayImg failed to be instantiated: " + e);
		}

		// PlanarImg
		try {
			final Img<UnsignedByteType> planarImg = PlanarImgs.unsignedBytes(dim);
			System.out.println("  PlanarImg instantiated successfully: " + planarImg);
		} catch (RuntimeException e) {
			System.out.println("  PlanarImg failed to be instantiated: " + e);
		}

		// CellImg has no convenience methods. Using the factory, which also
		// allows generic instantiations
		try {
			final ImgFactory<UnsignedByteType> cellImgFactory = new CellImgFactory<>(new UnsignedByteType(), 100);
			final Img<UnsignedByteType> cellImg = cellImgFactory.create(dim);
			System.out.println("  CellImg instantiated successfully: " + cellImg);
		} catch (RuntimeException e) {
			System.out.println("  CellImg failed to be instantiated: " + e);
		}
	}

	public static void main(String[] args) {

		// visualize different iterations on arrayimgs and cellimgs
		testIteration();

		// will work with all img types
		instantiateImgs(new long[]{2048, 2048, 50});

		// will fail on ArrayImg because the total number of pixels is bigger
		// than 2^31 (2147483647) which is around 2GB for 8bit
		instantiateImgs(new long[]{2048, 2048, 550});

		// will fail on ArrayImg and PlanarImg because the total number of
		// pixels per plane is bigger than 2^31 (2147483647)
		// note: ImageJ cannot handle this image
		instantiateImgs(new long[]{47000, 47000});
	}
}
