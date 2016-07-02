
package maze_solving;
import java.util.*;

public class maze_solving {
	static LinkedList<node> myQueue = new LinkedList<node>();
	static int depth;
	static int size;		
	static boolean flag = false;
	
	//Maze is initialized to 0 by default
	static int[][] maze;
	static ArrayList<node> path_array = new ArrayList<node>();
	
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
		int gRow = Integer.parseInt(scanner.next());
		System.out.print("Enter goal column: ");
		int gCol = Integer.parseInt(scanner.next());
		scanner.close();
		
		maze[gRow][gCol] = 2;
		depth = 0;
		
		Long t1 = System.nanoTime();
		
		node tmp = create_node(row,col,0);
		
		/* 												Depth First Search 
		myQueue.addFirst(tmp);
		depthFirstSearch(tmp);
		 												Depth First Search End 
		*/
		
		/* Breadth First Search */
//		myQueue.addLast(tmp);
		breadthFirstSearch(tmp);
		
		Long t2 = System.nanoTime() - t1;
		
		for(node n : path_array){
			System.out.println("(" + n.getRow() + " " + n.getColumn() + ")" );
		}
		
		System.out.println("Found path: " + flag);
		System.out.println("Total cost: " + depth);
		
		System.out.println("Elapsed time: " + t2 + " ns");
		
	}
	
	public static int breadthFirstSearch(node c_node){
		path_array.add(c_node);
		
		//Found goal
		if(maze[c_node.getRow()][c_node.getColumn()] == 2){
			flag = true;
			return 1;
		}
		
		//Set visited
		maze[c_node.getRow()][c_node.getColumn()] = 9; 
		

		//Left
		if(c_node.getColumn()>= 1){
			if(maze[c_node.getRow()][c_node.getColumn() - 1] == 0 || maze[c_node.getRow()][c_node.getColumn() - 1] == 2 ){
				node left = create_node(c_node.getRow(), c_node.getColumn()-1, depth+1 );
				myQueue.addLast(left);
			}
		}
		
		//down
		if(c_node.getRow() < size - 1 ){
			if(maze[c_node.getRow() + 1][c_node.getColumn()] == 0 || maze[c_node.getRow() + 1][c_node.getColumn()] == 2 ){
				node down = create_node(c_node.getRow() + 1, c_node.getColumn(), depth+1 );
				myQueue.addLast(down);
			}
		}
		
		//right
		if(c_node.getColumn() < size-1 ){
			if(maze[c_node.getRow()][c_node.getColumn() + 1] == 0 || maze[c_node.getRow()][c_node.getColumn() + 1] == 2 ){
				node right = create_node(c_node.getRow(), c_node.getColumn() + 1, depth+1 );
				myQueue.addLast(right);
			}
		}
		
		//Up
		if(c_node.getRow() >= 1 ){
			if(maze[c_node.getRow() - 1][c_node.getColumn()] == 0 || maze[c_node.getRow() - 1][c_node.getColumn()] == 2 ){
				node up = create_node(c_node.getRow()-1, c_node.getColumn(), depth+1 );
				myQueue.addLast(up);
			}
		}
		if(myQueue.isEmpty()){
			return 0;
		}
		
		if(breadthFirstSearch(myQueue.removeFirst()) == 1){
			return 1;
		}else{
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
		if(maze[c_node.getRow()][c_node.getColumn()] == 2){
			flag = true;
			return 1;
		}
		
		//Set visited
		maze[c_node.getRow()][c_node.getColumn()] = 9; 
		

		//Left
		if(c_node.getColumn()>= 1){
			if(maze[c_node.getRow()][c_node.getColumn() - 1] == 0 || maze[c_node.getRow()][c_node.getColumn() - 1] == 2 ){
				node left = create_node(c_node.getRow(), c_node.getColumn()-1, depth+1 );
				myQueue.addFirst(left);
//				depth++;
				if(depthFirstSearch(left)==1){
					return 1;
				};				
			}
		}
		
		//down
		if(c_node.getRow() < size - 1 ){
			if(maze[c_node.getRow() + 1][c_node.getColumn()] == 0 || maze[c_node.getRow() + 1][c_node.getColumn()] == 2 ){
				node down = create_node(c_node.getRow() + 1, c_node.getColumn(), depth+1 );
				myQueue.addFirst(down);
//				depth++;
				if(depthFirstSearch(down)==1){
					return 1;
				};
			}
		}
		
		//right
		if(c_node.getColumn() < size-1 ){
			if(maze[c_node.getRow()][c_node.getColumn() + 1] == 0 || maze[c_node.getRow()][c_node.getColumn() + 1] == 2 ){
				node right = create_node(c_node.getRow(), c_node.getColumn() + 1, depth+1 );
				myQueue.addFirst(right);
//				depth++;
				if(depthFirstSearch(right) ==1){
					return 1;
				}; 
			}
		}
		
		//Up
		if(c_node.getRow() >= 1 ){
			if(maze[c_node.getRow() - 1][c_node.getColumn()] == 0 || maze[c_node.getRow() - 1][c_node.getColumn()] == 2 ){
				node up = create_node(c_node.getRow()-1, c_node.getColumn(), depth+1 );
				myQueue.addFirst(up);
//				depth++;
				if(depthFirstSearch(up) == 1){
					return 1;
				};
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

