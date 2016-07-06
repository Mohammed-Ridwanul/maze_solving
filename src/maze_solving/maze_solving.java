
package maze_solving;
import java.util.*;

public class maze_solving {
	
	public static void main(String[] args) {
		dfs d = new dfs();
		bfs b = new bfs();
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Which one? DFS =1, BFS = 2 ");
		int search_type = Integer.parseInt(scanner.next());
		if(search_type == 1){
			d.dfs_main();
		} else if(search_type ==2){
			b.bfs_main();
		}
	}
}
