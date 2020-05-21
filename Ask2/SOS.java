import java.util.ArrayList;
import java.lang.Math;
import java.util.Scanner;

public class SOS {

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
		if (state.isFinal)
			return;
		createChildren(state);
		//initialize v
		int v = 1;
		if (state.isAI)
			v = -1;

		for (State child: state.children) {
			createGameTree(child);
			//find max value for AI nodes and min value for player nodes
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

	public static boolean isValidMove(State gameState, char c, int[] pos) {
		if (c != 'S' && c != 'O')//invalid letter
			return false;
		if (pos[0] > 3 || pos[0] < 0 || pos[1] > 3 || pos[1] < 0)//out of bounds
			return false;
		if (gameState.grid[pos[0]][pos[1]] != ' ')//position already taken
			return false;
		return true;
	} 



	public static void main(String[] args) {
		System.out.print("Building Game Tree...");

		char[][] startingState = new char[][]{{' ',' ',' '}, {' ',' ',' '}, {' ',' ',' '}};
		State gameState = new State(startingState, true, new int[]{1,2}, 'O');
		//create whole game tree
		createGameTree(gameState);
		System.out.println("Done");

		Scanner input = new Scanner(System.in);
		gameState.printState();
		//Begin game
		while (!gameState.isFinal){
			System.out.println("AI's turn");
			int maxValue = gameState.children.get(0).value;
			State maxChild = gameState.children.get(0);
			
			//find the child with max value	
			for (State child: gameState.children){
				if (child.value > maxValue){
					maxValue = child.value;
					maxChild = child;
					break;
				}
			}
			gameState = maxChild;
			gameState.printState();
			//AI wins or draw
			if (gameState.isFinal)
				break;

			char c = ' ';
			int[] pos = new int[2];
			while (!isValidMove(gameState, c, pos)){
				System.out.println("Plz enter S or O and its coordinates (ex. O 1 2)");
				c = Character.toUpperCase(input.next().charAt(0));
				pos[0] =input.nextInt();
				pos[1] =input.nextInt();
			}
			//find child that matches with player's move
			for (State child: gameState.children){
				if (child.grid[pos[0]][pos[1]] == c){
					gameState = child;
					break;
				}
			}
			gameState.printState();
		}

		//game over
		if (gameState.value == 1)
			System.out.println("AI Wins!");
		else if (gameState.value == -1)
			 System.out.println("Player wins!!");
		else
			System.out.println("It's a draw");
	}

	static class State {
		char[][] grid = new char[3][3];
		int value;
		boolean isAI;
		boolean isFinal = false;
		ArrayList<State> children;

		State(char[][] grid, boolean isAI, int[] pos, char c) {
			//copy grid from parent and add the new char
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
			//check all chars adjacent to pos
			for (int i = Math.max(0, pos[0]-1); i <= Math.min(2, pos[0]+1); i++){
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
						if (x < 3 && y < 3 && x >= 0 && y >= 0){
							//look for the last S
							if (grid[x][y] == 'S') {
								isFinal = true;
								//1 AI wins -1 player wins
								if (isAI) value = -1;
								else value = 1;	
								return;
							}
						}
					}
				}
			}
			//check if there are cells in the grid
			for (char[] line: grid)
				for (char chr: line)
					if (chr == ' ')
						return;
			//no empty cells it's a draw
			isFinal = true;
			value = 0;
		}

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