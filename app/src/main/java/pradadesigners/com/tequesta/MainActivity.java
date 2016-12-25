package pradadesigners.com.tequesta;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
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

    @BindView(R.id.parkList) ListView parkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        requestParkSiteHTML();
    }


    private void requestParkSiteHTML() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://www.westonfl.org/Departments/ParksAndRecreation/ParksStatus.aspx")
                .build();

        final Activity activity = this;

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

                List<Park> parks = new ArrayList<>();

                while (matcher.find()) {
                    String match = matcher.group(0);
                    Park park = new Park(match);
                    parks.add(park);
                }

                final ArrayAdapter<Park> adapter = new ArrayAdapter<>(activity, R.layout.activity_list_view, R.id.label, parks);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parkList.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
