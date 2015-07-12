/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supern4ut.redes2.archivos;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author supernaut
 */
public class ArchivoServ {
    static final short PTO = 9001;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Socket cli;
        ServerSocket serv;
        File[] files;
        byte[] buff;
        int leidos, total;
        long size;
        ObjectInputStream objIn;
        BufferedOutputStream buffOut;
        
        try {
            serv = new ServerSocket(PTO);
            System.out.println("servidor listo\n");
            while(true) {
                cli = serv.accept();
                System.out.println("cliente conectado en " +
                        cli.getInetAddress() + ":" + cli.getPort() + "\n");
                objIn = new ObjectInputStream(cli.getInputStream());
                total = objIn.readInt();
                System.out.println(total + " archivo(s) a copiar\n");
                files = new File[total];
                for(File file : files) {
                    Thread.sleep(10);
                    file = new File("descargas/" + (String) objIn.readObject());
                    buffOut = new BufferedOutputStream(new FileOutputStream(file));
                    size = objIn.readLong();
                    buff = new byte[1024];
                    leidos = objIn.read(buff, 0, buff.length);
                    do {
                        size -= leidos;
                        buffOut.write(buff, 0, leidos);
                        leidos = objIn.read(buff, 0, buff.length);
                    } while(size > 0 && leidos != -1);
                    buffOut.flush();
                    System.out.println("archivo " + file.getName() + 
                            " copiado\n");
                    // objIn.close();
                }
            }
        } catch(Exception ex) {
            Logger.getLogger(ArchivoServ.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
    
}
