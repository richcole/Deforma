import functional.FUtils;

import java.util.stream.LongStream;

import static functional.FUtils.range;

//  -XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation -XX:+PrintInlining -XX:CompileCommand=print,*.computeCount' -XX:MaxInlineLevel=40 -XX:MaxInlineSize=400
public class TestLoop {

	public static void main(String[] args) {
		long rounds = 1000;
		long len = 100000;
		long before, after;
		Long result = 0L;

 		before = java.lang.System.currentTimeMillis();
		result = 0L;
 		for(int j=0; j<rounds; ++j) {
			for (int i = 0; i < len; ++i) {
				result += 1;
			}
		}
		after = java.lang.System.currentTimeMillis();
		System.out.println("result:" + result +
			" deltaTime: " + (after - before) +
			" ops/second:" + (len * rounds / (1e-3 * (after - before))));

		before = java.lang.System.currentTimeMillis();
		result = 0L;
		for(int i=0; i<rounds; ++i) {
			result += range(len).reduce((x, y) -> x + 1, 0L);
		}
		after = java.lang.System.currentTimeMillis();
		System.out.println("result:" + result + " deltaTime: " + (after - before) + " ops/second:" + (len * rounds / (1e-3 * (after - before))));

		before = java.lang.System.currentTimeMillis();
		result = LongStream
			.range(0, rounds)
			.map(a -> LongStream.range(0, len).reduce(0, (x1, y1) -> y1 + 1))
			.reduce(0, (x2,y2) -> x2 + y2);
		after = java.lang.System.currentTimeMillis();
		System.out.println("result:" + result + " deltaTime: " + (after - before) + " ops/second:" + (len * rounds / (1e-3 * (after - before))));


	}

	private static long computeCount(long len) {
		long result;
		result = range(len).reduce((x, y) -> x + 1, 0L);
		return result;
	}
}
