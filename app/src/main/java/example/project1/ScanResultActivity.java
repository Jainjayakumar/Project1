package example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanResultActivity extends AppCompatActivity {


    private static final String TAG = "Response";
    String postext="Press the button",scanResult;
    Button Refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        final TextView resp = findViewById(R.id.textView);

        Bundle extras = getIntent().getExtras();
            if (extras != null) {
                scanResult = extras.getString("ScanResult"); /* Retrieving text of QR Code */
                final String url = extras.getString("url");
                resp.setText(scanResult);
            }

        try {
            postRequest(scanResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            resp.setText(postext);
        }

        Refresh =  findViewById(R.id.refresh);
        Refresh.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                resp.setText(postext);
            }

        });

    }
    public void postRequest(String scanResult) throws IOException {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "http://192.168.42.186:8000/qrid";

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("QRID",scanResult);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                postext = "Connection Failed";
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                postext = mMessage;
                Log.e(TAG, mMessage);
            }
        });
    }
}
