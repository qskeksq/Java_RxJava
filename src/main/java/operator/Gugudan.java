package operator;

import java.util.Scanner;

public class Gugudan {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void gu() {
		Scanner in = new Scanner(System.in);
		System.out.print("Gugudan Input:");
		int dan = Integer.parseInt(in.nextLine());
		for (int row = 1; row <= 9; ++row) {
			System.out.println(dan + " * " + row + " = " + dan*row);
		}
		in.close();
	}

}
