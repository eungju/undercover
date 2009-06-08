package undercover.data;

public interface MetaDataVisitor {
	void visitEnter(MetaData metaData);
	void visitLeave(MetaData metaData);
	
	void visitEnter(ClassMeta classMeta);
	void visitLeave(ClassMeta classLeave);

	void visitEnter(MethodMeta methodMeta);
	void visitLeave(MethodMeta methodLeave);
	
	void visit(BlockMeta blockMeta);
}
