package com.rocketapp.mediumos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy myCustomizableThread = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myCustomizableThread);
        //Starting from here
    }

    private Emitter.Listener onNewClip = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String clipboard;
                    try {
                        clipboard = data.getString("clip");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    Toast.makeText(MainActivity.this, ""+clipboard, Toast.LENGTH_SHORT).show();
                    getClipToClipBoard(clipboard);
                }
            });
        }
    };

    private void getClipToClipBoard(String data){
        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(this.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("MediumOS", data);
        clipboard.setPrimaryClip(clip);
    }


    public void connect(View view) {
        final EditText editText = findViewById(R.id.url);
        String url = editText.getText().toString();
        //First lets check if the server is available
        try{
            socket = IO.socket("http://"+url);
            socket.connect();
        }
        catch (Exception e){ //We will handle all the types of exception here only. We will break it down ahead
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            return;
        }

        if(false){}

        else{

            /*
            * Before starting the new Activity lets create a new thread where the socket will work
            * This new thread can be a service also as we don't want android to quit the connection
            * After the service is started, we can focus on the coming audio and video on the next Activity
            */

            Intent intent = new Intent(this, SocketListener.class);
            final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(this.CLIPBOARD_SERVICE);
            clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
                public void onPrimaryClipChanged() {
                    String a = clipboard.getText().toString();
                    socket.emit("clipboard", a);
                }
            });
            socket.on("clipboard", onNewClip);
            //socket.disconnect();
            //intent.putExtra("Url", "http://"+url);
            //startService(intent);
            //startActivity(new Intent(this, SharingMedia.class));
        }
    }
}
