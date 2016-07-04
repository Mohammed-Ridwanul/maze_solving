
package maze_solving;
import java.util.*;

public class maze_solving {
	static LinkedList<node> myQueue = new LinkedList<node>();
	
	static int depth;
	static int size;		
	static boolean flag = false;
	static int gRow;
	static int gCol;
	static int search_type;
	static int row;
	static int col;
	static int num_blocked;
	static int[] blocked_nodes;
	static String[] numberStrs;
	
	//Maze is initialized to 0 by default
	static int[][] maze;
	static int[][] temp_maze;
	
	//Will give all nodes visited
	static ArrayList<node> path_array = new ArrayList<node>();
	
	static LinkedList<node> search_list = new LinkedList<node>();
	
	static LinkedList <node> a_star_result_path = new LinkedList<node>();
	
	static LinkedList <node> bfs_result_path = new LinkedList<node>();
	
	static LinkedList <node> dfs_result_path = new LinkedList<node>();
	
	public static void main(String[] args) {
		
		int i = 0;
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Size of array: ");
		size = Integer.parseInt(scanner.next());
		
		maze = new int[size][size];
		temp_maze = new int[size][size];
		
		System.out.print("1 for A*, 2 for BFS, 3 for DFS: ");
		search_type = Integer.parseInt(scanner.next());
		
//		System.out.print("Enter node to block: ");
//		num_blocked = Integer.parseInt(scanner.next());
		
		Random rand = new Random();
		System.out.println("Blocked nodes: ");
		String blocks = scanner.next();
		numberStrs = blocks.split(",");
		
		for(int l = 0; l< numberStrs.length; l = l+2){
			temp_maze[Integer.parseInt(numberStrs[l])][Integer.parseInt(numberStrs[l+1])] = 3;
			System.out.println("(" + Integer.parseInt(numberStrs[l]) + " " + Integer.parseInt(numberStrs[l+1]) + ")");
		}
		
		System.out.print("Enter starting row: ");
			row = Integer.parseInt(scanner.next());
		System.out.print("Enter starting column: ");
			col = Integer.parseInt(scanner.next());
		
		maze[row][col] = 1;
		
		System.out.print("Enter goal row: ");
			gRow = Integer.parseInt(scanner.next());
		System.out.print("Enter goal column: ");
			gCol = Integer.parseInt(scanner.next());
		scanner.close();
		
		maze[gRow][gCol] = 2;
		depth = 0;
		temp_maze[gRow][gCol] = 2;
		
		Long t1 = System.nanoTime();
		Long t2 = t1;
		
		//Generate start node
		node tmp = create_node(row,col,0);
		path_array.add(tmp);
		
		switch(search_type)
		{
		case 3:
			// Depth First Search 
			depthFirstSearch(tmp);
			dfs_result_path.add(tmp);
			t2 = System.nanoTime() - t1;

			for(int i1 = dfs_result_path.size()-1; i1>=0 ; i1--){
				System.out.println("(" + dfs_result_path.get(i1).y() + " " + dfs_result_path.get(i1).x() + ")" );
			}
			
			if(flag){
				System.out.println("Found path with cost from source to goal: " + (dfs_result_path.size()-1));
			}else{
				System.out.println("Failed to find path from source to goal.");
			}
			break;
			
		case 2:
			// Breadth First Search
			breadthFirstSearch(tmp);
			bfs_result_path.add(tmp);
			t2 = System.nanoTime() - t1;
			
			for(int i1 = bfs_result_path.size()-1; i1>=0 ; i1--){
				System.out.println("(" + bfs_result_path.get(i1).y() + " " + bfs_result_path.get(i1).x() + ")" );
			}S
			
			if(flag){
				System.out.println("Found path with cost from source to goal: " + (bfs_result_path.size()-1));
			}else{
				System.out.println("Failed to find path from source to goal.");
			}
			break;
			
		case 1:
			//	A* Search
			a_star(tmp);
			t2 = System.nanoTime() - t1;
			
			for(node n : a_star_result_path){
				System.out.println("(" + n.y() + " " + n.x() + ")" );
			}
			
			if(flag){
				System.out.println("Found path with cost from source to goal: " + a_star_result_path.size());
			}else{
				System.out.println("Failed to find path from source to goal.");
			}
			break;
			
		}
		
		System.out.println("All nodes visited: ");
		for(node n : path_array){
			System.out.println("(" + n.y() + " " + n.x() + ")" );
		}
		
		System.out.println("Total nodes visited: " + path_array.size());
		System.out.println("Elapsed time: " + t2 + " ns");
	}
	
	public static int a_star(node c_node){
		
		//Found goal
		if(maze[c_node.y()][c_node.x()] == 2){
			flag = true;
			return 1;
		}
		
		gen_neighbours(c_node, c_node.getDepth());
		
		if(search_list.isEmpty()){
			flag = false;
			return 0;
		}
		
		node tmp = search_list.remove(0);
	
		clear_path(a_star_result_path);
		append_path(tmp, a_star_result_path);
		
		return(a_star(tmp));
	}
	
