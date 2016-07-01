package maze_solving;

public class node {
	private int depth;
	private int row;
	private int column;
	private boolean visited;
	
	public node(int r, int c, int d){
		depth = d;
		row = r;
		column = c;
		visited = false;
	}
	
	
	public void setDepth(int m){
		depth =m;
	}
	
	public void setRow(int m){
		row =m;
	}
	
	public void setColumn(int m){
		column =m;
	}
	
	public void setVisited(boolean v){
		visited = v;
	}
	
	public int getDepth(){
		return depth;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public boolean getVisited(){
		return visited;
	}
	

}
