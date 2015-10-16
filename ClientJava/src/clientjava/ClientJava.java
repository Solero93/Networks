/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientjava;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.JFrame;
//import javax.swing.WindowConstants;

/**
 *
 * @author Manel
 */
public class ClientJava {

    private static Socket socket;
    /**
     * lectura de dades del socket
     */
    private static InputStream dataInput;
    /**
     * escritura de dades socket
     */
    private static OutputStream dataOutput;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //Scanner in;
        try {
            //connectem amb el servidor...
            System.out.println("Introduce la dirección IP del servidor: ");
            String dirip = sc.nextLine();
            System.out.println("Introduce el puerto del servidor: ");
            int puerto = Integer.parseInt(sc.nextLine());
            socket = new Socket(dirip, puerto);
            //socket = new Socket("time-A.timefreq.bldrdoc.gov", 13);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            dataOutput = socket.getOutputStream();
            //dataInput = socket.getInputStream();
            //in = new Scanner(dataInput);

            
            PrintWriter out = new PrintWriter(dataOutput, true);
            out.println("hola sever!!!");
            while (true) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            dataInput = socket.getInputStream();
                            Scanner in = new Scanner(dataInput);

                            while (true) {
                                String Line = in.nextLine();
                                System.out.println(Line);
                            }
                        } catch (Exception e) {
                            System.out.println("Error");
                        }
                    }
                }).start();

                String dades = sc.nextLine();
                out.println("client says > " + dades);

            }
        } catch (IOException ex) {
            Logger.getLogger(ClientJava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
