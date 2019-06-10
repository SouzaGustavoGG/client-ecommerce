package com.example.client_ecommerce.service;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

public class Stream {

    /**
     * responsável por fechar interfaces Closeable
     * @param cs
     */
    public static void closeAllStreams(Closeable... cs) {
        for(Closeable c : cs) {
            if(c != null) {
                try {
                    c.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * responsável por ler InputStream e converter em uma String
     * @param isr
     * @return
     */
    public static String convertInputStreamToString(InputStreamReader isr) {
        BufferedReader bfr = null;
        try {
            bfr = new BufferedReader(isr);
            String line;

            StringBuilder builder = new StringBuilder();
            while ((line = bfr.readLine()) != null) {
                builder.append(line + "\n");
            }

            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAllStreams(isr, bfr);
        }
        return null;
    }
}


