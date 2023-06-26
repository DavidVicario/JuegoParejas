package com.davidvicario.juegoparejasvfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MarcadorHTTP extends AppCompatActivity {
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        // ocultar la barra de la aplicación
        if (getSupportActionBar() != null)  getSupportActionBar().hide();

        final Button send = findViewById(R.id.btnSend);
        final EditText jugador =  findViewById(R.id.editJugador);
        final EditText puntosT =  findViewById(R.id.editTotal);
        final EditText puntosJ =  findViewById(R.id.editJuego);
        final EditText bonus =  findViewById(R.id.editBonus);
        final EditText tiempo =  findViewById(R.id.editTiempo);
        final TextView msjERROR = findViewById(R.id.txtError);

        puntosT.setText(String.valueOf(MainActivity.puntosTotal));
        puntosJ.setText(String.valueOf(MainActivity.puntuacion));
        bonus.setText(String.valueOf((int)MainActivity.sg*5));
        tiempo.setText((String.valueOf(120-(int)MainActivity.sg)));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // servidor php que procesa el POST
                String url = "https://digitalgentilis.com/apps/android/actualiza.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                        // response -> Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show(),
                        response -> msjERROR.setText(response),
                        error -> Toast.makeText(MarcadorHTTP.this, error.toString(), Toast.LENGTH_LONG).show()){
                    // añadir los parametros de la respuesta

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("JU", jugador.getText().toString());
                        params.put("PT", puntosT.getText().toString());
                        params.put("PJ", puntosJ.getText().toString());
                        params.put("BO", bonus.getText().toString());
                        params.put("TI", tiempo.getText().toString());
                        return params;
                    }
                };
                requestQueue = Volley.newRequestQueue(MarcadorHTTP.this);
                requestQueue.add(stringRequest);
            }
        });
    }
}
