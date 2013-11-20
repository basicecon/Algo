/*
Ford-Fulkerson Algorithm
LANG: JAVA
BFS and DFS
*/

import java.util.*;
import java.io.*;
import java.lang.Math;

class FordFulkerson {
	static int MAX = 102;
	static int nodes;
	static int edges;
	static int[][] capacity = new int[MAX][MAX];
	static int[][] flow = new int[MAX][MAX];
	static boolean[] used = new boolean[MAX];
	static int[] father = new int[MAX];
	static int maximumFlow = 0;

	public static void main(String args[]) {
		read();
		int max = FordFulkerson(0, nodes-1);
		System.out.println(max);
	}

	public static void read() {
		Scanner scan = null;
		try {
			scan = new Scanner(System.in);
		} catch(Exception e) {}
		nodes = scan.nextInt();
		edges = scan.nextInt();
		for (int i = 0; i < edges; i ++) {
			int x = scan.nextInt();
			int y = scan.nextInt();
			int cap = scan.nextInt();
			capacity[x][y] = cap;
		}
	}

	public static int FordFulkerson(int src, int des) {
		for (int[] f : flow)
			Arrays.fill(f, 0);
		while (true) {
			Arrays.fill(used, false);
			father[src] = -1;
/*
			if (!bfs(src, des))
				break;
*/
			dfs(src, des);
			if (!used[des])
				break;

			int minflow = 99999;
			for (int i = des; father[i] >= src; i = father[i]) {
				minflow = (int)Math.min(minflow, capacity[father[i]][i] - flow[father[i]][i]);
			}
			for (int i = des; father[i] >= src; i = father[i]) {
				flow[father[i]][i] += minflow;
				flow[i][father[i]] -= minflow;
			}
			maximumFlow += minflow;
		} 
		return maximumFlow;
	}

	public static void dfs(int src, int des) {
		used[src] = true;
		for (int i = 0; i < nodes; i ++) {
			if (!used[i] && (capacity[src][i] > flow[src][i])) {
				used[i] = true;
				father[i] = src;
				dfs(i, des);
			}	
		}
	}	

	public static boolean bfs(int src, int des) {
		used[src] = true;
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(src);
		int index = 0;

		while (index < path.size()) {
			int node = path.get(index);
			for (int i = 0; i < nodes; i ++) {
				if (!used[i] && (capacity[node][i] > flow[node][i])) {
					used[i] = true;
					father[i] = node;
					path.add(i);
				}
			}
			index ++;
		}
		return used[des];
	}
}