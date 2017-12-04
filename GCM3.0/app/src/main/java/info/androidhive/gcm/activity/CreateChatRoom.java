package info.androidhive.gcm.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.gcm.R;
import info.androidhive.gcm.app.EndPoints;
import info.androidhive.gcm.app.MyApplication;

public class CreateChatRoom extends AppCompatActivity {

    private EditText editText;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat_room);
        editText = (EditText) findViewById(R.id.input_name);
        btn = (Button) findViewById(R.id.button_create);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                sendRequest(name);
            }
        });
    }

    private void sendRequest(final String name){
        AndroidNetworking.post(EndPoints.CREATE_CHAT_ROOM)
                .addBodyParameter("name",name)
                .setTag("Test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String s = response.getString("error");
                            String t = response.getString("created_at");
                            if(!s.equals("false")){
                                Toast.makeText(CreateChatRoom.this, "Sorry chat room can't be created right now", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent i = new Intent(CreateChatRoom.this,MainActivity.class);
                                i.putExtra("name",name);
                                i.putExtra("timestamp",t);
                                i.putExtra("id",response.getString("chat_room_id"));
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() != 0) {
//
                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());

                        } else {

                            Log.d("TAG", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    }
                });
    }
}
