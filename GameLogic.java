import java.awt.Color;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.awt.event.KeyEvent;
import java.io.*;

public class GameLogic{
   
      
   //Name of file containing all the possible "secret words"
   private static final String SECRET_WORDS_FILENAME = "secrets.txt";   
   
   //Name of file containing all the valid guess words
   private static final String VALID_GUESSES_FILENAME = "valids.txt";   
   
   //Use for generating random numbers!
   private static final Random rand = new Random();
   
   //Dimensions of the game grid in the game window
   public static final int MAX_ROWS = 6;
   public static final int MAX_COLS = 5;
   
   //Character codes for the enter and backspace key press
   public static final char ENTER_KEY = KeyEvent.VK_ENTER;
   public static final char BACKSPACE_KEY = KeyEvent.VK_BACK_SPACE;
   
   //The null character value (used to represent an "empty" value for a spot on the game grid)
   public static final char NULL_CHAR = 0;
   
   //Various Color Values
   private static final Color CORRECT_COLOR = new Color(53, 209, 42); //(Green)
   private static final Color WRONG_PLACE_COLOR = new Color(235, 216, 52); //(Yellow)
   private static final Color WRONG_COLOR = Color.DARK_GRAY; //(Dark Gray [obviously])
   private static final Color DEFAULT_KEY_COLOR = new Color(160, 163, 168); //(Light Gray)
   

   
   //A preset, hard-coded secret word to be use when the resepective debug is enabled
   private static final char[] DEBUG_PRESET_SECRET = {'S', 'L', 'E', 'E', 'K'};      
   
   
   //Array storing all valid guesses read out of the respective file
   private static String[] validGuesses;
   
   //The current row/col where the user left off typing
   private static int currentRow, currentCol;
      
   
   
   //This function gets called ONCE when the game is very first launched
   //before the user has the opportunity to do anything.
   //
   //Should perform any initialization that needs to happen at the start of the game,
   //and return the randomly chosen "secret word" as a char array
   //
   //If either of the valid guess or secret words files cannot be read, or are
   //missing the word count in the first line, this function returns null.
   
   public static char[] initializeGame(){
      currentCol = 0; 
      currentRow = 0; 
      
      if (vaildGuessHelper() == false) {
         return null;
      } else {
         if (JWordleLauncher.DEBUG_USE_PRESET_SECRET){
            return DEBUG_PRESET_SECRET;
         }
         
         String randomWord = secretHelper();
         return randomWord.toCharArray(); 
      }
   }

   public static boolean vaildGuessHelper(){
      int count = 0; 

      try {
      File wordsText = new File(VALID_GUESSES_FILENAME);
      Scanner scnr = new Scanner(wordsText);
      int numWords = (int) scnr.nextInt();   
      scnr.next();
      validGuesses = new String [numWords]; 
      
      while(scnr.hasNextLine()){
         String word = scnr.nextLine ();
         validGuesses [count] = word; 
         count++; 
      }
      scnr.close();
      return true;
      } catch (FileNotFoundException e) {
         return false;
      }
   }

   public static String secretHelper(){
      int count = 0; 
      
      try {
      File wordsText = new File(SECRET_WORDS_FILENAME);
      Scanner scnr = new Scanner(wordsText);
      int numWords = (int) scnr.nextInt();   
      scnr.next();
      String [] secretArr = new String [numWords]; 
      
      while(scnr.hasNextLine()){
         String word = scnr.nextLine ();
         secretArr [count] = word; 
         count++; 
      }
         scnr.close();
         return secretArr[rand.nextInt(numWords)]; 
      
      } catch (FileNotFoundException e) {
         return null;
      }
   }
   
   public static boolean validAndWiggle(){

      String storeWord = "";

      for (int i =0; i < MAX_COLS; i++){
         storeWord += GameGUI.getGridChar(currentRow, i);
      }
      for(int i =0; i < validGuesses.length; i++){
         if (validGuesses[i].equals(storeWord)){
            return true; 
         }
      }
      GameGUI.wiggle(currentRow);
      return false; 

   }




   
   //Complete your warmup task (Section 3.1.1 part 2) here by calling the requisite
   //functions out of GameGUI.
   //This function gets called ONCE after the graphics window has been
   //initialized and initializeGame has been called.
   public static void warmup(){
      /*
      GameGUI.setGridChar(0,0,'C');
      GameGUI.setGridColor(0, 0, CORRECT_COLOR);
      GameGUI.setGridChar(1, 3,'O');
      GameGUI.setGridColor(1, 3, WRONG_COLOR);
      GameGUI.setGridChar(3, 4, 'S');
      GameGUI.setGridColor(3, 4, DEFAULT_KEY_COLOR);
      GameGUI.setGridChar(5, 4, 'C');
      GameGUI.setGridColor(5, 4, WRONG_PLACE_COLOR);
      */
      
     
     //All of your warmup code will go in here except for the
     //"wiggle" task (3.1.1 part 3)... where will that go?
     
   }
   
   
   
