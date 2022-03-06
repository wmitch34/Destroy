// Will mitchell 02/17/2022
// COP 3503 Guha
// D E S T R O Y

import java.io.*;
import java.util.*;

public class destroy{

	// Globally accessable data structures
	public static int[] parent;
	public static int[] height;
	public static HashMap<Integer, Pair> d_values_map = new HashMap<>();
	
	// create disjoint set where each index's value is itself, 
	// each nodes root is itself initially. This is Prof guha's code
	private static void initDJset(int n){
		parent = new int[n];
		for(int i = 1; i < n; i++){
			parent[i] = (int) i;
		}
	}

	// default height for each tree is one. initial connectivity is n
	private static void initHeightArr(int n){
		height = new int[n];
		for(int i = 1; i < n; i++){
			height[i] = (int) 1;
		}
	}

	// Professor Guha's find function
	private static int find(int target){
		if(parent[target] == target) return target;

		int ret = find(parent[target]);

		parent[target] = ret;

		return ret;
	}

	// professor Guh's union function with added height tracker at each node
	private static boolean union(int u , int v){
		int root_v1 = find(u);
		int root_v2 = find(v);

		if (root_v1 == root_v2) return false;
		parent[root_v2] = root_v1;

		// height tracker here: it works like a tally system with a set number 
		// of donations. You can't give tallys away without losing them
		// When tallies get donated, the donator's tallies gets set to 0
		height[root_v1] += height[root_v2];
		height[root_v2] = 0;

		return true;
	}

	// connectivity is the height at each node squared and summed
	private static long connectivity(int n){
		long sum = 0;
		for(int i = 0; i < n; i++){
			sum += ((long) height[i] * (long) height[i]);
		}
		return sum;
	}



	public static void main(String[] args)throws IOException{

		Long start  = System.currentTimeMillis();

		Scanner reader = new Scanner(System.in);

		int n =  1 + reader.nextInt(); // num items in system
		int m =  reader.nextInt(); // num union opperatioins
		int d =  reader.nextInt(); // num destroys

		initDJset(n);
		initHeightArr(n);

		// we need an output for each d value and an output for the whole set: d+1
		long[] myOutputArray = new long[d + 1];

		// get opperations list and put it in a pair array
		for(int i = 0; i < m; i++){
			int u = reader.nextInt();
			int v = reader.nextInt();
			d_values_map.put(i + 1, new Pair(u, v));
		}

		// need array for in order access to d values
		int[] d_values_array = new int[d];

		// need hash set for fast contains() function
		HashSet<Integer> d_values_Hash_set = new HashSet<>();

		// get values to remove and push to array and hashset
		for(int i = 0; i < d; i++){
			int input = reader.nextInt();
			d_values_Hash_set.add(input);
			d_values_array[i] = input;
		}


		// initial build without connections to destroy.
		// if in index value appears in d array, dont push union to main branch, else do push
		for(int i = 1; i < m; i++){
			if(!d_values_Hash_set.contains(i)){
				Pair myPair = d_values_map.get(i);
				union(myPair.x, myPair.y);
			}			
		}

		// first connectivity value calculated is last value to print
		myOutputArray[d] = connectivity(n);

		// add connections one at a time and backfill output array.
		// last destroy pair is first union. hashmap connects index values to union pair
		for(int i = d - 1 ; i >=0; i--){
			Pair myPair = d_values_map.get(d_values_array[i]);
			union(myPair.x, myPair.y);
			myOutputArray[i] = connectivity(n);
		}

		// print the outputs post process
		for(int i = 0; i < d + 1; i++){
			System.out.println(myOutputArray[i]);
		}

		
		//  long stop = System.currentTimeMillis();
		// float time = ((float) (stop - start) )/ ((float)1000);
		//  System.out.println(time);
	}

}

// class to store union pairs. could have saved pair index to avoid using hashmap also
class Pair{
	int x;
	int y;

	public Pair(int inOne, int inTwo){
		this.x = inOne;
		this.y = inTwo;

	}
}