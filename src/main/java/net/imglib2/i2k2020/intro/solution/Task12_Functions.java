package net.imglib2.i2k2020.intro.solution;

import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import net.imglib2.position.FunctionRandomAccessible;
import net.imglib2.position.FunctionRealRandomAccessible;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedLongType;
import net.imglib2.util.Intervals;

public class Task12_Functions {

	/**
	 * Define the Julia fractal, which can return a value for any real
	 * coordinate and display with BigDataViewer
	 */
	public static void fractal() {

		double a = 0.2, b = 0.8;
		int n = 1000;
		FunctionRealRandomAccessible<UnsignedLongType> julia = new FunctionRealRandomAccessible<>(
				2,
				(x, y) -> {
					long i = 0;
					double v = 0,
							c = x.getDoublePosition(0),
							d = x.getDoublePosition(1);
					while (i < n && v < 4096) {
						double br = c * c - d * d;
						d = 2 * c * d;
						c = br + a;
						d += b;
						v = Math.sqrt(c * c + d * d);
						++i;
					}
					y.set(i);
				}, UnsignedLongType::new);

		BdvFunctions.show(julia, Intervals.createMinMax(-1, -1, 1, 1), "", BdvOptions.options().is2D()).setDisplayRange(0, 32);
	}

	/**
	 * Define a function on integer coordinates that makes a funny pattern and
	 * display with BigDataViewer
	 */
	public static void funnyCheckerBoard() {

		FunctionRandomAccessible<UnsignedByteType> checkerboard = new FunctionRandomAccessible<>(
				2,
				(location, value) -> {
					value.setInteger(
							Math.abs(location.getIntPosition(0)) % 3 +
									(-Math.abs(location.getIntPosition(1))) % 3);
				},
				UnsignedByteType::new);

		BdvFunctions.show(checkerboard, Intervals.createMinMax(-10, -10, 10, 10), "", BdvOptions.options().is2D()).setDisplayRange(0, 1);
	}

	public static void main(String[] args) {

		// function
		funnyCheckerBoard();

		// real function
		fractal();
	}
}
