public class FlowVisitor<String> implements Visitor<String> {
	@Override
	public void visit(String obj) {
		System.out.println(obj);
	}
}