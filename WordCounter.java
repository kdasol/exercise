/**
 * Created by Dasol Kwon
 */

import java.io.FileReader;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.*;


// Deadline Aug 17 before the class email to jcrogel@ucla.edu

public class WordCounter {

    public Hashtable count_words(String contents){

        Hashtable<String, Integer> count = new Hashtable<>();
        StringTokenizer st = new StringTokenizer(contents);
        String word;

        while(st.hasMoreTokens()){

            word = st.nextToken();

            if(count.containsKey(word)){
                Integer i = count.get(word);
                count.put(word, i+1);
            }
            else {
                count.put(word, 1);
                //count.put(word, new Integer(1));
            }
        }
        return count;
    }

    // TODO: Return top 20 words on the songs
    public LinkedList top20(Hashtable count)
    {

        // A sortedValue for sorted string values
        LinkedList<String> sortedList = new LinkedList<>();

        // Another Hashtable for cleaned-up keys and values
        Hashtable<String, Integer> newCount = new Hashtable<>();

        /* STEP 1. Clean up the table and update the cleaned-up elements in the newCount Hashtable */

        Set<String> keys = count.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // String of Key
        String word;

        while (itr.hasNext()) {

            word = itr.next();
            Integer i = (Integer) count.get(word);

            //Remove special characters
            if (word.contains("\"")) {
                word = word.replaceAll("\"", "");
            }
            if (word.contains(",")) {
                word = word.replaceAll(",", "");
            }
            if (word.contains(";")) {
                word = word.replaceAll(";", "");
            }
            //Convert to lower cases
            word = word.toLowerCase();

            // Add cleaned-up elements in the newly created newCount Hashtable

            //  Here, have to deal with the key that is already in the newCount, need to increment the number of words not overwrite it.
            // for example, after cleaning up the keys, the result of word "Made" is equals to the word "made"

            // If the key is already in the table
            if(newCount.containsKey(word)){

                Integer j = newCount.get(word);
                j += i;
                //newCount.put(word, new Integer(j.intValue()));
                newCount.put(word, j);
            }

            // no duplicates in the newCount
            else{
                //newCount.put(word,  new Integer(i.intValue()));
                newCount.put(word, i);
            }
        }

        //System.out.println(newCount);
        /* StEP 2. Make a copy of the Hash table's Values then sort it.
                   And update a list of values with NO duplicates */

        List<Integer> sortedValue = new LinkedList<>(newCount.values());
        Collections.sort(sortedValue);
        Collections.reverse(sortedValue);                           // reverse the list for descending order

        List<Integer> updatedSortedValue = new LinkedList<>();      // list of sorted values with no values duplicated

        // remove duplicates in the sortedValue list
        for(int i = 0; i < sortedValue.size(); i++){
            int value = sortedValue.get(i);                         // value in the list that might contain the duplicated values

            // if nothing has been added to the list
            if (updatedSortedValue.isEmpty()) {
                updatedSortedValue.add(sortedValue.get(i));
            }
            else{
                int prevValue = sortedValue.get(i - 1);
                if (value != prevValue) {                           // if no value of the same has been added to the list
                    updatedSortedValue.add(sortedValue.get(i));     // update the list without the duplicates
                }
            }
        }

        /* STEP 3. Add top20 keys to the sortedValue as the form of {key + value} where the highest element is at the first
                   Retrieve the values from newCount Hashtable by iterating through the values in the sorted list
                   and add the corresponded (key, value) pairs(20 pairs) in the sortedList. */

        Set<String> updatedKeys = newCount.keySet();
        String updatedWord;                                     // Keys in newCount
        int numbOfElement = 0;                                  // Number of element stored in the sortedList

        for(int z = 0; z < updatedSortedValue.size(); z++) {
            Integer largest = updatedSortedValue.get(z);        // Value in the list of sorted values of table

            //  Obtaining iterator of keys over set entries
            Iterator<String> updatedItr = updatedKeys.iterator();

            while (updatedItr.hasNext()) {

                updatedWord = updatedItr.next();
                Integer i = newCount.get(updatedWord);          // Integer Value of current Key

                if (largest.equals(i) && numbOfElement < 20)  {

                    if(numbOfElement == 19){
                        // no comma should be added at the end if it is the last element in the list
                        sortedList.add("{" + updatedWord + " = " + i + "}");
                    }
                    else {
                        sortedList.add("{" + updatedWord + " = " + i + "}, ");
                    }
                    numbOfElement++;                            // update the number of element added
                }
            }
        }

        //System.out.println(newCount);

        // return a list of top20 items
        return sortedList;

    }


    public static void main(String args[]){
        try{
            String contents = "";
            Scanner in = new Scanner(new FileReader("src/ADayInTheLife.txt"));

            while(in.hasNextLine())
            {
                contents += in.nextLine() + "\n";
            }

            WordCounter wc = new WordCounter();
            Hashtable count = wc.count_words(contents);

            LinkedList top20 = wc.top20(count);

            /// / Each element of the array can be a Hashtable with only one element sorted where the highest element is at the top
            // For example:  [{the=20}, {a=10}, {boy=10}, {news=5}.....]

            System.out.print("Top 20s: [ ");
            for (Object item: top20)
            {
                System.out.print(item);
            }

            System.out.print(" ]");
            System.out.println();
            System.out.println("All elements in the original table: ");
            System.out.println(count);

        } catch (Exception e){
            System.err.println("Error " + e.getMessage());
        }
    }

}

