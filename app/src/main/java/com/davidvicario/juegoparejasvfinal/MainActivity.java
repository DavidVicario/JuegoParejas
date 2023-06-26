package com.davidvicario.juegoparejasvfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final int[] idArray = {
            R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6,
            R.id.imageView7, R.id.imageView8, R.id.imageView9, R.id.imageView10,  R.id.imageView11,
            R.id.imageView12,  R.id.imageView13,  R.id.imageView14,  R.id.imageView15,  R.id.imageView16,
            R.id.imageView17,  R.id.imageView18,  R.id.imageView19,  R.id.imageView20,  R.id.imageView21
    };

    private static final int[] idDrawable = {
            R.drawable.circuloa, R.drawable.circulob, R.drawable.cuadradoa, R.drawable.cuadradob,
            R.drawable.romboa, R.drawable.rombob, R.drawable.trapecioa, R.drawable.trapeciob,
            R.drawable.trianguloa, R.drawable.triangulob
    };

    private static final int[] posicionFichas = new int[idArray.length];
    private ImageView ficha [] = new ImageView[idArray.length];
    private static int destapada[] = new int[20];
    private static int imagenAnteriorDestapada = 0;
    private static int posicionAnteriorDestapada = 99;
    private ImageView imagenViewAnteriorDestapada= null;

    public static int puntuacion = 0;
    private static int acierto = 0;
    public static int puntosTotal = 0;
    private CountDownTimer tempo;
    public static long sg = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ocultar la barra de la aplicación
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        final Button repeat = findViewById(R.id.btnRepetir);
        final Button enviar = findViewById(R.id.btnEnviar);
        final TextView puntos = findViewById(R.id.txtPuntos);
        final TextView tiempo = findViewById(R.id.txtTime);
        final TextView puntosTotales = findViewById(R.id.txtPuntosTotales);

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barajea();
                iniZERO();
                inicia();
                enviar.setVisibility(View.INVISIBLE);
                puntuacion = 0;
                puntos.setText("Puntuación: " + puntuacion);
                puntosTotales.setText("");
                tempo.start();
                for (int i = 0; i < ficha.length; i++) {
                    ficha[i].setClickable(true);
                }
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MarcadorHTTP.class);
                startActivity(intent);
            }
        });

        tempo = new CountDownTimer(120000,1000){
            private boolean msg10sg = true;
            public void onTick(long millisUntilFinished){
                sg= millisUntilFinished / 1000;
                tiempo.setText("Tiempo: " + sg);
                if(millisUntilFinished < 11000 && msg10sg){
                    Toast.makeText(getApplicationContext(), "10 sg", Toast.LENGTH_SHORT).show();
                    msg10sg = false;
                }
            }
            public void onFinish(){
                tiempo.setText("Termino");
                for (int i = 0; i < ficha.length; i++) {
                    ficha[i].setClickable(false);
                }
                puntosTotal = puntuacion;
                puntosTotales.setText("Puntuación Total: " + puntosTotal);
                enviar.setVisibility(View.VISIBLE);
            }
        }.start();

        barajea();
        iniZERO();
        inicia();
        enviar.setVisibility(View.INVISIBLE);

        for( int nn = 0; nn<idArray.length; nn++){
            ficha[nn] = (ImageView)findViewById(idArray[nn]);
            ficha[nn].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View viewMiCarta) {

                    int posEnArray = damePos(viewMiCarta.getId());
                    Toast.makeText(getApplicationContext(), String.valueOf(posEnArray), Toast.LENGTH_SHORT ).show();

                    if (cartasDestapadas() < 2) {
                        ((ImageView) viewMiCarta).setImageResource(posicionFichas[posEnArray]);
                        destapada[posEnArray] = 1;

                        if (imagenAnteriorDestapada == posicionFichas[posEnArray] && cartasDestapadas() ==2) {
                            new CountDownTimer(1000, 1000) {
                                public void onTick(long millisUntilFinished) {}
                                public void onFinish() {
                                    ((ImageView) viewMiCarta).setImageResource(R.drawable.tick);
                                }
                            }.start();

                            imagenViewAnteriorDestapada.setImageResource(R.drawable.tick);
                            imagenViewAnteriorDestapada.setClickable(false);
                            puntuacion = puntuacion +5;
                            acierto++;
                            puntos.setText("Puntuacion: "+ puntuacion);
                            destapada[posEnArray] = 2;
                            destapada[posicionAnteriorDestapada] = 2;
                            if(acierto == 10){
                                tempo.cancel();
                                puntosTotal=(int)sg * 5 + puntuacion;
                                puntosTotales.setText("Puntuacion Total: " + puntosTotal);
                                enviar.setVisibility(View.VISIBLE);
                            }

                        }

                        imagenAnteriorDestapada = posicionFichas[posEnArray];
                        posicionAnteriorDestapada = posEnArray;
                        imagenViewAnteriorDestapada = (ImageView) viewMiCarta;
                    }
                    else {
                        inicia();
                        puntuacion = puntuacion -1;
                        puntos.setText("Puntuacion: " + puntuacion);
                    }
                }
            });
        }
    }

    private void barajea(){
        ArrayList<Integer> lista2 = new ArrayList<>();
        for(int j = 0; j < idDrawable.length; j++) {
            posicionFichas[j] = idDrawable[j];
        }
        // relleno de la segunda mitad
        for(int j = 10; j < idDrawable.length *2; j++) {
            posicionFichas[j] = idDrawable[19-j];
        }

        for (int j = 0; j < posicionFichas.length; j++) {
            lista2.add(posicionFichas[j]);
        }
        Collections.shuffle(lista2);
        for (int j = 0; j < posicionFichas.length; j++) {
            posicionFichas[j]= lista2.get(j);
        }
        System.out.println(Arrays.toString(lista2.toArray()));

    }

    private int damePos(int pIdObjeto) {
        int ii = 0;
        while (pIdObjeto != idArray[ii++]);
        return ii-1;
    }

    private int cartasDestapadas(){
        int contador = 0;
        for(int ii = 0; ii < destapada.length; ii++){
            if (destapada[ii] ==1) contador++;
        }
        return contador;
    }

    private void inicia(){
        for(int n = 0; n < 20; n++) {
            if (destapada[n] == 1) {
                ficha[n] = (ImageView) findViewById(idArray[n]);
                ficha[n].setImageResource(R.drawable.rever);
                destapada[n] = 0;
            }
        }
    }

    private void iniZERO(){
        for(int n = 0; n < 20; n++) destapada[n] = 1;
    }
}