   //This function gets called everytime the user types a valid key on the
   //keyboard (alphabetic character, enter, or backspace) or clicks one of the
   //keys on the graphical keyboard interface.
   //The key pressed is passed in as a char value.
   public static void reactToKey(char key){
      /*if (key == 'W'){
         GameGUI.wiggle(3);
       }*/

       //checking if its valid 

      if (BACKSPACE_KEY != key && ENTER_KEY != key && currentCol < MAX_COLS){ //updates current columns
         GameGUI.setGridChar(currentRow, currentCol, key);
         currentCol++;
      } else if (ENTER_KEY == key && currentRow >= 0 && currentRow < 6 && currentCol == 5){//updates current rows
         
         if (validAndWiggle() == true) {
         currentCol=0; 
         helperColor(); 
         currentRow++;
         System.out.println("currentCol = " + currentCol);
         } 

      } else if (BACKSPACE_KEY == key && currentCol  > 0){
         currentCol--; 
         GameGUI.setGridChar(currentRow, currentCol, NULL_CHAR);    

      }else{ 
         System.out.println("currentCol = " + currentCol);
         GameGUI.wiggle(currentRow);
      }
       System.out.println("reactToKey(...) called! key (int value) = '" + ((int)key) + "'");
   }
   

//This function checks how many times the guessed word 
public static int numsTimesLetter(char[] checkWord, char letterCheck){

   int count = 0; 
   for (int i = 0; i<checkWord.length; i++){
      if (letterCheck == checkWord[i]){
         count++; 
      }
   }
   return count; 
}


   //Counts how many yellow or greens there are and returns the number 
   public static int Green_Or_Yellow_Count (char storedChr){
      
      int greenOrYellowCount = 0; 

      for (int x = 0; x < MAX_COLS; x++){
        
         if (storedChr == GameGUI.getGridChar(currentRow,x)){ //
            
            if (GameGUI.getGridColor(currentRow, x) == WRONG_PLACE_COLOR || GameGUI.getGridColor(currentRow, x) == CORRECT_COLOR){
               greenOrYellowCount++; 
            }      
         }
      }
      return greenOrYellowCount; 
   }

   //checks if the color is green or gray 
   public static void helperColor (){
   
      char secret [] = GameGUI.getSecretWordArr(); 
      
      for (int i = 0; i < MAX_COLS; i++){
         char storedChr = GameGUI.getGridChar(currentRow, i);
         System.out.println("currentRow = " + currentRow);

         if ((new String (secret)).contains(Character.toString(storedChr))){
            
            if (secret [i] == storedChr){  //Sets to green
               GameGUI.setGridColor(currentRow, i, CORRECT_COLOR);
               GameGUI.setKeyColor(storedChr, CORRECT_COLOR);
            } 

         } else { //Sets to grey 
            GameGUI.setGridColor(currentRow, i, WRONG_COLOR); 
            System.out.print(i + " " + storedChr);
            //check = false; 
            GameGUI.setKeyColor(storedChr, WRONG_COLOR);      
         }
      }
      helperForDouble (secret);
      checkingWin (secret);
   }
   

//Deals with the hierarchy of yellow to grey 
public static void helperForDouble (char [] secret){

   for (int i = 0; i < MAX_COLS; i++){

      char storedChr = GameGUI.getGridChar(currentRow, i);
      System.out.println("currentRow = " + currentRow);

      if (GameGUI.getGridColor(currentRow, i) != WRONG_PLACE_COLOR && GameGUI.getGridColor(currentRow, i) != CORRECT_COLOR){
         
         int numLetterInSecret = numsTimesLetter(secret, storedChr); 
         int counted = Green_Or_Yellow_Count (storedChr);

         if (counted < numLetterInSecret){ //turns it into yellow 
            GameGUI.setGridColor(currentRow, i, WRONG_PLACE_COLOR);

            if (GameGUI.getKeyColor(storedChr) != CORRECT_COLOR ){ //turns into yellow with hierarchy 
               GameGUI.setKeyColor(storedChr, WRONG_PLACE_COLOR);
            }

         } else {
            GameGUI.setGridColor(currentRow, i, WRONG_COLOR); //turns it into grey 
         }
      }
   }   
}

   public static boolean winLose(char secret []){

      boolean track = false; 
      
      for (int i = 0; i < MAX_COLS; i++){
         if (secret[i] == GameGUI.getGridChar(currentRow, i)) {
            track = true; 
         } else{
            return false; 
         }
      }
      return track ; 
   }

    //checks to see if its win or lose 
    public static void checkingWin (char secret []){

      if (winLose(secret) == true){ 
         GameGUI.gameOver(true);

      } else if (currentRow > MAX_COLS-1){ 
         GameGUI.gameOver(false);
      }
   }

   //possibility for losing 
   public static void winLose( boolean check){

      if (check == true){
         GameGUI.gameOver(true);

      } else if (currentRow > MAX_COLS-1){ 
         GameGUI.gameOver(false);
      }
   }
}
   