	private static void clear_path(LinkedList <node> x) {
		//Resets maze to original, allowing backtracking. 
		temp_maze = new int [size][size];
		
		temp_maze[row][col] = 9;
		temp_maze[gRow][gCol] =2;
		
		for(int l = 0; l< numberStrs.length; l = l+2){
			temp_maze[Integer.parseInt(numberStrs[l])][Integer.parseInt(numberStrs[l+1])] = 3;
//			System.out.println("(" + Integer.parseInt(numberStrs[l]) + " " + Integer.parseInt(numberStrs[l+1]) + ")");
		}
		
		x.clear();
	}

	private static void append_path(node temp, LinkedList <node> k) {
		//Set visited
		temp_maze[temp.y()][temp.x()] = 9;
		if(temp.getParent() == null){
			return;
		}else{
			k.add(temp);
			append_path(temp.getParent(), k);
		}
	}

	private static boolean gen_neighbours(node c_node, int d) {
		boolean hasChild = false;
		
		//Check left
		if(c_node.x()>= 1){
			if(temp_maze[c_node.y()][c_node.x() - 1] == 0 || temp_maze[c_node.y()][c_node.x() - 1] == 2 ){
				node left = create_node(c_node.y(), c_node.x()-1, (d+1));
				left.setEval(eval(c_node.y(), c_node.x()-1, (d+1)));
				left.setParent(c_node);
				if(search_type!=3){
					search_list.add(left);
				}else{
					path_array.add(left);
					clear_path(dfs_result_path);
					append_path(left, dfs_result_path);
					if(maze[left.y()][left.x()] == 2){
						flag = true;
						return flag;
					}
					if(gen_neighbours(left, left.getDepth())){
						return true;
					}else{
						clear_path(dfs_result_path);
					}
				}
			}
		}
		
		//Down
		if(c_node.y() < size - 1 ){
			if(temp_maze[c_node.y() + 1][c_node.x()] == 0 || temp_maze[c_node.y() + 1][c_node.x()] == 2 ){
				node down = create_node(c_node.y() + 1, c_node.x(), d+1 );
				down.setEval(eval(c_node.y() + 1, c_node.x(), d+1));
				down.setParent(c_node);
				if(search_type!=3){
					search_list.add(down);
				}else{
					path_array.add(down);
					clear_path(dfs_result_path);
					append_path(down, dfs_result_path);
					if(maze[down.y()][down.x()] == 2){
						flag = true;
						return flag;
					}
					if(gen_neighbours(down, down.getDepth())){
						return true;
					}else{
						clear_path(dfs_result_path);
					}
				}
			}
		}
		
		//right
		if(c_node.x() < size-1 ){
			if(temp_maze[c_node.y()][c_node.x() + 1] == 0 || temp_maze[c_node.y()][c_node.x() + 1] == 2 ){
				node right = create_node(c_node.y(), c_node.x() + 1, d+1 );
				right.setEval(eval(c_node.y(), c_node.x() + 1, d+1 ));
				right.setParent(c_node);
				if(search_type!=3){
					search_list.add(right);
				}else{
					path_array.add(right);
					clear_path(dfs_result_path);
					append_path(right, dfs_result_path);
					if(maze[right.y()][right.x()] == 2){
						flag = true;
						return flag;
					}
					if(gen_neighbours(right, right.getDepth())){
						return true;
					}else{
						clear_path(dfs_result_path);
					}
				}
			}
		}
		
		//Up
		if(c_node.y() >= 1 ){
			if(temp_maze[c_node.y() - 1][c_node.x()] == 0 || temp_maze[c_node.y() - 1][c_node.x()] == 2 ){
				node up = create_node(c_node.y()-1, c_node.x(), d+1 );
				up.setEval(eval(c_node.y()-1, c_node.x(), d+1 ));
				up.setParent(c_node);
				if(search_type!=3){
					search_list.add(up);
				}else{
					path_array.add(up);
					clear_path(dfs_result_path);
					append_path(up, dfs_result_path);
					if(maze[up.y()][up.x()] == 2){
						flag = true;
						return flag;
					}
					if(gen_neighbours(up, up.getDepth())){
						return true;
					}else{
						clear_path(dfs_result_path);
					}
				}
			}
		}
		
		if(hasChild && search_type == 1){
			Collections.sort(search_list);
		}
		
		return hasChild;
	}

	public static int eval(int row, int column, int depth){
		//Use Manhattan distance for heuristic 
		int heurestic = Math.abs(gRow - row) + Math.abs(gCol - column);
		return (heurestic+depth);
	}
	
	public static int breadthFirstSearch(node c_node){
		
		//Found goal
		if(maze[c_node.y()][c_node.x()] == 2){
			append_path(c_node, bfs_result_path);
			flag = true;
			return 1;
		}
		
		append_path(c_node, bfs_result_path);
		
		gen_neighbours(c_node, c_node.getDepth());
		
		if(search_list.isEmpty()){
			flag = false;
			return 0;
		}
		
		path_array.add(search_list.get(0));
		node tmp = search_list.remove(0);
	

		clear_path(bfs_result_path);
		
		
		return(breadthFirstSearch(tmp));

	}

	public static void depthFirstSearch(node c_node) {
		
		gen_neighbours(c_node, c_node.getDepth());

		return;
	}

	public static node create_node(int row, int col, int depth) {
		node newNode = new node(row, col, depth);
		return newNode;
	}


}

