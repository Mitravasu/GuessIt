package com.mitravasu;

/*
    Guess the word, guess how long the word is, guess letters, number of tries decides the score of the person.
 */

import java.util.Scanner;
import java.io.*;

public class Main {
    private static final String NEW_GAME = "1";
    private static final String HOW_TO_PLAY = "2";
    private static final String LEADER_BOARD = "3";
    private static final String EXIT_GAME = "4";
    private static String userName = "";
    private static int points = 1000;
    private static int totalPoints = 0;


    public static void printDashedLine() {
        for (int i = 0; i < 81; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public static void mainMenu() {
        System.out.println("\nWELCOME TO GUESS IT");
        printDashedLine();
        System.out.println("New Game (1)");
        System.out.println("How to Play (2)");
        System.out.println("Leader Board (3)");
        System.out.println("Exit Game (4)");
        printDashedLine();
    }

    public static String getInput() {
        String input = "";
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();

        return input;
    }

    public static String generateRandomWord(String topic) throws Exception {
        String fileName = ".\\Choice" + topic + ".txt";

        Scanner fileScanner = new Scanner(new File(fileName));
        int randomNumber = (int) (Math.random() * 8) + 1;
        if (topic.equals("3")) {
            randomNumber = (int) (Math.random() * 49) + 1;
        }
        String word = "";
        for (int i = 0; i < randomNumber; i++) {
            word = fileScanner.nextLine();
        }

        fileScanner.close();

        return word;
    }

    public static String getValidInput(int rangeStart, int rangeEnd) {
        boolean validChoice = true;
        int temp = 0;
        String choice = "";
        do {
            if (!validChoice) {
                System.out.println("Please enter a valid number between " + rangeStart + " and " + rangeEnd + ".");
            }
            choice = getInput();
            temp = Integer.parseInt(choice);
            validChoice = temp >= rangeStart && temp <= rangeEnd;
        } while (!validChoice);

        return choice;
    }

    // Change conditions to use compareTo
    public static int guessWordLength(String word) {
        int wordLength = word.length();
        int lengthGuess = 0;
        int numberOfTries = -1;

        System.out.println("\nA Word has Magically been chosen!");

        // Loop this until the user gets the correct length, keep track of number of tries
        do {
            System.out.print("Please guess the length of this word: ");
            // Accept user input for length of word
            lengthGuess = Integer.parseInt(getInput().trim());

            // Check whether input is correct
            // If input is incorrect state whether the length is higher/lower.
            if (lengthGuess > wordLength) {
                System.out.println("Guess lower!");
            } else if (lengthGuess < wordLength) {
                System.out.println("Guess higher!");
            }
            numberOfTries++;
        } while (lengthGuess != wordLength);

        if ((points - numberOfTries * 100) >= 0) {
            points -= (numberOfTries * 100);
        }
        return points;
    }

    public static String printBlankWord(String word) {
        String blankWord = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ' ') {
                blankWord += " ";
            } else {
                blankWord += "_";
            }
        }

        return blankWord;
    }

    public static String updateBlankWord(String letter, String blankWord, int index) {
        String newBlankWord = "";
        for (int i = 0; i < blankWord.length(); i++) {
            if (i == index) {
                newBlankWord += letter;
            } else if (blankWord.charAt(i) == ' ') {
                newBlankWord += " ";
            } else if (blankWord.charAt(i) == '_') {
                newBlankWord += "_";
            } else {
                newBlankWord += blankWord.substring(i, i + 1);
            }
        }

        return newBlankWord;
    }

    public static String letterInWord(String letterGuess, String word, String blankWord) {
        String newBlankWord = blankWord;
        for (int i = 0; i < word.length(); i++) {
            if (letterGuess.indexOf(word.substring(i, i + 1)) == 0) {
                newBlankWord = updateBlankWord(letterGuess, newBlankWord, i);
            }
        }
        return newBlankWord;
    }

