package maze_solving;

import java.util.ArrayList;
import java.util.UUID;

public class node implements Comparable<node> {
	private int depth;
	private int row;
	private int column;
	private int eval; 
	private node parent;
	public String path;
	
	public node(int r, int c, int d){
		depth = d;
		row = r;
		column = c;
		eval = 999;
		parent = null; 
		path = " ";
	}
	
	@Override
	public int compareTo(node compareNode){
		int compareEval = ((node) compareNode).getEval();
		return this.eval - compareEval;
		
	}
	
	public void setDepth(int m){
		depth =m;
	}
	public void setEval(int m){
		eval = m;
	}
	
	public void setRow(int m){
		row =m;
	}
	
	public void setColumn(int m){
		column =m;
	}
	
	public void setParent(node c_node) {
		parent = c_node;
	}
	
	public int getDepth(){
		return depth;
	}
	
	public int getEval(){
		return eval;
	}
	
	public int y(){
		return row;
	}
	
	public int x(){
		return column;
	}
	
	public node getParent() {
		return parent;
	}
	
}
