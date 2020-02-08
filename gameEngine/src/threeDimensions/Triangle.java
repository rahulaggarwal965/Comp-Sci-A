package threeDimensions;

public class Triangle {

	public Vertex v0, v1, v2;
	public int color;
	
	public Triangle(Vertex v0, Vertex v1, Vertex v2, int color) {
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.color = color;
	}
	
	public Triangle(Vertex v0, Vertex v1, Vertex v2) {
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
	}

}
