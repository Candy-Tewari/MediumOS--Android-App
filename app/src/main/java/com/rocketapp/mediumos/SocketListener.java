package com.rocketapp.mediumos;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketListener extends Service {

    private Context context;
    private Socket socket;

    public SocketListener() {

    }


    private Emitter.Listener onNewClip = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            new Runnable() {
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
                    System.out.println("Clip Message Received------------------->"+clipboard);
                    Toast.makeText(SocketListener.this, ""+clipboard, Toast.LENGTH_SHORT).show();
                }
            };
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("Url");
        try {
            socket = IO.socket("http://"+url);
            socket.connect();
            context=this;
            System.out.println("---------------------->This is where the socket has started");
            socket.on("clipboard", onNewClip);
            socket.emit("clipboard", "A new clip has came");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
