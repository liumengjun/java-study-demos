package algorism.graph;

public enum GraphKind {
	DIRECTED, UNDIRECTED;
	public boolean is2Way(){
		return this.equals(UNDIRECTED);
	}
}
