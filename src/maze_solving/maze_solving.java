
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
	
	//Maze is initialized to 0 by default
	static int[][] maze;
	static int[][] temp_maze;
	
	//Will give all nodes visited
	static ArrayList<node> path_array = new ArrayList<node>();
	
	static ArrayList<node> search_list = new ArrayList<node>();
	
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
		int search_type = Integer.parseInt(scanner.next());
		
		System.out.print("How many nodes to block: ");
		String num_blocked = scanner.next();
		
		Random rand = new Random();
		
		System.out.println("Blocked nodes: ");
		while( i < Integer.parseInt(num_blocked)){
			//3 Stands for blocked, 1 for start node, 2 for goal node
			//0 for not visited, maze is initialized to 0 by default.
			// 9 for visited
			int j = rand.nextInt(size);
			int k = rand.nextInt(size);
			maze[j][k] = 3;
			System.out.println("(" + j + " " + k + ")");
			i++;
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
		temp_maze = maze.clone();
		
		Long t1 = System.nanoTime();
		Long t2 = t1;
		
		//Generate start node
		node tmp = create_node(row,col,0);
		path_array.add(tmp);
		
		switch(search_type)
		{
		case 3:
			// Depth First Search 
			myQueue.addFirst(tmp);
			depthFirstSearch(tmp);
			t2 = System.nanoTime() - t1;

			for(node n : dfs_result_path){
				
			}
			
			if(flag){
				System.out.println("Found path with cost from source to goal: " + dfs_result_path.size());
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
			}
			
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
				search_list.add(left);
				hasChild = true;
			}
		}
		
		//Down
		if(c_node.y() < size - 1 ){
			if(temp_maze[c_node.y() + 1][c_node.x()] == 0 || temp_maze[c_node.y() + 1][c_node.x()] == 2 ){
				node down = create_node(c_node.y() + 1, c_node.x(), d+1 );
				down.setEval(eval(c_node.y() + 1, c_node.x(), d+1));
				down.setParent(c_node);
				search_list.add(down);
				hasChild = true;
			}
		}
		
		//right
		if(c_node.x() < size-1 ){
			if(temp_maze[c_node.y()][c_node.x() + 1] == 0 || temp_maze[c_node.y()][c_node.x() + 1] == 2 ){
				node right = create_node(c_node.y(), c_node.x() + 1, d+1 );
				right.setEval(eval(c_node.y(), c_node.x() + 1, d+1 ));
				right.setParent(c_node);
				search_list.add(right);
				hasChild = true;
			}
		}
		
		//Up
		if(c_node.y() >= 1 ){
			if(temp_maze[c_node.y() - 1][c_node.x()] == 0 || temp_maze[c_node.y() - 1][c_node.x()] == 2 ){
				node up = create_node(c_node.y()-1, c_node.x(), d+1 );
				up.setEval(eval(c_node.y()-1, c_node.x(), d+1 ));
				up.setParent(c_node);
				search_list.add(up);
				hasChild = true;
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

	public static int depthFirstSearch(node c_node) {
		depth++;
		
		if(myQueue.isEmpty()){
			depth--;
			return 0;
		}
		
		path_array.add(c_node);
		myQueue.removeFirst();
		
		//Found goal
		if(maze[c_node.y()][c_node.x()] == 2){
			flag = true;
			return 1;
		}
		
		//Set visited
		maze[c_node.y()][c_node.x()] = 9; 
		

		//Left
		if(c_node.x()>= 1){
			if(maze[c_node.y()][c_node.x() - 1] == 0 || maze[c_node.y()][c_node.x() - 1] == 2 ){
				node left = create_node(c_node.y(), c_node.x()-1, depth+1 );
				myQueue.addFirst(left);
//				depth++;
				if(depthFirstSearch(left)==1){
					return 1;
				};	
				System.out.println(path_array.get(path_array.size()-1));
				path_array.remove(path_array.size()-1);
				System.out.println(path_array.get(path_array.size()));
			}
		}
		
		//down
		if(c_node.y() < size - 1 ){
			if(maze[c_node.y() + 1][c_node.x()] == 0 || maze[c_node.y() + 1][c_node.x()] == 2 ){
				node down = create_node(c_node.y() + 1, c_node.x(), depth+1 );
				myQueue.addFirst(down);
//				depth++;
				if(depthFirstSearch(down)==1){
					return 1;
				};
				System.out.println(path_array.get(path_array.size()-1));
				path_array.remove(path_array.size()-1);
			}
		}
		
		//right
		if(c_node.x() < size-1 ){
			if(maze[c_node.y()][c_node.x() + 1] == 0 || maze[c_node.y()][c_node.x() + 1] == 2 ){
				node right = create_node(c_node.y(), c_node.x() + 1, depth+1 );
				myQueue.addFirst(right);
//				depth++;
				if(depthFirstSearch(right) ==1){
					return 1;
				}; 
				System.out.println(path_array.get(path_array.size()-1));
				path_array.remove(path_array.size()-1);
			}
		}
		
		//Up
		if(c_node.y() >= 1 ){
			if(maze[c_node.y() - 1][c_node.x()] == 0 || maze[c_node.y() - 1][c_node.x()] == 2 ){
				node up = create_node(c_node.y()-1, c_node.x(), depth+1 );
				myQueue.addFirst(up);
//				depth++;
				if(depthFirstSearch(up) == 1){
					return 1;
				};
				System.out.println(path_array.get(path_array.size()-1));
				path_array.remove(path_array.size()-1);
			}
		}
		
		depth--;

		return 0;
	}

	public static node create_node(int row, int col, int depth) {
		node newNode = new node(row, col, depth);
		return newNode;
	}


}

