package cl.afiebig.loginrest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ListItems extends Activity {

    //URL LOGIN Lista
    private String urlJsonLogin = "http://gestorcontable.ingvaldiviavivar.com/api/json/datosListaJson/1&1";
    private String Auth_token;

    private TextView textView;

    private String jsonResponse;

    private static ListItems mInstance;

    private static final String TAG = ListItems.class.getSimpleName();

    private RequestQueue mRequestQueue;

    public static synchronized ListItems getInstance() {
        return mInstance;
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = this;

        Intent intent = getIntent();
        Auth_token = intent.getStringExtra(MainActivity.ACCESS_TOKEN);

        textView = new TextView(this);
        textView.setText(Auth_token);
        setContentView(textView);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        makeJsonArrayRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_items, menu);
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

    private void makeJsonArrayRequest() {

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                urlJsonLogin, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse (JSONArray response) {
                Log.d(TAG, response.toString());

                try {
                    //Parsing json object response
                    //response will be json Object
                    String login = response.getString(0);
                    jsonResponse = "OK: " + login;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    textView.setTextColor(Color.RED);
                    textView.setText("Error, ERROR, ERROR");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //Set Mensaje Error
                textView.setTextColor(Color.RED);
                textView.setText("Error, Invalid Token");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + Auth_token);
                return headers;
            }
        };
        //Adding request to request queque
        ListItems.getInstance().addToRequestQueue(jsonObjReq);
        urlJsonLogin = "http://gestorcontable.ingvaldiviavivar.com/api/json/datosListaJson/1&1";
        textView.setText("OK");
        textView.setTextColor(Color.BLACK);
    }
}
