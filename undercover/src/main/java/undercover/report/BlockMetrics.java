package undercover.report;

import java.util.Collection;
import java.util.Collections;

import undercover.support.LazyValue;
import undercover.support.Proportion;

public class BlockMetrics {
	private final int complexity_;
	private final int count_;
	private final int coveredCount_;
	private final LazyComplexity complexity;
	private final LazyValue<Proportion> coverage;

	public BlockMetrics(Collection<? extends Item> children) {
		this(0, 0, 0, children);
	}
	
	public BlockMetrics(int complexity, int count, int coveredCount) {
		this(complexity, count, coveredCount, Collections.<Item>emptyList());
	}
	
	public BlockMetrics(int complexity, int count, int coveredCount, final Collection<? extends Item> children) {
		this.complexity_ = complexity;
		this.count_ = count;
		this.coveredCount_ = coveredCount;
		this.complexity = new LazyComplexity(children);
		this.coverage = new LazyValue<Proportion>() {
			@Override
			protected Proportion calculate() {
				int whole = count_;
				int part = coveredCount_;
				for (Item each : children) {
					whole += each.getBlockMetrics().getCoverage().whole;
					part += each.getBlockMetrics().getCoverage().part;
				}
				return new Proportion(part, whole);
			}
		};
	}

	public int getComplexity() {
		return complexity_ + complexity.value();
	}
	
	public Proportion getCoverage() {
		return coverage.value();
	}

	public boolean isExecutable() {
		return getCoverage().whole > 0;
	}
	
	public boolean isExecuted() {
		return getCoverage().part > 0;
	}
	
	public double getRisk() {
		return getComplexity() + (getComplexity() * (1 - getCoverage().getRatio()));
	}
}