    public static void newGame() throws Exception {
        // Ask user's name
        System.out.print("Please enter a username: ");
        userName = getInput();

        // Ask the user for topic
        System.out.println("\nWelcome " + userName + ", please choose 1 of the following topics: ");
        System.out.println("1. Vegetables (1)\n2. Fruits (2) \n3. Extreme Words (3)");
        String topic = getValidInput(1, 3);


        // Computer generates random word from the specified topic list.
        String word = generateRandomWord(topic);

        // User guesses the length of the word.
        totalPoints = guessWordLength(word);

        // Print string which shows all the underscores for each unknown letter of the word.
        String blankWord = printBlankWord(word);
        // Ask user to enter a letter, if the letter exists in the word, then update the display to show the letter's
        // position in the word.
        int numberOfTries = 0;
        // Keep looping this until the blankWord is the same as word, each wrong answer deducts 100 points.
        do {
            System.out.println(blankWord);
            System.out.print("Please guess a letter: ");
            String letterGuess = getInput();
            if (blankWord.equals(letterInWord(letterGuess, word, blankWord))) {
                numberOfTries++;
            }
            blankWord = letterInWord(letterGuess, word, blankWord);
        } while (!blankWord.equals(word));

        System.out.println(blankWord);

        if ((1000 - numberOfTries * 100) >= 0) {
            totalPoints += (1000 - numberOfTries * 100);
        }


        // Show their score, and tell them to check the leaderboard for their ranking.
        System.out.println("\nYou scored " + totalPoints + " points!");
        // Ask whether they want to play again or return to main menu
        // if they want to play again, loop the entire game again
    }

    public static void howToPlay() {
        System.out.println("How to play:");
        System.out.println("The user can make choices by typing in the number in brackets beside a choice.");
        System.out.println("1. The computer will generate a random word corresponding to the chosen topic.");
        System.out.println("2. The player will need to guess the length of the word (3 letters to 10 letters).");
        System.out.println("3. Once the player has successfully guessed the length, they will then try and guess\n"
                + "   letters which may be part of the word.");
        System.out.println("4. The player's score will be calculated based on the number of tries that it took to\n"
                + "    guess the word.");
    }

    public static void showLeaderBoard() {
        System.out.println("LEADERBOARD");
        System.out.format("%-10s | %8s | %8s | %8s |", "USERNAME", "LENGTH SCORE", "WORD SCORE", "TOTAL SCORE");
        System.out.format("\n%10s | %12s | %10s | %11s |", getUserName(), points, totalPoints - points, totalPoints);
    }

    public static void exitGame() {
        System.out.println();
        printDashedLine();
        System.out.println("Thank you for playing GUESS IT!");
        printDashedLine();
    }

    public static boolean mainMenuChoice(String choice) throws Exception {
        boolean validChoice;
        String playAgain = "";
        if (choice.equals(NEW_GAME)) {
            do {
                newGame();
                System.out.print("Do you want to play again? (Yes = 1; No = 2): ");
                playAgain = getInput();
            } while (playAgain.compareTo("1") == 0);

            validChoice = true;
        } else if (choice.equals(HOW_TO_PLAY)) {
            howToPlay();
            validChoice = true;
        } else if (choice.equals(LEADER_BOARD)) {
            showLeaderBoard();
            validChoice = true;
        } else if (choice.equals(EXIT_GAME)) {
            exitGame();
            validChoice = true;
        } else {
            System.out.println("Please enter a valid choice (Between 1-4).");
            validChoice = false;
        }

        return validChoice;
    }

    public static String pickAMenu() throws Exception {
        boolean validChoice = false;
        String choice = "";
        do {
            System.out.print("\nPick a Menu: ");
            choice = getInput();
            if (choice.equals(EXIT_GAME)) {
                break;
            }
            validChoice = mainMenuChoice(choice);
        } while (!validChoice);


        return choice;
    }

    public static void play() throws Exception {
        boolean returnToMainMenu = false;
        boolean exitGame = false;
        String input = "";
        do {
            mainMenu();
            exitGame = pickAMenu().equals(EXIT_GAME);
            if (exitGame) {
                break;
            }
            System.out.print("\nDo you want to return to the Main Menu? (Yes = 1; No = 2): ");
            input = getInput();


            returnToMainMenu = input.compareTo("1") == 0;

        } while (returnToMainMenu);


        exitGame();
    }

    public static String getUserName() {
        return userName;
    }

    public static void main(String[] args) throws Exception {
        play();
    }
}
