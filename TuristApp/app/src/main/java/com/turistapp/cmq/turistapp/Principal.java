package com.turistapp.cmq.turistapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Principal extends AppCompatActivity {

    EditText edtCorreo,edtContraseña;
    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        edtCorreo=(EditText)findViewById(R.id.edtCorreo);
        edtContraseña=(EditText)findViewById(R.id.edtContraseña);

        btnIngresar=(Button)findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread tr=new Thread(){
                    @Override
                    public void run(){
                    final String res=enviarPost(edtCorreo.getText().toString(),edtContraseña.getText().toString());
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            int r=objJSON(res);
                            if (r>0){
                                Intent i= new Intent(getApplicationContext(),Principal.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrectas", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    }
                };
                tr.start();
            }
        });
    }
    public String enviarPost(String correo, String contraseña)
    {
        String parametros="correo="+correo+"&contraseña="+contraseña;
        HttpURLConnection connection=null;
        String respuesta="";
        try
        {
            /*si estuviese en la nube tendria que poner el host del servidor de nube*/
            URL url=new URL("http://192.168.0.5/TuristApp/validacion.php");
            connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length",""+Integer.toString(parametros.getBytes().length));

            connection.setDoOutput(true);
            DataOutputStream wr=new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();

            Scanner inStream=new Scanner(connection.getInputStream());

            while(inStream.hasNextLine())
                respuesta+=(inStream.nextLine());



        }catch (Exception e)
        {

        }
        return respuesta.toString();
    }

    public int objJSON(String rspta)
    {
        int res=0;
        try {
            JSONArray json=new JSONArray(rspta);
            if (json.length()>0)
                res=1;
        }catch (Exception e)
        {}
        return res;

    }





}
