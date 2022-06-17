import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class main {
    public static Scanner input = new Scanner(System.in);

    public static void menu(int s) throws FileNotFoundException {
        if (s == 1) {
            // Selecting n
            System.out.println("Input n:");
            int n = input.nextInt();

            // Selecting option
            System.out.println("Please select an option: " + "\n" +
                    "   -Enter 0 to skip." + "\n" +
                    "   -Enter 1 for right unit LS." + '\n' +
                    "   -Enter 2 for left unit LS." + '\n' +
                    "   -Enter 3 for left and right unit LS." + '\n' +
                    "   -Enter 4 for RCLS." + '\n' +
                    "   -Enter 5 for right unit RCLS." + '\n' +
                    "   -Enter 6 for post-comutative." + '\n' +
                    "   -Enter 7 for RCLS transformed by Tseitin transformation." + '\n' +
                    "   -Enter 8 for comutative." + '\n' +
                    "   -Enter 6 for right unit post-comutative.");
            int option = input.nextInt();

            generator gen1 = new generator(n, option);
            System.out.println("Your cnf formula has been generated.");

            // Printing cnf formula
            PrintStream o = new PrintStream(new File("cnf.txt"));
            PrintStream console = System.out;
            System.setOut(o);
            System.out.println("p cnf " + gen1.getNumberOfValues() + " " + gen1.getNumberOfClauses());
            //System.out.print("p cnf "+gen1.getNumberOfValues()+" "+gen1.getNumberOfClauses() + "\\n");
            gen1.printClauses();
            System.setOut(console);
        }
        if (s == 2) {
            // Putting in values from SAT solver
            System.out.println("Input n:");
            int n = input.nextInt();
            generator gen1 = new generator(n, 0);
            System.out.println("Paste output from SAT in one line with numbers only:");
            String flush = input.nextLine();
            String tmp = input.nextLine();
            // Printing result (in console and .txt document)
            System.out.println("Result:");
            gen1.printResult(tmp);
            //PrintStream p = new PrintStream(new File("result.txt"));
            //System.setOut(p);
            //gen1.printResult(tmp);
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Enter 1 for generating CNF or 2 for printing results (enter anything else for exit):");
        int s = input.nextInt();
        while (s == 1 || s == 2) {
            menu(s);
            System.out.println("Enter 1 for generating CNF or 2 for printing results (enter anything else for exit):");
            s = input.nextInt();
        }

    }

}
