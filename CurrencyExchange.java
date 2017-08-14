import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.Scanner;


public class CurrencyExchange {

    private static final String FILENAME = "src/forex.csv";

    public static Float Convert(String base, String counter, float amount){

        String line;
        String csvSplitBy = ",";

        // List of pairs (currency symbol, rate)
        Hashtable<String, Float> currencyTable = new Hashtable<>();


        try {

            // open input file
            BufferedReader br = new BufferedReader(new FileReader(FILENAME));

            // iterate over the file reading a line at a time
            while ((line = br.readLine()) != null) {

                // Add each line of values in the file into the array of strings by using comma as separator in the csv file
                String[] currency = line.split(csvSplitBy);

                // Cast the value of currency rate to be double
                float rate = Float.parseFloat(currency[2]);

                // Put items in the table
                currencyTable.put(currency[0], rate);

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        System.out.println(currencyTable);

        // currency rate fpr base and destination country
        float baseRate = 0;
        float counterRate = 0;

        //Get keys of base currency table
        Set<String> keys = currencyTable.keySet();
        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Iterate over the currency table and find the symbols and their corresponded rate
        while(itr.hasNext()) {
            // Getting a current Key
            String str = itr.next();

            if(str.equals(base)) {
                baseRate = currencyTable.get(base);

                // baseRate has been set first
                if(counterRate == 0) {
                    // process the 1st calculation
                    amount = amount / baseRate;
                }
                // counterRate has been set before baseRate
                else{
                    // process the whole calculations
                    amount = (amount / baseRate) * counterRate;
                }
            }
            if(str.equals(counter)){
                counterRate = currencyTable.get(counter);

                // if baeRate has been set already
                if(baseRate != 0) {
                    // process the 2nd calculation
                    amount = amount * counterRate;
                }
                // ele, since baseRate has not been set, continue to check for the next symbol
                // (because the 1st calculation should be processed before the 2nd one.)
            }
        }


        // Why with the right below code, I get null for the counterRate
//        baseRate = currencyTable.get(base);
//        counterRate = currencyTable.get(counter);
//
//        amount = (amount / baseRate) * counterRate;
//

//      while(itr.hasNext()) {
//            String str = itr.next();
//            if(str.equals(base)) {
//                baseRate = currencyTable.get(base);
//                amount = amount / counterRate;
//            }
//        }


//        while(itr.hasNext()) {
//            String str = itr.next();
//            if(str.equals(counter)) {
//                counterRate = currencyTable.get(counter);
//                amount = amount * counterRate;
//            }
//        }


        return amount;
    }

    public static void main(String args[]){

        // Prompt the user inputs
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the base currency: ");
        String base = scan.nextLine();

        System.out.println("Please enter the counter currency: ");
        String counter = scan.nextLine();

        System.out.println("Please enter your amount for conversion: ");
        float amount = scan.nextFloat();

        try{
            float convertedValue = Convert(base, counter, amount);

            String formatAmount = String.format("%.0f", amount);
            String result = String.format("%.2f", convertedValue);

            System.out.println("...");
            System.out.println(formatAmount + " " + base +  " = " + result + " " + counter);

        }
        catch(Exception e){
            e.getMessage();
        }
        finally{
            System.out.println("End of conversion");
        }

    }

}
