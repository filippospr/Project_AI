import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;

public class UCS {
	ArrayList<State> frontier = new ArrayList<State>();

	public void addToFrontier(State state){
		int size = frontier.size();
		//for empty frontier
		for (int i=0; i<size; i++) {
			if (state.cost < frontier.get(i).cost) {
				frontier.add(i, state);//found pos of state
				return;
			}
		}
		frontier.add(state);
	}

	/*Removes the first element of the frontier since it is sorted*/
	public State removeFromFrontier(){
		if (frontier.size() == 0)
			return null;//frontier empty return null
		return frontier.remove(0);
	}

	public void createChildren(State state){
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
				int cost = state.cost + Math.abs(pos-i);
				children.add(new State(new String(child),cost));
			}
		}
		System.out.println(children);
	}

	public static void main(String[] args) {
		/* Get Starting state
		Scanner scanner = new Scanner(System.in);
		System.out.println("Give me a starting state: ");
		String state = scanner.nextLine();*/

		UCS mak = new UCS();
		State testState = new State("MMAMMA-AM", 10);
		mak.addToFrontier(testState);
		mak.createChildren(testState);
		System.out.println();
	}

	static class State{
		String state;
		int cost;

		State(String state, int cost){
			this.state = state;
			this.cost = cost;
		}

		public String toString(){
			return state+':'+cost;
		}
	}
}