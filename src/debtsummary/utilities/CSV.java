/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package debtsummary.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ayorindeadunse
 */
public class CSV {

    //This method will read data from a csv file
    public static List<String[]> read(String file) {
        List<String[]> data = new LinkedList<>();
        String datarow;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((datarow = br.readLine()) != null) {
                String[] datarecords = datarow.split(",");
                data.add(datarecords);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSV.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found. Please re-run the application, specify the correct directory and try again. Thank you!");
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(CSV.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found. Please re-run the application, specify the correct directory and try again. Thank you!");
            System.exit(0);
        }
        return data;
    }
}
