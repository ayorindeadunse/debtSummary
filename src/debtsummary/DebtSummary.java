/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package debtsummary;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author ayorindeadunse
 */
public class DebtSummary {

    /**
     * @param args the command line arguments
     */
    private static BufferedReader bufferedReader;
    private static String file;
    private static Map<String, Double> summarizedData;

    public static void main(String[] args) throws IOException {
        // TODO code application logic here      

        List<DebtInformation> debtInfoDetails = new ArrayList<>();

        try {

            //Navigate to the full path of the folder and supply it as an input parameter
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("_____________________________");
            System.out.println("Initializing Debt Summary App");
            System.out.println("______________________________");
            System.out.println("Get the full path to where your csv file is (for example: /Users/ayorindeadunse/Downloads/yourfilename.csv for a linux machine.)");
            System.out.println("For a Windows machine the path could be in c://DirectoryName/filename.csv");
            System.out.println("_________________________________________________");
            System.out.println("Type the path to your csv file as indicated above:");

            file = bufferedReader.readLine().trim();
            //Read csv file and save output to string array readfileinfo
            List<String[]> readfileinfo = debtsummary.utilities.CSV.read(file);

            //iterate to get data from csv file and break into tokens
            readfileinfo.stream().map((info) -> {
                //Store details of the debtor and creditor and concatenate for better management
                DebtInformation debtInfo = new DebtInformation();
                debtInfo.setName(info[0] + "," + info[1]);
                debtInfo.setAmount(Double.parseDouble(info[2]));//store the amount being owed (which is the third parameter)
                return debtInfo;
            }).forEachOrdered((debtInfo) -> {
                debtInfoDetails.add(debtInfo);
            });         

            //Summarize the data from duplicates and otherwise with the total sum owing
            summarizedData = debtInfoDetails.stream().collect(
                    Collectors.groupingBy(DebtInformation::getName, Collectors.summingDouble(DebtInformation::getAmount)));

        } catch (IOException | NumberFormatException n) {
            System.out.println("An Error has occured. Summary data not done!");
            System.out.println("____________________________________________");
            Logger.getLogger(DebtSummary.class.getName()).log(Level.SEVERE, null, n);
            System.exit(0);
        } 
            
            
            Instant instant = Instant.now();
            long timeStampMillis = instant.toEpochMilli();

            //append the current timestamp to filename to ensure it's uniqueness each time.
            String outputfile = "finalsummaryoutput"+timeStampMillis+".csv";
            try {
                System.out.println("Summarizing data with the total amount owed...");
                bufferedReader.close();
                Iterator<String> iterator = summarizedData.keySet().iterator();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = summarizedData.get(key).toString();
                    System.out.println(key + "," + value);

                    //Write to csv file
                    String lineseparator = System.getProperty("line.separator");

                    try ( Writer writer = new FileWriter(outputfile)) {
                        for (Map.Entry<String, Double> entry : summarizedData.entrySet()) {
                            writer.append(entry.getKey())
                                    .append(',')
                                    .append(entry.getValue().toString())
                                    .append(lineseparator);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(DebtSummary.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("There was an error writing to the output file.");
                        System.exit(0);
                    }
                }
                System.out.println("-----------------------------------------------------------------");
                System.out.println("Output csv file has been generated in the application dist directory.");
                System.out.println("The name of the file is : " + outputfile);
                System.out.println("-----------------------------------------------------------------");
            } catch (IOException e) {
                Logger.getLogger(DebtSummary.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("There was an error summarizing the debt data. ");
                System.exit(0);
            }

        
    }
}
