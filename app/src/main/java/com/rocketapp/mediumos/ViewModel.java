package com.rocketapp.mediumos;
import android.os.StrictMode;

import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLEncoder;

public class ViewModel{
    static String urlMain;
    public static boolean getAccess(String url){
        urlMain=url;
        try {
            Socket socket = new Socket(url.substring(0, url.length()-5), Integer.parseInt(url.substring(url.length()-4)));
            socket.setSoTimeout(5*1000);
            DataOutputStream out    = new DataOutputStream(socket.getOutputStream());
            PrintWriter writer = new PrintWriter(out, true);

            writer.println("GET " + "/" + " HTTP/1.1");
            writer.println("Host: " + url.substring(0, url.length()-5));
            writer.println("User-Agent: MediumOS");
            writer.println("Accept: text/html");
            writer.println("Accept-Language: en-US");
            writer.println("Connection: close");
            writer.println();

            DataInputStream in = new DataInputStream(socket.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line=reader.readLine())!=null){
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        return false;
        }
        return true;
    }
    public static void sendClipBoard(String clip){
        try {

            String clippin = URLEncoder.encode("Clip", "UTF-8")
                    + "=" + URLEncoder.encode(clip, "UTF-8");

            Socket socket = new Socket(urlMain.substring(0, urlMain.length() - 5), Integer.parseInt(urlMain.substring(urlMain.length() - 4)));
            socket.setSoTimeout(5 * 1000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            PrintWriter writer = new PrintWriter(out, false);

            writer.println("POST " + "/" + " HTTP/1.1");
            writer.println("Host: " + urlMain.substring(0, urlMain.length() - 5));
            writer.println("User-Agent: MediumOS");
            writer.println("Accept: text/html");
            writer.println("Content-Length: " + clippin.length());
            writer.println("Accept-Language: en-US");
            writer.println("Connection: close");
            writer.println("Content-Type: application/x-www-form-urlencoded");
            writer.println();
            writer.print(clippin);
            writer.flush();

            DataInputStream in = new DataInputStream(socket.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (Exception e){e.printStackTrace();}
    }
}
