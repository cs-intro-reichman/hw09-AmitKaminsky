import java.util.HashMap;
import java.util.Random;

public class LanguageModel {
    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		// Your code goes here
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public List calculateProbabilities(List probs) {				
        List updatedList = new List();
        int orgSize = probs.getSize();
        for (int i = 0; i < orgSize; i++) {
            updatedList.update(probs.get(i).chr);
        }
        int numberOfChars = updatedList.getSize();
        CharData firstChar = updatedList.getFirst();
        firstChar.p = (double) firstChar.count / orgSize;
        firstChar.cp = firstChar.p; 
        for (int i = 1; i < numberOfChars; i++) {
            CharData currectChar = updatedList.get(i);
            CharData prevChar = updatedList.get(i-1);
            double x = (double) currectChar.count / orgSize;
            currectChar.p = x;
            currectChar.cp = prevChar.cp + x;
        }
        return updatedList;
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
		double r = Math.random();
        for (int i = 0; i < probs.getSize(); i++) {
            CharData currentChar = probs.get(i);
            if (r < currentChar.cp) {
                return currentChar.chr;
            }
        }
        return probs.get(probs.getSize() - 1).chr;
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		return "";
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        LanguageModel languageModel = new LanguageModel(3);
        List newList = new List();
        String committee = "committttee_";
        for (int i = 0; i < committee.length(); i++) {
            newList.addFirst(committee.charAt(i));
        }
        System.out.println(newList);
        newList = languageModel.calculateProbabilities(newList);
        System.out.println(newList);
        int count = 0;
        int N = 10000;
        for (int i = 0; i < N; i++) {
            char c = languageModel.getRandomChar(newList);
            if (c == 't') count++;
        }
        System.out.println((double) count / N);
    }
}
