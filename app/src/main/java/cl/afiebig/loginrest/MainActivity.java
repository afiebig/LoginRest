package cl.afiebig.loginrest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    //URL LOGIN JSON
    private String urlJsonLogin = "http://www.ingvaldiviavivar.com/api/login";

    public final static String ACCESS_TOKEN = "com.mycompany.myfirstapp.MESSAGE";

    private String jsonResponse;

    private TextView textResult;

    ProgressDialog progress;


    private static MainActivity mInstance;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInstance = this;
        progress = new ProgressDialog(this);
        progress.setTitle("Login In");
        progress.setMessage("Wait while login...");

        textResult = (TextView)findViewById(R.id.Resultado_Login);
    }

    public static synchronized MainActivity getInstance() {
        return mInstance;
    }

    private RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
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
        textResult = (TextView)findViewById(R.id.Resultado_Login);
        String login = ((EditText)findViewById(R.id.email)).getText().toString();
        String pass = ((EditText)findViewById(R.id.password)).getText().toString();
        textResult.setText("Generando Request JSON");
        progress.show();
        makeJsonObjectRequest(login, pass);
        return true;
    }

    private void makeJsonObjectRequest(String user, String pass) {

       /* HashMap<String, String> params = new HashMap<String, String>();
        params.put("username",user);
        params.put("password",pass);*/
        urlJsonLogin = urlJsonLogin + "?username=" + user + "&password=" + pass;

        //textResult.setText("Creando Objeto JSON");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
                urlJsonLogin, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    //Parsing json object response
                    //response will be json Object
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


                    progress.dismiss();
                    textResult.setText(jsonResponse);
                    //Nueva Vista.
                    Intent intent = new Intent(mInstance, ListItems.class);
                    intent.putExtra(ACCESS_TOKEN, access_token);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    textResult.setTextColor(Color.RED);
                    textResult.setText("Error, please check your username/password");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //textResult.setText("ERROR 02 --> " + "Error: " + error.getMessage());
                // To dismiss the dialog
                progress.dismiss();
                //Setea Mensaje Error
                textResult.setTextColor(Color.RED);
                textResult.setText("Error, please check your username/password");
            }
        });
        //Adding request to request queque
        MainActivity.getInstance().addToRequestQueue(jsonObjReq);
        urlJsonLogin = "http://www.ingvaldiviavivar.com/api/login";
        textResult.setTextColor(Color.BLACK);
    }
}
