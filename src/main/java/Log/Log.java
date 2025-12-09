/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Log;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author gabri
 */
public class Log {

    private static final String FILE = "Log.txt";

    public static void logInTxt(String linea) {
        try (FileWriter writer = new FileWriter(FILE, true)) {
            writer.write(linea + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
