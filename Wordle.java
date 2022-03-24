import java.util.*;

public class Wordle {
    // filename of word list, containing line-separated English words
    public static final String WORD_FILE = "words.txt";

    /*
     * printWelcome - prints the message that greets the user at the beginning of the game
     */
    public static void printWelcome() {
        System.out.println();
        System.out.println("Welcome to Wordle!");
        System.out.println("The mystery word is a 5-letter English word.");
        System.out.println("You have 6 chances to guess it.");
        System.out.println();
    }

    /*
     * initWordList - creates the WordList object that will be used to select
     * the mystery work. Takes the array of strings passed into main(),
     * since that array may contain a random seed specified by the user
     * from the command line.
     */
    public static WordList initWordList(String[] args) {
        int seed = -1;
        if (args.length > 0) {
            seed = Integer.parseInt(args[0]);
        }

        return new WordList(WORD_FILE, seed);
    }

    /*
     * readGuess - reads a single guess from the user and returns it
     * inputs:
     *   guessNum - the number of the guess (1, 2, ..., 6) that is being read
     *   console - the Scanner object that will be used to get the user's inputs
     */
    public static String readGuess(int guessNum, Scanner console) {
        String guess;
        do {
            System.out.print("guess " + guessNum + ": ");
            guess = console.next();
        } while (! isValidGuess(guess));

        return guess.toLowerCase();
    }

    public static boolean includes(String s, char c) {
	return s.contains(c + "");
    }

    public static boolean isAlpha(String s) {
	boolean out = true;
	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i);
	    if (! Character.isAlphabetic(c)) {
		out = false;
	    }
	}
	return out;
    }

    public static int numOccur(char c, String s) {
	int count = 0;
	for (int i = 0; i < s.length(); i++) {
	    char cTest = s.charAt(i);
	    if (c == cTest) {
		count ++;
	    }
	}
	return count;
    }

    public static int numInSamePosn(char c, String s1, String s2) {
	int count = 0;
	for (int i = 0; i < s1.length(); i++) {
	    if (c == s1.charAt(i) && c == s2.charAt(i)) {
		count ++;
		}
	}
	return count;
    }

    /*
     * isValidGuess -  takes an arbitrary string guess and returns true
     * if it is a valid guess for Wordle, and false otherwise
     */
    public static boolean isValidGuess(String guess) {
	boolean isValidLength = false;
	boolean isValid = false;
	if (guess.length() == 5) {
	    isValidLength = true;
	}

	if (isValidLength == false) {
	    System.out.println("Your guess must be 5 letters long.");
	}
	if (isAlpha(guess) == false) {
	    System.out.println("Your guess must only contain letters of the alphabet");
	}

	if (isAlpha(guess) && isValidLength) {
	    isValid = true;
	}
        return isValid;
    }

    public static boolean processGuess(String guess, String mystery) {

	boolean toggle = true;
	for (int i = 0; i < guess.length(); i++) {

	    System.out.print(" ");

	    int samePos = numInSamePosn(guess.charAt(i), guess, mystery);
	    int numOccG = numOccur(guess.charAt(i), guess);
	    int numOccM = numOccur(guess.charAt(i), mystery);

	    if (guess.charAt(i) == mystery.charAt(i)) {
		System.out.print(guess.charAt(i));
		}
	    else if (numOccM == numOccG) {
		System.out.print("[" + guess.charAt(i) + "]");
	    }
	    else if ((samePos != 1) && (numOccM == 1) && (numOccG == 1 || toggle)) {
		System.out.print("[" + guess.charAt(i) + "]");
		toggle = false;
	    }
	    else if (samePos !=1 && numOccG > 1) {
		System.out.print("_");
	    }
	    else {
		System.out.print("_");
	    }
	}

	System.out.println();

	if (guess.equals(mystery)) {
	    return true;
	}
	else {
	    return false;
	}
    }


    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        printWelcome();

        // Create the WordList object for the collection of possible words.
        WordList words= initWordList(args);

        // Choose one of the words as the mystery word.
        String mystery = words.getRandomWord();

	boolean winner = false;
	for (int i = 1; i <= 6; i++) {
	    String guess = readGuess(i, console);

	    if (processGuess(guess, mystery)) {
		System.out.println("Congrats! You guessed it!");
		winner = true;
		break;
	    }
	}
	if (! winner) {
	    System.out.println("Sorry! Better luck next time!");
	    System.out.println("The word was " + mystery + ".");
	}
        console.close();
    }
}
