/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorjava;
 
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
 
/**
 *
 * @author Manel Port utilitzat: 8189
 */
public class ServidorJava {
 
    int client_id;
    volatile Queue<String> broadcast;
    volatile List<Socket> clientList;
 
    public static void main(String[] args) {
        ServidorJava servidor = new ServidorJava();
        servidor.start();
    }
 
    public ServidorJava() {
        broadcast = new LinkedList<String>();
        clientList = new ArrayList<Socket>();
        client_id = 0;
    }
 
    public void start() {
        try {
            ServerSocket socketServidor = new ServerSocket(8189);
            System.out.println("Server Started.");
 
            //new Thread(new BroadcastRunnable()).start();
            while (true) {
                Socket clientSocket = socketServidor.accept();
                clientList.add(clientSocket);
                // Start a new thread
                new Thread(new ChatServerRunnable(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    class BroadcastRunnable implements Runnable {
 
        OutputStream out;
        PrintWriter printer;
 
        public void run() {
            System.out.println("Broadcast Thread Started.");
 
            while (true) {
 
                if (!broadcast.isEmpty()) {
                    System.out.println("broadcast not emptry");
                }
 
                try {
                    for (Socket sock : clientList) {
                        out = sock.getOutputStream();
                        printer = new PrintWriter(out, true);
 
                        for (String msg : broadcast) {
                            printer.println(msg);
                        }
 
                    }
                    // Rest for a while
                    Thread.sleep(100);
                    broadcast.clear();
 
                    // Catch failures
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
 
            }
        }
    }
 
    class ChatServerRunnable implements Runnable {
 
        InputStream dataInput;
        OutputStream dataOutput;
        PrintWriter printer;
        String msg;
 
        Scanner s = new Scanner(System.in);
        int clientID = 0;
        String name;
 
        //Contructor del hilo del servidor con el cliente
        public ChatServerRunnable(Socket socket) {
            sck = socket;
        }
 
        public void run() {
            try {
                clientID = ++client_id;
                name = "persona"+clientID;
                
                System.out.println("Client connected with id" + clientID);
 
                //para leer
                dataInput = sck.getInputStream();
                Scanner in = new Scanner(dataInput, "UTF-8");
 
                while (true) {
                    // Update loop
                    if (in.hasNextLine()) {
                        msg = in.nextLine();
 
                        for (Socket sock : clientList) {
                            if (sock != this.sck) {
                                dataOutput = sock.getOutputStream();
                                printer = new PrintWriter(dataOutput, true);
                                printer.println(name+" "+msg);
                            }
                        }
                    }
                    Thread.sleep(100);
                }
 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private Socket sck;
 
    }
}