import java.lang.Math;
import java.util.*;

public class UCS {
	private ArrayList<State> frontier = new ArrayList<State>();
	private HashSet<String> closedSet = new HashSet<String>();
	private ArrayList<State> finalStates = new ArrayList<State>();
	int N;

	//adds a new state to frontier
	public void addToFrontier(State state){
		int size = frontier.size();
		for (int i=0; i<size; i++) {
			if (state.cost < frontier.get(i).cost) {
				frontier.add(i, state);//found pos of state
				return;
			}
		}
		//for empty frontier or for biggest element
		frontier.add(state);
	}

	/*Removes the first element of the frontier*/
	public State removeFromFrontier(){
		if (frontier.size() == 0)
			return null;//frontier empty return null
		return frontier.remove(0);
	}

	public boolean frontierIsEmpty(){
		return frontier.size() == 0;
	}
	//closed set
	public boolean inClosedSet(State state){
		return closedSet.contains(state.state);
	}

	public void addToClosedSet(State state){
		closedSet.add(state.state);
	}

	//final state list
	public void addFinalState(State state){
		finalStates.add(state);
	}

	public ArrayList<State> createChildren(State state){
		ArrayList<State> children = new ArrayList<State>();
		int pos = state.state.indexOf('-');

		for (int i=0; i<state.state.length(); i++) {
			char[] child = state.state.toCharArray();
			if (i != pos){
				//Swap '-' with another character
				char temp = child[i];
				child[i] = child[pos];
				child[pos] = temp;
				//calculate cost
				int cost = Math.abs(pos-i);
				if (cost <= N){
					cost = state.cost + cost;
					children.add(new State(new String(child), cost, state));
				}
			}
		}
		return children;
	}


	public boolean isFinalState(State state){
		char[] fState = state.state.toCharArray();
		int blacks = 0;

		for(int i=0; i<fState.length; i++){
			if (fState[i] == 'M')
				blacks++;
			else if(fState[i] == 'A' && blacks < (fState.length-1)/2)//all blacks need to be left
				return false;
			else if(i == fState.length-1 && fState[i] != 'A')//last needs to be white
				return false;
		}
		return true;
	}

	//find final state with min cost
	public State findBestSolution(){
		//System.out.println(finalStates);
		State minState = finalStates.get(0);
		for (State state: finalStates)
			if (state.cost<minState.cost)
				minState = state;
		return minState;
	}

	//check if initial state is valid
	public boolean isValidState(String state){
		char[] chars = state.toCharArray();
		int blacks = 0, whites = 0;
		boolean dash = false;

		for (char c: chars) {
			if (c == 'M')
				blacks++;
			else if (c == 'A')
				whites++;
			else if (c == '-' && !dash) //exactly one dash
				dash = true;
			else return false;
		}
		N = blacks;
		return blacks == whites && dash;
	}

	public static void main(String[] args) {
		UCS ucs = new UCS();
		Scanner scanner = new Scanner(System.in);
		String input = "";
		//Get Starting state
		while(!ucs.isValidState(input)){
			System.out.print("Give me a starting state: ");
			input = scanner.nextLine();
			System.out.println();
		}
		State state = new State(input, 0);
		ucs.addToFrontier(state);//Step 1
		ArrayList<State> children;
		//System.out.printf("Starting state is:%s\n",state);
		while(!ucs.frontierIsEmpty()){//Step 2
			//System.out.printf("Removing state %s from frontier\n",state);
			state = ucs.removeFromFrontier();//Step 3
			if (ucs.inClosedSet(state))//Step 4
				continue;
			if (ucs.isFinalState(state)){//Step 5
				//System.out.printf("State %s is Final add to Fstate list\n",state);
				ucs.addFinalState(state);
				continue;//6
			}
			//System.out.printf("Creating children of state %s\n",state);
			children = ucs.createChildren(state);//Step 7
			//System.out.printf("Adding children of state %s to frontier\n",state);
			for (State child: children)
				ucs.addToFrontier(child);//Step 8
			//System.out.printf("Adding state %s to Closed Set\n",state);
			ucs.addToClosedSet(state);//Step 9
		}
		State solution = ucs.findBestSolution();
		System.out.printf("Best path is: %s\nCost is %d\n", solution.getPath(), solution.cost);
	}

	static class State{
		String state;
		int cost;
		ArrayList<String> path = new ArrayList<String>();

		State(String state, int cost){
			this.state = state;
			this.cost = cost;
			path.add(state);
		}

		State(String state, int cost, State parent){
			this.state = state;
			this.cost = cost;
			path.addAll(parent.path);
			path.add(state);
		}

		//get path of state
		String getPath(){
			String result = "";
			for (String state: path)
				result += state + " ";
			return result;
		}
		//used for tests
		/*public String toString(){
			return state+':'+cost;
		}/**/
	}
}