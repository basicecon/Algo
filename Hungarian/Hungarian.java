/*
Hungarian Algorithm
LANG: JAVA
BFS and DFS
*/

/*
online source:
	http://fuliang.iteye.com/blog/372845
	http://www.cs.princeton.edu/~wayne/kleinberg-tardos/pdf/07NetworkFlowII.pdf
	http://www.cs.princeton.edu/~wayne/kleinberg-tardos/pdf/07NetworkFlowI.pdf
	https://www.byvoid.com/blog/hungary
*/

import java.util.*;
import java.io.*;

class Hungarian {
	static int MAX = 102;
	static int x_set;
	static int y_set;
	static int num_edges;
	static int[][] adj_list = new int[MAX][MAX];
	static int[] match = new int[MAX];
	static boolean[] used = new boolean[MAX];
	/* keep track of path for every nodes in x */
	static ArrayList<Integer[]> track = new ArrayList<Integer[]>();
	static int matching = 0;
	static ArrayList<String> result = new ArrayList<String>();

	public static void main(String args[]) {
		read();
		Arrays.fill(match, 0);
		int i = Hungarian();
		System.out.println("Maxium matching is " + i);
		for (String str : result) {
			System.out.println(str);
		}
	}

	public static void read() { // update adj_list
		Scanner scan = null;
		try {
			scan = new Scanner(System.in);
		} catch(Exception e) {}
		x_set = scan.nextInt();
		y_set = scan.nextInt();
		num_edges = scan.nextInt();
		for (int i = 1; i <= num_edges; i ++) {
      		int x = scan.nextInt();
      		int y = scan.nextInt();
      		adj_list[x][ ++adj_list[x][0] ] = y;
    	}
	}

	public static boolean bfs(int start) {
		int index = 0;
		Integer[] src = new Integer[2];
		src[0] = start;
		src[1] = -1;
		track.clear();
		track.add(src);
		used[start] = true;

		while (index < track.size()) {
			int node = track.get(index)[0];
			if (node < x_set+1) { // node in x set
				for (int i = 1; i <= adj_list[node][0]; i ++) {
					int y = adj_list[node][i];
					/*
					if (!used[y])
						break;
					*/
					if (used[y])
						continue;
					Integer[] tmp = new Integer[2];
					tmp[0] = y;
					tmp[1] = index;
					System.out.println(y + " " + node);
					track.add(tmp);
					used[y] = true;
					if (match[y] == 0) {
						index = MAX;
						break;
					}
				}
			}
			else {  // node in y set
				int x = match[node];
				if (x == 0)
					break;
				else{
					if (!used[x]) {
						Integer[] tmp = new Integer[2];
						tmp[0] = x;
						tmp[1] = index;
						track.add(tmp);
						used[x] = true;
					}
				}
			}
			index ++;
		}

		// debug 
	    /*
	    System.out.println("round: " + start);
	    for (Integer[] t : track) {
	      System.out.print("[" + t[0] + ", " + t[1] + "]");
	    }
	    System.out.println();
	    */

		Integer[] last_pair = track.get(track.size()-1);
		int last = last_pair[0];

		if (match[last] != 0 || last <= x_set)
			return false;
		
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(last_pair[0]);
		int father = last_pair[1];
		while (father != -1) {
			last_pair = track.get(father);
			path.add(last_pair[0]);
			father = last_pair[1];
		}	

		for (int i = 0; i < path.size()-1; i += 2) {
			match[path.get(i)] = path.get(i+1);
		}
		return true;
	}

	public static boolean dfs(int x) {
		for (int i = 1; i <= adj_list[x][0]; i ++) {
			int y = adj_list[x][i];
			if (!used[y]) {
				used[y] = true;
				if (match[y] == 0 || dfs(match[y])) {
					match[y] = x;
					return true;
				}
			} 
		}
		return false;
	}

	public static int Hungarian() {
		for (int i = 1; i <= x_set; i ++) {
			Arrays.fill(used, false);
		/*	if (bfs(i)) {
				matching ++;
			}*/
			if (dfs(i)) {
				matching ++;
			}
		}
		for (int i = 1; i <= x_set; i ++) {
			for (int j = x_set+1; j <= x_set+y_set; j ++) {
				if (match[j] == i) {
					result.add(i + " " + j);
				}
			}
		}

		return matching;
	}
}