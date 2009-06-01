package undercover.report;

import java.util.Collection;

import undercover.support.LazyValue;

public abstract class ClassMeasure extends MethodMeasure {
	public abstract int getMethodCount();
	public abstract int getMaximumMethodComplexity();
	
	public double getAverageMethodComplexity() {
		return ((double) getComplexity()) / getMethodCount();
	}
	
	static class LazyComplexity extends LazyValue<Integer> {
		private Collection<? extends MethodMeasure> children;

		public LazyComplexity(Collection<? extends MethodMeasure> children) {
			this.children = children;
		}
		
		protected Integer calculate() {
			int result = 0;
			for (MethodMeasure each : children) {
				result += each.getComplexity();
			}
			return result;
		}
	};
	
	static class LazyBlockCount extends LazyValue<Integer> {
		private Collection<? extends MethodMeasure> children;

		public LazyBlockCount(Collection<? extends MethodMeasure> children) {
			this.children = children;
		}
		
		protected Integer calculate() {
			int result = 0;
			for (MethodMeasure each : children) {
				result += each.getBlockCount();
			}
			return result;
		}
	};
	
	static class LazyCoveredBlockCount extends LazyValue<Integer> {
		private Collection<? extends MethodMeasure> children;

		public LazyCoveredBlockCount(Collection<? extends MethodMeasure> children) {
			this.children = children;
		}
		
		protected Integer calculate() {
			int result = 0;
			for (MethodMeasure each : children) {
				result += each.getCoveredBlockCount();
			}
			return result;
		}
	};

	static class LazyMethodCount extends LazyValue<Integer> {
		private Collection<? extends MethodMeasure> children;

		public LazyMethodCount(Collection<? extends MethodMeasure> children) {
			this.children = children;
		}
		
		protected Integer calculate() {
			int result = 0;
			for (MethodMeasure each : children) {
				if (each instanceof ClassMeasure) {
					result += ((ClassMeasure) each).getMethodCount();
				} else {
					result += 1;
				}
			}
			return result;
		}
	};

	static class LazyMaximumMethodComplexity extends LazyValue<Integer> {
		private Collection<? extends MethodMeasure> children;

		public LazyMaximumMethodComplexity(Collection<? extends MethodMeasure> children) {
			this.children = children;
		}
		
		protected Integer calculate() {
			int result = 0;
			for (MethodMeasure each : children) {
				int complexity;
				if (each instanceof ClassMeasure) {
					complexity = ((ClassMeasure) each).getMaximumMethodComplexity();
				} else {
					complexity = each.getComplexity();
				}
				result = Math.max(result, complexity);
			}
			return result;
		}
	};
}
