package pradadesigners.com.tequesta;

import android.nfc.Tag;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String TAG = "Tequesta";

    @BindView(R.id.statusText) TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        requestParkSiteHTML();
    }

    private JSONArray convertParkSiteHTMLtoJSON (List<String> matchingGroups) throws JSONException {

        JSONArray parksData = new JSONArray();
        for (String matchingGroup : matchingGroups) {
            JSONObject parkData = new JSONObject();

            Pattern pattern = Pattern.compile("<td((\\S|\\s)*?)</td>");
            Matcher matcher = pattern.matcher(matchingGroup);

            while (matcher.find()) {
                String match = matcher.group(1);
                match = match.replaceAll("(\\n|\\r|\\t)","");
                if (match.contains("statusImage")) {
                    String parkStatus = match.contains("cell-14") ? "open" : "closed";
                    parkData.put("status", parkStatus);
                }
                else if (match.contains("span")) {
                    match = match.split("<br")[0];
                    match = match.replaceAll("\\s{2,}","");
                    parkData.put("name", match);
                }
                else if (match.contains(">Updated")) {
                    parkData.put("lastUpdated", match.replace(" align=\"center\" width=\"150\">Updated ",""));
                }
                else if (match.contains("align")) {
                    match = match.replace(" align=\"center\" width=\"125\">","");
                    match = match.replaceAll("\\s{2,}","");
                    match = match.replace("<br />",": ");
                    parkData.put("comment", match);
                }
            }

            parksData.put(parkData);
        }

        Log.d(TAG, parksData.toString());

        return parksData;
    }

    private void requestParkSiteHTML() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://www.westonfl.org/Departments/ParksAndRecreation/ParksStatus.aspx")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }


                String responseBody = response.body().string();

                Pattern pattern = Pattern.compile("rgRow.*(\\s|\\S)*?</tr");
                Matcher matcher = pattern.matcher(responseBody);

                List<String> matchingGroups = new ArrayList<>();

                while (matcher.find()) {
                    String match = matcher.group(0);
                    matchingGroups.add(match);
                }

                JSONArray data = null;

                try {
                    data = convertParkSiteHTMLtoJSON(matchingGroups);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String status = data.toString();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "status: " + status);
                        statusText.setText(status);
                    }
                });
            }
        });
    }
}
