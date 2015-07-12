/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supern4ut.redes2.archivos;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author supernaut
 */
public class ArchivoCli {
    static final short PTO = 9001;

    public static void main(String[] args) {
        Socket cl;
        BufferedInputStream buffIn;
        ObjectOutputStream objOut;
        byte[] buff;
        int result, leidos;
        JFileChooser chooser;
        File[] files;
            
        try {
            cl = new Socket(InetAddress.getByName("127.0.0.1"), PTO);
            System.out.println("cliente conectado...\nelija archivos...\n");
            chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            result = chooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION) {
                files = chooser.getSelectedFiles();
                objOut = new ObjectOutputStream(cl.getOutputStream());
                objOut.writeInt(files.length);
                for(File file : files) {
                    objOut.writeObject(file.getName());
                    objOut.writeLong(file.length());
                    objOut.flush();
                    buffIn = new BufferedInputStream(new FileInputStream(file));
                    cl.setSoTimeout(3000);
                    buff = new byte[1024];
                    leidos = 0;
                    while((result = buffIn.read(buff, 0, buff.length)) != -1) {
                        objOut.write(buff, 0, result);
                        leidos += result;
                        System.out.println(leidos*100.0/file.length() +
                                "% de archivo " + file.getName() + "\n");
                        objOut.flush();
                    }
                }
            } else {
                System.out.println("saliendo\n");
            }
            cl.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            Logger.getLogger(ArchivoCli.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
}
