

import java.util.*;

/**
 * 
 * Simulate the encryption of messages that was performed by the World War
 * II-era German Enigma cipher machine.
 * <li><a href="http://en.wikipedia.org/wiki/Enigma_machine" target="wiki">
 * http://en.wikipedia.org/wiki/Enigma_machine</a></li>
 *<li><a href="https://www.youtube.com/watch?v=G2_Q9FoD-oQ" target="youtube">
 *https://www.youtube.com/watch?v=G2_Q9FoD-oQ</a></li>
 */

public class Enigma {

// CAUTION: YOU MAY NOT DECLARE STATIC FIELDS IN class Enigma
// All values must be passed (as described) to and from methods
// as this is a major objective of this program assignment.

/**
* 
* User enter some initial configuration information and is then prompted
* for the lines of text to be encrypted.
* Each line is encrypted according to the rotor configuration. The encoded
* line is displayed to the user.
*@param args UNUSED
*/

public static void main(String[] args) {

// Create scanner "stdin" for user input.

Scanner stdin = new Scanner(System.in);

// Declare Variables

String input = ""; // user input read by scanner stdin

// reflectorRotor: This should be a one-dimensional array of integers
// that represents the reflector rotor. You can create it by using
// the convertRotor() method in conjunction with the
// RotorConstants.REFLECTOR constant
// Declare reflector
int[] reflectorRotor = convertRotor(RotorConstants.REFLECTOR);

// Welcome Message

System.out.println("Willkommen auf der Enigma-Maschine");

System.out.println("Please enter a Rotor Configuration.");
System.out
.println("This must be a list of numbers in the range from 0 to "
+ (RotorConstants.ROTORS.length - 1)

+ ", separated by spaces.");
System.out.println("Note that rotor 0 is the identity rotor.");
System.out.println();
// Read input to parse and store the rotors in use.
input = stdin.nextLine();
int[] rotorsInUse = parseRotorIndices(input);
// Select notch positions based on the rotors in use
int[][] rotorNotchPositions = getRotorNotches(rotorsInUse);

int[] rotorOffsets = new int[RotorConstants.ROTOR_LENGTH];

// Call parse rotor indicies to use input to create rotorsInUse
int[][] selectedRotorsAsNumbers = setUpRotors(rotorsInUse);

// Main method main loop to encode multiple lines 
// with the same rotor configuration.
// The program doesn't need a command to terminate.
boolean done = false;
while (!done) {
//prompt user
System.out.println("Enter lines of text to be encoded:");
System.out.println();

//receive input, one characters at a time and encode them.
String userInput = stdin.nextLine();
userInput = userInput.toUpperCase();
System.out.println("Encoded result: ");
for (int i = 0; i < userInput.length(); i++) {
if (userInput.charAt(i) >= 'A' && userInput.charAt(i) <= 'Z') {
System.out.print(encode(selectedRotorsAsNumbers,
reflectorRotor, userInput.charAt(i)));
advance(selectedRotorsAsNumbers, rotorOffsets,
rotorNotchPositions);
} else {
System.out.print(userInput.charAt(i));
}
}
System.out.println();
}
// Close scanner to avoid resource leak.
stdin.close();
/*
* Hint: This is where you should put your welcome messages, the  
* configuration prompt, and most importantly, your main program loop.
* This is also the place where you should declare and use the  
* "Suggested Data Structures" from the P2 Specifications Page.
*/

/*
* Caution: When calling other methods, you must follow the requirements
* of each method as it is defined. For example, never call encode()
* with a non-upper-case character because the encode() method requires
* upper-case characters.
*/
}

/**
* 
* Rotates (advances) a single rotor by one position. This is done by
* removing the first value of the array, shifting all the other values
* towards the beginning of the array, and placing the first value back into
* the array as the last value.
* 
* @param rotor
*            The rotor that must be rotated (or shifted).
*/

public static void rotate(int[] rotor) {
//store the first value before it is overwritten.
int storeFirstValue = rotor[0];
// replace each value with the value one greater in the array until the last value
for (int i = 0; i < rotor.length; i++) {

if (i < (rotor.length - 1)) {
rotor[i] = rotor[i + 1];
}
// replace the last array value with the stored first value
else {
rotor[(rotor.length) - 1] = storeFirstValue;
}
}
}

/**
* 
* Parses (interprets) the rotor configuration string of integer values and
* returns an integer array of those values.
* 
* Example: If the input string is:
* &quot;3 7 2&quot;
* The method returns an int array containing three indices: { 3, 7, 2 }.
* <h6>Parameter Validation</h6> <blockquote>
* If any of the specified indices is not a valid index into the
* {@code RotorConstants.ROTORS} array, the method prints:
* Invalid rotor. You must enter an integer between 0 and 8.
* to {@code System.out} and then quits the program by calling
* {@code System.exit(-1)}.
* The same rotor may not be used twice. If the user tries to specify the
* same rotor multiple more than once, this method prints:
* You cannot use the same rotor twice.
* to {@code System.out} and then quits the program by calling
* {@code System.exit(-1)}. </blockquote>
* @param rotorIndicesLine
*            Text containing rotor index values separated by spaces.
* @return Array of rotor index values.
*/

public static int[] parseRotorIndices(String rotorIndicesLine) {

String[] entries = rotorIndicesLine.split(" ");
//Declare variable N to shorten code
int N = RotorConstants.ROTORS.length ; 
//Create array to store the rotor numbers
int[] rotorIndexValues = new int[entries.length];

if (rotorIndicesLine.length() <= 0) {
System.out.println("You must specify at least one rotor.");
System.exit(-1);
} else {
// Scanner to grab the values from the rotorIndiciesLine
Scanner scan = new Scanner(rotorIndicesLine);
//Loop to place each rotor value in the array
for (int i = 0; i < entries.length; i++) {
if (scan.hasNextInt()) {
rotorIndexValues[i] = scan.nextInt();
if (rotorIndexValues[i] < 0
|| rotorIndexValues[i] > (N) - 1) {
System.out.println("Invalid rotor. You must "
+ "enter an integer between 0 and "
+ (N - 1) + ".");

System.exit(-1);
}
} else {
System.out.println("Invalid rotor. You must "
+ "enter an integer between 0 and "
+ (N - 1) + ".");
System.exit(-1);
}
// determine whether the user tried to use the same rotor twice
for (int j = 0; j < i; j++) {
if (rotorIndexValues[i] == rotorIndexValues[j]) {
System.out
.println
("You cannot use the same rotor twice.");
System.exit(-1);
}
}

}
//close the scanner to avoid resource leak
scan.close();
}

// CAUTION: Do not hard code values, we can (and will) change the
// RotorConstants class. So, you must use those constant names
// and not the values themselves to ensure your code will work
// with different versions of those constants.
return rotorIndexValues;

}

/**
* 
* Creates and returns array of rotor ciphers, based on rotor indices.
* The array of rotor ciphers returned is a 2D array, where each "row" of
* the array represents a given rotor's rotor cipher in integral form.
* The number of rows is equal to the number of rotors in use, as specified
* by the length of rotorIndices parameter.
* @param rotorIndices
*            The indices of the rotors to use. Each value in this array
*            should be a valid index into the {@code RotorConstants.ROTORS}
*            array.
* @return The array of rotors, each of which is itself an array of ints
*         copied from {@code RotorConstants.ROTORS}.
*/

public static int[][] setUpRotors(int[] rotorIndices) {

	//create array to store the rotor numbers
int[][] selectedRotorConstants = new int
[rotorIndices.length][RotorConstants.ROTOR_LENGTH];

// rows
for (int i = 0; i < rotorIndices.length; i++) {

int[] integerOfRotor = 
convertRotor(RotorConstants.ROTORS[rotorIndices[i]]);

// columns
for (int j = 0; j < RotorConstants.ROTOR_LENGTH; j++) {

selectedRotorConstants[i][j] = integerOfRotor[j];

}

// Hint: Access the rotor ciphers contained in RotorConstants,
// and convert them into integral form by calling convertRotor().
}
return selectedRotorConstants;

}

// take appropriate rotor constant array and use as rows 1, 2, and 3.
// Hint: Access the rotor ciphers contained in RotorConstants,
// and convert them into integral form by calling convertRotor().

/**
* Creates and returns a 2D array containing the notch positions for each
* rotor being used.
* 
* Each "row" of the array represents the notch positions of a single rotor.
* A rotor may have more than one notch position, so each "row" will contain
* one or more integers. There will be multiple rows, if multiple rotors are
* in use.
* 
* Note that this array may be jagged, since different rotors have different
* numbers of notch positions.
* 
* Example:
* Input: [2, 1, 3]
* Output: a 2D Array that would look something like this:
* [[Array of notch positions of Rotor II],
* [Array of notch positions of Rotor I] ,
* [Array of notch positions of Rotor III]]
* 
* @param rotorIndices
* 
*            Indices of the rotors. Each value in this array should be a
*            valid index into the {@code RotorConstants.ROTORS} array.
* 
* @return An array containing the notch positions for each selected rotor.
*/

public static int[][] getRotorNotches(int[] rotorIndices) {
//create array to store the rotor notch positions
int[][] notchPositions = new int[rotorIndices.length][];
//Loop to create rows of the corrrect length
for (int row = 0; row < rotorIndices.length; row++) {
notchPositions[row] = 
new int[RotorConstants.NOTCHES[rotorIndices[row]].length];
//loop to place the nothc values in the array
for (int numberOfNotches = 0; numberOfNotches < 
notchPositions[row].length; numberOfNotches++) {
notchPositions[row][numberOfNotches] = 
RotorConstants.NOTCHES[rotorIndices[row]][numberOfNotches];
}
}

return notchPositions;
}

/**
* 
* Converts a rotor cipher from its textual representation into an integer-
* based rotor cipher. Each int would be in the range [0, 25], representing
* the alphabetical index of the corresponding character.
* 
* The mapping of letters to integers works as follows: <br />
* Each letter should be converted into its alphabetical index. That is, 'A'
* maps to 0, 'B' maps to 1, etc. until 'Z', which maps to 25.
* 
* Example:
* Input String: EKMFLGDQVZNTOWYHXUSPAIBRCJ
* Output Array: [4 10 12 5 11 6 3 16 21 25 13 19 14 22 24 7 23 20 18 15 0
* 8 1 17 2 9]
* @param rotorText
* 
*            Text representation of the rotor. This will be like the
*            Strings in {@code RotorConstants.ROTORS}; that is, it will be
*            a String containing all 26 upper-case letters.
* 
* @return array of values between 0 and 25, inclusive that represents the
*         integer index value of each character.
*/

public static int[] convertRotor(String rotorText) {
// create an array to store the converted values
int[] convertedRotors = new int[rotorText.length()];
//one by one convert the upper-case chars to numbers
for (int i = 0; i < rotorText.length(); i++) {

convertedRotors[i] = (((int) rotorText.charAt(i)) - 65);

}

return convertedRotors;

}

/**
* 
* Encodes a single uppercase character according to the current state of
* the Enigma encoding rotors.
* 
* To do this:
* <li>Convert input character to its alphabetical index, e.g. 'A' would be
* 0, 'B' would be 1, etc.</li>
* 
* <li>Run the letter through the rotors in forward order.</li>
* To "run character through rotors", use your converted-letter as the index
* into the desired row of the rotors array. Then, apply the reflector, and
* run the letter through the rotors again, but in reverse. Encoding in
* reverse not only implies that the rotor-order is to be reversed. It also
* means that each cipher is applied in reverse.
* 
* An example:
* Cipher (input): EKMFLGDQVZNTOWYHXUSPAIBRCJ
* Alphabet (output): ABCDEFGHIJKLMNOPQRSTUVWXYZ
* 
* While encoding in reverse, 'E' would get encoded as 'A', 'K' as 'B', etc.
* (In the forward direction, 'E' would get encoded as 'L')
* 
* Finally, convert your letter integer index value back to a traditional
* character value.
* 
* @param rotors
*            Current configuration of rotor ciphers, each in integral rotor
*            cipher form. Each "row" of this array represents a rotor
*            cipher.
* 
* @param reflector
*            The special reflector rotor in integral rotor cipher form.
* 
* @param input
*            The character to be encoded. Must be an upper-case letter. DO
*            NOT CONVERT TO UPPERCASE HERE.
* 
* @return The result of encoding the input character. ALL encoded
*         characters are upper-case.
*/

public static char encode(int[][] rotors, int[] reflector, char input) {
//declare convertedvalue to store the char as it moves through the rotors
int convertedValue = (int) input - 65;

// rotors forward
for (int i = 0; i < rotors.length; i++) {

convertedValue = rotors[i][convertedValue];
}
// apply reflector
convertedValue = reflector[convertedValue];

// rotors in reverse
boolean keepSearching = true;
for (int row = (rotors.length - 1); row >= 0; row--) {
keepSearching = true;
for (int col = 0; col < rotors[0].length &&
keepSearching == true; col++) {
if (rotors[row][col] == convertedValue) {
convertedValue = col;
keepSearching = false;
}
}
}
char characterConvertedValue = (char) (convertedValue + 65);

return characterConvertedValue;


}

/**
* Advances the rotors. (Always advances rotor at index 0. May also advance
* other rotors depending upon notches that are reached.)
* 
* <li>Advancement takes place, starting at rotor at index 0.</li>
* <li>The 0th rotor is rotated</li>
* <li>Update the corresponding offset in <tt>rotorOffsets.</tt></li>
* <li>Check to see if the current offset matches a notch (meaning that a
* notch has been reached).</li>
* <li>If a notch has been reached:
* <li>The next rotor must also be advanced</li>
* <li>And have its rotorOffset updated.</li>
* <li>And if a notch is reached, the next rotor is advanced.</li>
* <li>Otherwise, notch was not reached, so no further rotors are advanced.
* 
* Advancement halts when a <tt>rotorOffset</tt> is updated and it does not
* reach a notch for that rotor.
* 
* Note: The reflector never advances, it always stays stationary.
* 
* @param rotors
* 
*            The array of rotor ciphers in their current configuration. The
*            rotor at index 0 is the first rotor to be considered for
*            advancement. It will always rotate exactly once. The remaining
*            rotors may advance when notches on earlier rotors are reach.
*            Later rotors will not not advance as often unless there is a
*            notch at each index. (Tip: We try such a configuration during
*            testing). The data in this array will be updated to show the
*            rotors' new positions.
* 
* @param rotorOffsets
* 
*            The current offsets by which the rotors have been rotated.
*            These keep track of how far each rotor has rotated since the
*            beginning of the program. The offset at index i will
*            correspond to rotor at index 0 of rotors. e.g. offset 0
*            pertains to the 0th rotor cipher in rotors. Will be updated
*            in-place to reflect the new offsets after advancing. The
*            offsets are compared to notches to know when next rotor must
*            also be advanced.
* 
* @param rotorNotches
*            The positions of the notches on each of the rotors. Each row
*            of this array represents the notches of a certain rotor. The
*            ith row will correspond to the notches of the ith rotor cipher
*            in rotors. Only when a rotor advances to its notched position
*            does the next rotor in the chain advance.
*/

public static void advance(int[][] rotors, int[] rotorOffsets,

int[][] rotorNotches) {
//boolean to stop proceeding when the current rotor isn't on a notch
boolean keepAdvance = true;
// int storing the rotor currently being rotated and checked
int rotorPosition = 0;
// Loop rotates rotors until the current one isn't at a notch
while (keepAdvance && rotorPosition < rotors.length) {
keepAdvance = false;
rotate(rotors[rotorPosition]);
rotorOffsets[rotorPosition]++;

// wrap the rotorOffset at the final possible notch position
if (rotorOffsets[rotorPosition] == RotorConstants.ROTOR_LENGTH) {
rotorOffsets[rotorPosition] = 0;
}
// check if the current rotor is on a notch
for (int positionToCheck = 0; positionToCheck < 
(rotorNotches[rotorPosition].length); positionToCheck++) {
if (rotorOffsets[rotorPosition] == 
rotorNotches[rotorPosition][positionToCheck]) {
keepAdvance = true;
rotorPosition++;
break;
} else {
keepAdvance = false;
}

}
}

/*
* 
* Hint: call rotate() to rotate a rotor. Check offset against notches:
* if a notch is reached, advance the next rotor and continue until
* advance and offset does not indicated that a notch has been reached.
*/

}

} // end of Enigma class
