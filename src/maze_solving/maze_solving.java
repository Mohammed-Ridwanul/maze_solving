
package maze_solving;
import java.util.*;

public class maze_solving {
	static LinkedList<node> myQueue = new LinkedList<node>();
	
	static int depth;
	static int size;		
	static boolean flag = false;
	static int gRow;
	static int gCol;
	
	//Maze is initialized to 0 by default
	static int[][] maze;
	static int[][] temp_maze;
	
	static ArrayList<node> path_array = new ArrayList<node>();
	static ArrayList<node> a_star_array = new ArrayList<node>();
	static LinkedList <node> actual_path = new LinkedList<node>();
	
	public static void main(String[] args) {		
		int i = 0;
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Size of array: ");
		size = Integer.parseInt(scanner.next());
		
		maze = new int[size][size];
		
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
		int row = Integer.parseInt(scanner.next());
		System.out.print("Enter starting column: ");
		int col = Integer.parseInt(scanner.next());
		
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
		
		node tmp = create_node(row,col,0);
		
//		Depth First Search 
//		myQueue.addFirst(tmp);
//		depthFirstSearch(tmp);
		
//		Breadth First Search
//		breadthFirstSearch(tmp);
		
//		A* Search
		a_star(tmp, 0);
		
		Long t2 = System.nanoTime() - t1;
		
		for(node n : path_array){
			System.out.println("(" + n.y() + " " + n.x() + ")" );
		}
		
		System.out.println("Found path: " + flag);
		//All others
//		System.out.println("Total cost: " + depth);
		
		//A*star
		System.out.println("Total cost: " + actual_path.size());
		
		System.out.println("Elapsed time: " + t2 + " ns");
		
	}
	
	public static int a_star(node c_node, int depth){
		
		//Found goal
		if(maze[c_node.y()][c_node.x()] == 2){
			flag = true;
			return 1;
		}
		
		gen_neighbours(c_node, c_node.getDepth());
		
		//TODO check if empty
		if(a_star_array.isEmpty()){
			flag = false;
			return 0;
		}
		
		node tmp = a_star_array.remove(0);
	
		clear_path();
		append_path(tmp);
		
		return(a_star(tmp, tmp.getDepth()));
	}
	
	private static void clear_path() {
		//Resets maze to original, allowing backtracking. 
		temp_maze = maze.clone();
		actual_path.clear();
	}

	private static void append_path(node temp) {
		//Set visited
		maze[temp.y()][temp.x()] = 9;
		if(temp.getParent() == null){
			return;
		}else{
			actual_path.add(temp);
			append_path(temp.getParent());
		}
	}

	private static boolean gen_neighbours(node c_node, int d) {
		boolean hasChild = false;
		
		//Check left
		if(c_node.x()>= 1){
			if(maze[c_node.y()][c_node.x() - 1] != 9 || maze[c_node.y()][c_node.x() - 1] != 3 ){
				node left = create_node(c_node.y(), c_node.x()-1, (d+1));
				left.setEval(eval(c_node.y(), c_node.x()-1, (d+1)));
				left.setParent(c_node);
				a_star_array.add(left);
				hasChild = true;
			}
		}
		
		//Down
		if(c_node.y() < size - 1 ){
			if(maze[c_node.y() + 1][c_node.x()] != 9 || maze[c_node.y() + 1][c_node.x()] != 3 ){
				node down = create_node(c_node.y() + 1, c_node.x(), d+1 );
				down.setEval(eval(c_node.y() + 1, c_node.x(), d+1));
				down.setParent(c_node);
				a_star_array.add(down);
				hasChild = true;
			}
		}
		
		//right
		if(c_node.x() < size-1 ){
			if(maze[c_node.y()][c_node.x() + 1] != 9 || maze[c_node.y()][c_node.x() + 1] != 3 ){
				node right = create_node(c_node.y(), c_node.x() + 1, d+1 );
				right.setEval(eval(c_node.y(), c_node.x() + 1, d+1 ));
				right.setParent(c_node);
				a_star_array.add(right);
				hasChild = true;
			}
		}
		
		//Up
		if(c_node.y() >= 1 ){
			if(maze[c_node.y() - 1][c_node.x()] != 9 || maze[c_node.y() - 1][c_node.x()] != 3 ){
				node up = create_node(c_node.y()-1, c_node.x(), d+1 );
				up.setEval(eval(c_node.y()-1, c_node.x(), d+1 ));
				up.setParent(c_node);
				a_star_array.add(up);
				hasChild = true;
			}
		}
		if(hasChild){
			Collections.sort(a_star_array);
		}
		
		return hasChild;
	}

	public static int eval(int row, int column, int depth){
		//Use Manhattan distance for heuristic 
		int heurestic = Math.abs(gRow - row) + Math.abs(gCol - column);
		return (heurestic+depth);
	}
	
	public static int breadthFirstSearch(node c_node){
		path_array.add(c_node);
		
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
				myQueue.addLast(left);
			}
		}
		
		//down
		if(c_node.y() < size - 1 ){
			if(maze[c_node.y() + 1][c_node.x()] == 0 || maze[c_node.y() + 1][c_node.x()] == 2 ){
				node down = create_node(c_node.y() + 1, c_node.x(), depth+1 );
				myQueue.addLast(down);
			}
		}
		
		//right
		if(c_node.x() < size-1 ){
			if(maze[c_node.y()][c_node.x() + 1] == 0 || maze[c_node.y()][c_node.x() + 1] == 2 ){
				node right = create_node(c_node.y(), c_node.x() + 1, depth+1 );
				myQueue.addLast(right);
			}
		}
		
		//Up
		if(c_node.y() >= 1 ){
			if(maze[c_node.y() - 1][c_node.x()] == 0 || maze[c_node.y() - 1][c_node.x()] == 2 ){
				node up = create_node(c_node.y()-1, c_node.x(), depth+1 );
				myQueue.addLast(up);
			}
		}

		if(myQueue.isEmpty()){
			return 0;
		}
		
		depth++;
		
		if(breadthFirstSearch(myQueue.removeFirst()) == 1){
			return 1;
		}else{
			depth--;
			return 0;
		}
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

