package com.example.puthea.socketclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    EditText ed_serverAdd, ed_serverPort;
    Button bt_connect, bt_clear;
    TextView tv_serverRespond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_serverAdd = findViewById(R.id.editTex_serverAddress);
        ed_serverPort = findViewById(R.id.editText_serverPort);
        bt_connect = findViewById(R.id.button_connect);
        bt_clear = findViewById(R.id.button_clear);
        tv_serverRespond = findViewById(R.id.textView_respond);

        bt_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverAdd = ed_serverAdd.getText().toString();
                int serverPort = Integer.parseInt(ed_serverPort.getText().toString());
                ConnectTask connectTask = new ConnectTask(serverAdd, serverPort);
                connectTask.execute();
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_serverRespond.setText("");
            }
        });
    }

    public class ConnectTask extends AsyncTask<Void, Void, Void>{

        String serverAdd;
        int serverPort;
        String responsd = "";

        public ConnectTask(String serverAdd, int serverPort){
            this.serverAdd = serverAdd; this.serverPort = serverPort;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Socket socket = null;
            try{
                socket = new Socket(serverAdd, serverPort);
                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                /*
                 * notice:
                 * inputStream.read() will block if no data return
                 */
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    responsd += byteArrayOutputStream.toString("UTF-8");
                }

            } catch (UnknownHostException e){
//              if the IP address of the host could not be determined.
                e.printStackTrace();
                responsd = "UnknownHostException: " + e.toString();
            } catch (IOException | SecurityException | IllegalArgumentException e) {
//              if an I/O error occurs when creating the socket or
//              if a security manager exists and its checkConnect method doesn't allow the operation.
                e.printStackTrace();
                responsd = e.toString();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            tv_serverRespond.setText(responsd);
            super.onPostExecute(aVoid);
        }
    }
}
