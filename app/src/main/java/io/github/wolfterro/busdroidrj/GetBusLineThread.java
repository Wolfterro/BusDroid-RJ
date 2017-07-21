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

/**
 * Created by Wolfterro on 16/07/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.json.*;

import java.util.ArrayList;

public class GetBusLineThread extends Thread {
    // Propriedades privadas
    // =====================
    private Context c = null;
    private ProgressDialog pd = null;

    private String busLineReceived = "";
    private Object busLine = null;

    private static int DATAHORA = 0;
    private static int ORDEM = 1;
    private static int LINHA = 2;
    private static int LATITUDE = 3;
    private static int LONGITUDE = 4;
    private static int VELOCIDADE = 5;

    private ArrayList<String> results = new ArrayList<String>();
    private boolean isOK = false;
    private boolean getInfoStatus = false;
    private String toastErrorMsg = "";

    // Construtor da classe
    // ====================
    public GetBusLineThread(String busLineReceived, Context c, ProgressDialog pd) {
        this.busLineReceived = busLineReceived;
        this.c = c;
        this.pd = pd;
    }

    // Métodos públicos
    // ================

    @Override
    public void run() {
        try {
            busLine = Double.parseDouble(busLineReceived);
        }
        catch(Exception e) {
            busLine = busLineReceived.toUpperCase();
        }

        getBusLineInfo();

        if(getInfoStatus) {
            if(results.size() == 0 || results == null) {
                isOK = false;
            }
            else {
                isOK = true;
            }
        }

        handler.sendEmptyMessage(0);
    }

    // Metodos privados
    // ================

    // Resgatando as informações da linha de ônibus escolhida
    // ======================================================
    private void getBusLineInfo() {
        GetJSON gj = new GetJSON(GlobalVars.busAPI);
        JSONObject jo = gj.get();

        if(!gj.getErrorMsg().equals("OK!")) {
            toastErrorMsg = String.format("%s\n%s %s",
                    c.getString(R.string.errorObtainingInformations),
                    c.getString(R.string.error),
                    gj.getErrorMsg());

            toastError.sendEmptyMessage(0);
            getInfoStatus = false;

            return;
        }

        try {
            JSONArray data = jo.getJSONArray("DATA");

            for(int i = 0; i < data.length(); i++) {
                JSONArray dataArray = data.getJSONArray(i);

                if(dataArray.get(LINHA).equals(busLine)) {
                    String latitLongit = String.format("%s,%s",
                            dataArray.get(LATITUDE),
                            dataArray.get(LONGITUDE));

                    String hora = dataArray.get(DATAHORA).toString().split(" ")[1];

                    String res = String.format("%s -//- %s -//- %s -//- %s",
                            latitLongit,
                            dataArray.get(ORDEM),
                            hora,
                            dataArray.get(VELOCIDADE));

                    results.add(res);
                }
            }

            getInfoStatus = true;
        }
        catch(org.json.JSONException e) {
            e.printStackTrace();

            toastErrorMsg = String.format("%s\n%s %s",
                    c.getString(R.string.errorObtainingInformations),
                    c.getString(R.string.error),
                    "JSON_EXCEPTION_ERROR_LINE!");

            toastError.sendEmptyMessage(0);
            getInfoStatus = false;
        }
    }

    // Dispensando a mensagem do ProgressDialog
    // ========================================
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(getInfoStatus) {
                if(isOK) {
                    pd.dismiss();

                    String message = String.format(c.getString(R.string.foundXBusFromYLine),
                            results.size(),
                            busLineReceived);

                    Toast.makeText(c, message, Toast.LENGTH_LONG).show();

                    // Chamando o Activity do Google Maps
                    // ----------------------------------
                    Intent intent = new Intent(c, MapsActivity.class);
                    intent.putStringArrayListExtra("RESULTS", results);

                    String bLine = String.format("%s", busLine).replace(".0", "");
                    String markerTitle = String.format("%s %s",
                            c.getString(R.string.bus),
                            bLine);

                    intent.putExtra("BUSLINE", markerTitle);
                    c.startActivity(intent);
                }
                else {
                    pd.dismiss();

                    String message = String.format(c.getString(R.string.noBusLineFound),
                            busLineReceived);
                    Toast.makeText(c, message, Toast.LENGTH_LONG).show();
                }
            }
            else {
                pd.dismiss();
            }
        }
    };

    // Mensagens de erro
    // =================
    private Handler toastError = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(c, toastErrorMsg, Toast.LENGTH_LONG).show();
        }
    };
}
