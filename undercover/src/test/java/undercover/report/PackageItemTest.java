package undercover.report;

import static org.junit.Assert.*;

import org.junit.Test;

public class PackageItemTest {
	@Test public void normalPackage() {
		PackageItem packageItem = new PackageItem("p/p");
		assertEquals("p/p", packageItem.getName());
		assertEquals("p.p", packageItem.getDisplayName());
	}
	
	@Test public void composition() {
		PackageItem packageItem = new PackageItem("p");
		
		packageItem.addClass(new DummyClassItem("p/c0", 1, 1, 1));
		assertEquals(1, packageItem.getBlockCount());
		assertEquals(1, packageItem.getCoveredBlockCount());
		assertEquals(1, packageItem.getMethodCount());
		
		packageItem.addClass(new DummyClassItem("p/c1", 0, 0, 1));
		assertEquals(1, packageItem.getBlockCount());
		assertEquals(1, packageItem.getCoveredBlockCount());
		assertEquals(2, packageItem.getMethodCount());

		packageItem.addClass(new DummyClassItem("p/c2", 2, 1, 1));
		assertEquals(3, packageItem.getBlockCount());
		assertEquals(2, packageItem.getCoveredBlockCount());
		assertEquals(3, packageItem.getMethodCount());
	}
	
	static class DummyClassItem extends ClassItem {
		private int blockCount;
		private int coveredBlockCount;
		private int methodCount;

		public DummyClassItem(String name, int blockCount, int coveredBlockCount, int methodCount) {
			super(null, name);
			this.blockCount = blockCount;
			this.coveredBlockCount = coveredBlockCount;
			this.methodCount = methodCount;
		}
		
		public int getBlockCount() { return blockCount; }
		public int getCoveredBlockCount() { return coveredBlockCount; }
		public int getMethodCount() { return methodCount; }
	}
}
