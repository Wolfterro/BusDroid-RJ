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

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Elementos da Activity Principal
    // ===============================
    private EditText editTextLine;
    private Button buttonLine;

    private EditText editTextOrder;
    private Button buttonOrder;

    // Propriedades Privadas da classe
    // ===============================
    private final int PERMISSION_GRANTED_VALUE = 0;

    // Menu de opções da Activity Principal
    // ====================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Selecionando opções no menu da Activity Principal
    // =================================================
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(about);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Criando Activity Principal do aplicativo
    // ========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Seleção para a linha de ônibus
        // ------------------------------
        editTextLine = (EditText)findViewById(R.id.editTextBusLine);
        buttonLine = (Button)findViewById(R.id.buttonLine);

        // Seleção para a ordem do ônibus
        // ------------------------------
        editTextOrder = (EditText)findViewById(R.id.editTextBusOrder);
        buttonOrder = (Button)findViewById(R.id.buttonOrder);

        // Pedindo permissão do usuário para determinar sua localização
        // ------------------------------------------------------------
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_GRANTED_VALUE);
        }

        // Buscando ônibus através da linha inserida
        // -----------------------------------------
        buttonLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextLine.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.pleaseInsertBusLine),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    // Iniciando o resgate de informações
                    // ==================================
                    ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.setTitle(getString(R.string.gettingInformations));
                    pd.setMessage(getString(R.string.pleaseWait));
                    pd.setCancelable(false);
                    pd.show();

                    GetBusLineThread gb = new GetBusLineThread(editTextLine.getText().toString(),
                            MainActivity.this,
                            pd);

                    gb.start();
                }
            }
        });

        // Buscando ônibus através da ordem inserida
        // -----------------------------------------
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextOrder.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.pleaseInsertBusOrder),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    // Iniciando o resgate de informações
                    // ==================================
                    ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.setTitle(getString(R.string.gettingInformations));
                    pd.setMessage(getString(R.string.pleaseWait));
                    pd.setCancelable(false);
                    pd.show();

                    GetBusOrderThread gb = new GetBusOrderThread(editTextOrder.getText().toString(),
                            MainActivity.this,
                            pd);

                    gb.start();
                }
            }
        });
    }
}
