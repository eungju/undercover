package undercover.report;

import java.util.Collection;
import java.util.Collections;

public class BlockMetrics {
	private final int complexity_;
	private final int blockCount_;
	private final int coveredBlockCount_;
	private final LazyComplexity complexity;
	private final LazyBlockCount blockCount;
	private final LazyCoveredBlockCount coveredBlockCount;

	public BlockMetrics(Collection<? extends Item> children) {
		this(0, 0, 0, children);
	}
	
	public BlockMetrics(int complexity, int blockCount, int coveredBlockCount) {
		this(complexity, blockCount, coveredBlockCount, Collections.<Item>emptyList());
	}
	
	public BlockMetrics(int complexity, int blockCount, int coveredBlockCount, Collection<? extends Item> children) {
		this.complexity_ = complexity;
		this.blockCount_ = blockCount;
		this.coveredBlockCount_ = coveredBlockCount;
		this.complexity = new LazyComplexity(children);
		this.blockCount = new LazyBlockCount(children);
		this.coveredBlockCount = new LazyCoveredBlockCount(children);
	}

	public int getComplexity() {
		return complexity_ + complexity.value();
	}

	public int getBlockCount() {
		return blockCount_ + blockCount.value();
	}

	public int getCoveredBlockCount() {
		return coveredBlockCount_ + coveredBlockCount.value();
	}
	
	public boolean isExecutable() {
		return getBlockCount() > 0;
	}
	
	public double getCoverageRate() {
		return getBlockCount() == 0 ? 1 : ((double) getCoveredBlockCount()) / getBlockCount();
	}
	
	public double getRisk() {
		return getComplexity() + (getComplexity() * (1 - getCoverageRate()));
	}
}
