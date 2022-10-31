package dao;

import java.io. * ;
import java.util.Scanner;

public class CSVReader {
    public static void read_file(String filename) throws Exception {
        Scanner sc = new Scanner(new File(filename));

        //parsing a CSV file into the constructor of Scanner class
        sc.useDelimiter(";");

        //setting comma as delimiter pattern
        while (sc.hasNext()) {
            System.out.print(sc.next());
        }

        sc.close();
        //closes the scanner
    }
}