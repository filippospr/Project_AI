import java.util.ArrayList;
import java.lang.Math;

public class SOS {

	static int nodes = 0;
	static int aiWins = 0;
	//creates children of state
	public static void createChildren(State state) {
		if (!state.isFinal) {
			ArrayList<int[]> emptyPosList = findEmptyPos(state);

			for(int[] pos: emptyPosList) {
				state.children.add(new State(state.grid, !state.isAI, pos, 'S'));//all S children
				state.children.add(new State(state.grid, !state.isAI, pos, 'O'));//all O children
			}
		}
	}

	public static void createGameTree(State state) {
		nodes++;
		if (state.isFinal) {
			if (state.isAI && state.value == 1)
				aiWins++;
			return;
		}
		createChildren(state);
		int v = 1;
		if (state.isAI)
			v = -1;

		for (State child: state.children) {
			createGameTree(child);
			if((child.value > v && state.isAI) || (child.value < v && !state.isAI))
				v = child.value;
		}
		state.value = v;
	}

	//finds all empty positions of state
	public static ArrayList<int[]> findEmptyPos(State state) {
		ArrayList<int[]> emptyPosList = new ArrayList<int[]>();

		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				if (state.grid[i][j] == ' ')
					emptyPosList.add(new int[]{i,j});

		return emptyPosList;
	}



	public static void main(String[] args) {
		char[][] startingState = new char[][]{{' ',' ',' '}, {' ',' ',' '}, {' ',' ',' '}};
		State gameState = new State(startingState, true, new int[]{1,2}, 'O');
		gameState.printState();
		createGameTree(gameState);
		Scanner input = new Scanner(System.in);

		while (!gameState.isFinal){
			System.out.println("Plz enter S or O and its coordinates (ex. O 1 2)");
			char c = input.nextChar();
			int[] pos = new int[]{input.nextInt(), input.nextInt()};//TODO: check if invalid not S/O or bad coordinates
			

		}

		//System.out.println(root.value);
		//System.out.printf("Nodes=%d\nAI_Wins=%d\n",nodes,aiWins);
		/*createChildren(root);
		System.out.println(root.children.size());
		for(State child: root.children){
			System.out.println("--------------------------------------------");
			child.printState();
		}*/
	}

	static class State {
		char[][] grid = new char[3][3];
		int value;
		boolean isAI;
		boolean isFinal = false;
		ArrayList<State> children;

		State(char[][] grid, boolean isAI, int[] pos, char c) {
			for (int i=0; i < 3; i++)
				for (int j=0; j < 3; j++) {

					this.grid[i][j] = grid[i][j];
					//add char at pos
					if (i == pos[0] && j ==pos[1])
						this.grid[i][j] = c;
				}

			this.isAI = isAI;
			isFinalState(pos, c);
			if (!isFinal) children = new ArrayList<State>();
		}

		private void isFinalState(int[] pos, char c) {
			//ArrayList<int[]> neighbours = findNeighbours(pos);
			for (int i = Math.max(0, pos[0]-1); i <= Math.min(2, pos[0]+1); i++)
				for (int j = Math.max(0, pos[1]-1); j <= Math.min(2, pos[1]+1); j++){
					if(i != pos[0] || j != pos[1] && grid[i][j] != ' '){
						int[] neighbour = new int[]{i,j};
						int x=-1, y=-1;
						//my char is O look for S
						if (c == 'O' && grid[neighbour[0]][neighbour[1]] == 'S') {
							x = 2*pos[0] - neighbour[0];
							y = 2*pos[1] - neighbour[1];
						//my char is S look for O
						}else if (c == 'S' && grid[neighbour[0]][neighbour[1]] == 'O'){
							x = 2*neighbour[0] - pos[0];
							y = 2*neighbour[1] - pos[1];
						}

						if (x < 3 && y < 3 && x >= 0 && y >= 0)
								if (grid[x][y] == 'S') {
									isFinal = true;
									//1 AI wins -1 player wins
									if (isAI) value = 1;
									else value = -1;
									
									return;
								}
				}
			}
			for (char[] line: grid)
				for (char chr: line)
					if (chr == ' ')
						return;
			isFinal = true;
			value = 0;
		}

		/*private ArrayList<int[]> findNeighbours(int[] pos) {
			ArrayList<int[]> neighbours = new ArrayList<int[]>();
			
			for (int i = Math.max(0, pos[0]-1); i <= Math.min(2, pos[0]+1); i++)
				for (int j = Math.max(0, pos[1]-1); j <= Math.min(2, pos[1]+1); j++){
					if(i != pos[0] || j != pos[1] && grid[i][j] != ' ')
						neighbours.add(new int[]{i, j});
				}
			return neighbours;
		}*/

		void printState() {
			for (char[] line: grid) {
				for (char chr: line)
					System.out.print(chr+"|");
				System.out.println();
			}
			System.out.println("--------------------------------------------");
		}
	}
}