package undercover.report;

import java.util.Collection;

public abstract class BlockMetrics {
	public abstract int getComplexity();

	public abstract int getBlockCount();

	public abstract int getCoveredBlockCount();
	
	public boolean isExecutable() {
		return getBlockCount() > 0;
	}
	
	public double getCoverageRate() {
		return getBlockCount() == 0 ? 1 : ((double) getCoveredBlockCount()) / getBlockCount();
	}
	
	public double getRisk() {
		return getComplexity() + (getComplexity() * (1 - getCoverageRate()));
	}
	
	public static class Leaf extends BlockMetrics {
		private final int complexity;
		private final int blockCount;
		private final int coveredBlockCount;

		public Leaf(int complexity, int blockCount, int coveredBlockCount) {
			this.complexity = complexity;
			this.blockCount = blockCount;
			this.coveredBlockCount = coveredBlockCount;
		}

		public int getComplexity() {
			return complexity;
		}

		public int getBlockCount() {
			return blockCount;
		}

		public int getCoveredBlockCount() {
			return coveredBlockCount;
		}
	}
	
	public static class Composite extends BlockMetrics {
		private LazyComplexity complexity;
		private LazyBlockCount blockCount;
		private LazyCoveredBlockCount coveredBlockCount;

		public Composite(Collection<? extends Item> children) {
			complexity = new LazyComplexity(children);
			blockCount = new LazyBlockCount(children);
			coveredBlockCount = new LazyCoveredBlockCount(children);
		}

		public int getComplexity() {
			return complexity.value();
		}
		
		public int getBlockCount() {
			return blockCount.value();
		}

		public int getCoveredBlockCount() {
			return coveredBlockCount.value();
		}
	}
}
