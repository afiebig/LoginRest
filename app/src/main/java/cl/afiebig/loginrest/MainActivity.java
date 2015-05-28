package cl.afiebig.loginrest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    //URL LOGIN JSON
    private String urlJsonLogin = "http://www.ingvaldiviavivar.com/api/login";

    private String jsonResponse;
    private  JsonObjectRequest jsonReq;

    private TextView txtResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean AtempLogin (View view){
        makeJsonObjectRequest("Valdivia","123");
        return true;
    }

    private String makeJsonObjectRequest(String user, String pass) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username",user);
        params.put("password",pass);
        urlJsonLogin = urlJsonLogin + "?username=" + user + "&password=" + pass;

        jsonReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonLogin, null, new Response.Listener<JSONObject>() {

            public void onResponse(JSONObject response) {
                // Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    String login = response.getString("login");
                    String permissions = response.getString("permissions");
                    String access_token = response.getString("access_token");
                    String expires_in = response.getString("expires_in");
                    String refresh_token = response.getString("refresh_token");


                    jsonResponse = "";
                    jsonResponse += "login: " + login + "\n\n";
                    jsonResponse += "permissions: " + permissions + "\n\n";
                    jsonResponse += "access_token: " + access_token + "\n\n";
                    jsonResponse += "expires_in: " + expires_in + "\n\n";
                    jsonResponse += "refresh_token: " + refresh_token + "\n\n";

                    txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                    jsonResponse = "Acceso Denegado Favor Revise sus credenciales";
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                jsonResponse = "Acceso Denegado Favor Revise sus credenciales";
            }
        });

        urlJsonLogin = "http://www.ingvaldiviavivar.com/api/login";
        return jsonResponse;
    }
}
