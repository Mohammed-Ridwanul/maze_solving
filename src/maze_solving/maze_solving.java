
package maze_solving;
import java.util.*;

public class maze_solving {
	static Queue<node> myQueue = new LinkedList<node>();
	static int depth;
	
	//Maze is initialized to 0 by default
	static int[][] maze = new int[25][25];
	static ArrayList<node> path_array = new ArrayList<node>();
	
	public static void main(String[] args) {
		int i = 0;
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("How many nodes to block: ");
		String num_blocked = scanner.next();
		
		Random rand = new Random();
		
		System.out.println("Blocked nodes: ");
		while( i < Integer.parseInt(num_blocked)){
			//3 Stands for blocked, 1 for start node, 2 for goal node
			//0 for not visited, maze is initialized to 0 by default.
			// 9 for visited
			int j = rand.nextInt(25);
			int k = rand.nextInt(25);
			maze[j][k] = 3;
			System.out.println("(" + j + " " + k + ")");
			i++;
		}
		
		System.out.println("Enter starting row: ");
		int row = Integer.parseInt(scanner.next());
		System.out.println("Enter starting column: ");
		int col = Integer.parseInt(scanner.next());
		
		maze[row][col] = 1;
		
		System.out.println("Enter goal row: ");
		int gRow = Integer.parseInt(scanner.next());
		System.out.println("Enter goal column: ");
		int gCol = Integer.parseInt(scanner.next());
		scanner.close();
		
		maze[gRow][gCol] = 2;
		depth = 0;
		
		beef(create_node(row,col,0));
		
	}

	public static void beef(node c_node) {
		boolean flag = false;
		
		
		path_array.add(c_node);
		
		//Found goal
		if(maze[c_node.getRow()][c_node.getColumn()] == 2){
			return;
		}
		
		//Set visited
		maze[c_node.getRow()][c_node.getColumn()] = 9; 
		

		//Left
		if(c_node.getRow()>= 1){
			if(maze[c_node.getRow()][c_node.getColumn() - 1] == 0 ){
				node left = create_node(c_node.getRow(), c_node.getColumn()-1, depth+1 );
				myQueue.add(left);
				flag = true; 
			}
		}
		
		//down
		if(c_node.getColumn() <= 23 ){
			if(maze[c_node.getRow() + 1][c_node.getColumn()] == 0 ){
				node down = create_node(c_node.getRow() + 1, c_node.getColumn(), depth+1 );
				myQueue.add(down);
				flag = true; 
			}
		}
		
		//right
		if(c_node.getRow() <= 23 ){
			if(maze[c_node.getRow()][c_node.getColumn() + 1] == 0 ){
				node right = create_node(c_node.getRow(), c_node.getColumn() + 1, depth+1 );
				myQueue.add(right);
				flag = true; 
			}
		}
		
		//Up
		if(c_node.getColumn() >= 1 ){
			if(maze[c_node.getRow() - 1][c_node.getColumn()] == 0 ){
				node up = create_node(c_node.getRow()-1, c_node.getColumn(), depth+1 );
				myQueue.add(up);
				flag = true; 
			}
		}
		
		if(myQueue.isEmpty()){
			return;
		}else{
			
			node tmp = myQueue.remove();
			if(!flag){
				int d = myQueue.peek().getDepth();
				//pop last until depth 
				for(int i=path_array.size() - 1; i<d; i--){
					path_array.remove(i);
				}
			}
			
			beef(tmp);
		}
	}

	public static node create_node(int row, int col, int depth) {
		node newNode = new node(row, col, depth);
		return newNode;
	}


}

