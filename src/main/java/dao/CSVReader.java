package dao;

import java.io. * ;
import java.util.Scanner;

public class CSVReader {
    public static void read(File file) throws Exception {
        Scanner sc = new Scanner(file);

        //parsing a CSV file into the constructor of Scanner class
        sc.useDelimiter(",");

        //setting comma as delimiter pattern
        while (sc.hasNext()) {
            System.out.print(sc.next());
        }

        sc.close();
        //closes the scanner
    }
    public static void read_by_path(String path) throws Exception {
        Scanner sc = new Scanner(new File(path));

        //parsing a CSV file into the constructor of Scanner class
        sc.useDelimiter(",");

        //setting comma as delimiter pattern
        while (sc.hasNext()) {
            System.out.print(sc.next());
        }

        sc.close();
        //closes the scanner
    }
}