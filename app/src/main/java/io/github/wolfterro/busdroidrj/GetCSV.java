/*
The MIT License (MIT)

Copyright (c) 2017 Wolfgang Almeida

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package io.github.wolfterro.busdroidrj;

import android.content.Context;

import java.net.*;
import java.io.*;

import com.opencsv.CSVReader;

public class GetCSV extends Thread {

    // Propriedades privadas
    // =====================
    private Context c = null;

    private String busLine = "";
    private String route = "";

    // Construtor da classe
    // ====================
    public GetCSV(Context c, String busLine) {
        this.c = c;

        this.busLine = busLine;
    }

    // Métodos privados da classe
    // ==========================

    // Iniciando resgate de valores sobre a linha de ônibus escolhida
    // ==============================================================
    @Override
    public void run() {
        try {
            // Retirando as letras da linha atual do ônibus
            // --------------------------------------------
            String busLineNoLetters = busLine.replaceAll("[^\\d]", "");

            URL u = new URL(String.format(GlobalVars.busRouteAPI, busLineNoLetters));
            BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));

            CSVReader reader = new CSVReader(in);

            // Adicionando zeros para a busca de linhas com menos de 3 números
            // ---------------------------------------------------------------
            if(busLineNoLetters.length() == 1) {
                busLineNoLetters = String.format("00%s", busLineNoLetters);
            }
            else if(busLineNoLetters.length() == 2) {
                busLineNoLetters = String.format("0%s", busLineNoLetters);
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {

                // Imprime o trajeto da linha de ônibus, sem a linha atual do ônibus ao lado
                // -------------------------------------------------------------------------
                if(nextLine[1].startsWith(busLineNoLetters)) {
                    route = nextLine[1].replace(busLineNoLetters + "-", "");
                    break;
                }
                else {
                    route = c.getString(R.string.notAvailable);
                    continue;
                }
            }

            reader.close();
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
            route = c.getString(R.string.notAvailable);
        }
        catch(IOException e) {
            e.printStackTrace();
            route = c.getString(R.string.notAvailable);
        }
    }

    // Métodos públicos da classe
    // ==========================

    // Resgatando o trajeto da linha de ônibus
    // =======================================
    public String getRoute() {
        return route;
    }
}