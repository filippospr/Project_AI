import java.lang.Math;
import java.util.*;

public class A_star {
	private ArrayList<State> frontier = new ArrayList<State>();
	private HashSet<String> closedSet = new HashSet<String>();
	static int N;

	//adds a new state to frontier
	public void addToFrontier(State state) {
		int size = frontier.size();
		for (int i=0; i<size; i++) {
			if (state.estimated_cost < frontier.get(i).estimated_cost) {
				frontier.add(i, state);//found pos of state
				return;
			}
		}
		//for empty frontier or for biggest element
		frontier.add(state);
	}

	/*Removes the first element of the frontier*/
	public State removeFromFrontier() {
		if (frontier.size() == 0)
			return null;//frontier empty return null
		return frontier.remove(0);
	}

	public boolean frontierIsEmpty() {
		return frontier.size() == 0;
	}

	//check if state is in closed set
	public boolean inClosedSet(State state) {
		return closedSet.contains(state.state);
	}

	public void addToClosedSet(State state) {
		closedSet.add(state.state);
	}

	public ArrayList<State> createChildren(State state) {
		ArrayList<State> children = new ArrayList<State>();
		int pos = state.state.indexOf('-');

		for (int i=0; i<state.state.length(); i++) {
			char[] child = state.state.toCharArray();
			if (i != pos) {
				//Swap '-' with another character
				char temp = child[i];
				child[i] = child[pos];
				child[pos] = temp;
				//calculate cost
				int cost = Math.abs(pos-i);
				if (cost <= N) {
					cost = state.cost + cost;
					children.add(new State(new String(child), cost, state));
				}
			}
		}
		return children;
	}

	//check it state is final
	public boolean isFinalState(State state) {
		char[] fState = state.state.toCharArray();
		int blacks = 0;

		for(int i=0; i<fState.length; i++) {
			if (fState[i] == 'M')
				blacks++;
			else if(fState[i] == 'A' && blacks < (fState.length-1)/2)//all blacks need to be left
				return false;
			else if(i == fState.length-1 && fState[i] != 'A')//last needs to be white
				return false;
		}
		return true;
	}

	//check if initial state is valid
	public boolean isValidState(String state) {
		char[] chars = state.toCharArray();
		int blacks = 0, whites = 0;
		boolean dash = false;

		for (char c: chars) {
			if (c == 'M')
				blacks++; //count blacks
			else if (c == 'A')
				whites++; //count whites
			else if (c == '-' && !dash) //exactly one dash
				dash = true;
			else return false;
		}
		N = blacks;
		return blacks == whites && dash;
	}

	public static void main(String[] args) {
		A_star astar = new A_star();
		Scanner scanner = new Scanner(System.in);
		String input = "";
		int extensions = 0;
		
		//Get Starting state
		while(!astar.isValidState(input)) {
			System.out.print("Give me a starting state: ");
			input = scanner.nextLine();
			System.out.println();
		}
		State state = new State(input, 0);
		astar.addToFrontier(state);//Step 1
		ArrayList<State> children;

		while(!astar.frontierIsEmpty()) {//Step 2
			state = astar.removeFromFrontier();//Step 3
			if (astar.inClosedSet(state))//Step 4
				continue;
			//Should stop here for A_star
			if (astar.isFinalState(state)) {//Step 5
				break;
			}
			children = astar.createChildren(state);//Step 6
			for (State child: children)
				astar.addToFrontier(child);//Step 7
			extensions++;
			astar.addToClosedSet(state);//Step 8
		}
		System.out.printf("Best path is: %s\nCost is %d\n", state.getPath(), state.cost);
		System.out.println("Total extensions: " + extensions);
	}

	static class State{
		String state;
		int cost, estimated_cost;//cost=g(n), and heuristic_cost=h(n)
		ArrayList<String> path = new ArrayList<String>();

		State(String state, int cost) {
			this.state = state;
			this.cost = cost;
			path.add(state);
			estimated_cost = cost + heuristicCost();
		}

		State(String state, int cost, State parent){
			this.state = state;
			this.cost = cost;
			path.addAll(parent.path);
			path.add(state);
			estimated_cost = cost + heuristicCost();
		}

		//calculate heuristic function h(n)
		int heuristicCost(){
			char[] chars = state.toCharArray();
			ArrayList<Integer> whites = new ArrayList<Integer>();
			int cost = 0, pos;
			//find the pos of all whites on left side
			for (int c=0; c<=N;c++){
				if (chars[c] == 'A')
					whites.add(c);
			}
			//no whites on left side
			if (whites.size() == 0)
				return 0;

			pos = whites.get(0);

			//match whites on left with blacks on right
			for (int c=N+1; c<2*N+1;c++) {
				if (chars[c] == 'M')
					cost += c-pos;
			}
			return cost;
		}

		//get path of state
		String getPath(){
			String result = "";
			for (String state: path)
				result += state + " ";
			return result;
		}
	}
}