package com.example.heartbeats.ebaymobile;

import android.app.Activity;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isDigitsOnly;
import static java.lang.Float.parseFloat;


public class EBaySearch extends ActionBarActivity {
    public EditText keyword;
    public TextView error;
    public EditText price_from;
    public EditText price_to;
    public Spinner sort_by;
   public Button clear_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebay_search);

        clear_btn = (Button)findViewById(R.id.clear_button);
        clear_btn.setOnClickListener(new OnClickListener() {
        public void onClick(View arg0) {

            keyword = (EditText) findViewById(R.id.Keyword);
            keyword.setText("");
            price_from = (EditText) findViewById(R.id.Price_from);
            price_from.setText("");
            price_to = (EditText) findViewById(R.id.Price_to);
            price_to.setText("");
            sort_by = (Spinner) findViewById(R.id.Sort_by);
            sort_by.setSelection(0);
            error = (TextView)findViewById(R.id.error);
            error.setText("");

        }
           });
            findViewById(R.id.search_button).setOnClickListener(new OnClickListener() {
                public void onClick (View arg0){

                    keyword = (EditText) findViewById(R.id.Keyword);
                    error = (TextView) findViewById(R.id.error);
                    price_from = (EditText) findViewById(R.id.Price_from);
                    price_to = (EditText) findViewById(R.id.Price_to);
                    sort_by = (Spinner) findViewById(R.id.Sort_by);
                    float from_val = 0;
                    float to_val;

                    if (keyword.getText().toString().equals("")) {
                        error.setText("Please enter a keyword");
                        return;
                    }

                    //String num1 = price_from.getText().toString();
                    //String num2 = price_to.getText().toString();



                    // if(!isValidNum(num1))
                    //       error.setText("Enter a valid number");

                    //if(!isValidNum(num2))
                    //  error.setText("Enter a valid number");
                    if(price_from.getText().toString().trim().length()!=0) {
                        from_val = Float.parseFloat(price_from.getText().toString());
                    }

                    if(price_to.getText().toString().trim().length()!=0){
                     to_val = Float.parseFloat(price_to.getText().toString());
                     if (from_val > to_val)
                      error.setText("Maximum price cannot be more than the Minimum price");
                        return;
                    }

                    new MainExecution().execute("");
                   // Log.v("executed", "DONE!!");

                }

                class MainExecution extends AsyncTask<String, Void, String> {

                    private String URL;
                    String SetServerString,ack;
                    JSONObject json;
                    protected String doInBackground(String... params) {
                        try {

                            // EBaySearch obj =  new EBaySearch();
                            String keywordValue = URLEncoder.encode(keyword.getText().toString(), "UTF-8");
                            String FromValue = URLEncoder.encode(price_from.getText().toString(), "UTF-8");
                            String ToValue = URLEncoder.encode(price_to.getText().toString(), "UTF-8");
                            String SortByValue = (sort_by.getSelectedItem().toString());
                            if(SortByValue.equals("Best Match"))
                            {
                                SortByValue="BestMatch";
                            }
                            else if(SortByValue.equals("Price: highest first"))
                            {
                                SortByValue="CurrentPriceHighest";
                            }

                            else if(SortByValue.equals("Price + Shipping: highest first"))
                            {
                                SortByValue="PricePlusShippingHighest";
                            }
                            else if(SortByValue.equals("Price + Shipping: lowest first"))
                            {
                                SortByValue="PricePlusShippingLowest";
                            }
                            String SortByValue1 = URLEncoder.encode(SortByValue, "UTF-8");
                            String URL = "http://saloneerege-env.elasticbeanstalk.com/index.php?keyword=" + keywordValue + "&price_from=" + FromValue + "&price_to=" + ToValue + "&sort_by=" + SortByValue1;

                           Log.v("index", URL);


                            // Create Request to server and get response
                            HttpClient Client = new DefaultHttpClient();
                            HttpGet httpget = new HttpGet(URL);
                            HttpResponse httpresponse = Client.execute(httpget);
                            SetServerString = EntityUtils.toString(httpresponse.getEntity());





                            // Show response on activity

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ClientProtocolException ex) {
                            Log.v("error", "Fail!");
                        } catch (IOException ex) {
                            Log.v("error2", "Fail!");
                        }
                        //TextView txt = (TextView) findViewById(R.id.error);
                        //txt.setText("Executed");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        try {
                            json = new JSONObject(SetServerString);
                            ack = json.getString("ack");
                            if (ack.equals("No results found")) {
                                error.setText("NO RESULTS FOUND !");
                              return;
                            }
                        }catch(Exception e){

                        }
                        keyword = (EditText) findViewById(R.id.Keyword);

                        String keywordValue = keyword.getText().toString();
                        Intent intent = new Intent(EBaySearch.this, ResultPage.class);
                        intent.putExtra("Keyword_Message", keywordValue);

                        Log.v("result", SetServerString);
                        intent.putExtra("result", SetServerString);
                        startActivity(intent);


                    }

                    @Override
                    protected void onPreExecute() {
                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                    }


                }


            }

            );

        }
   /* private boolean isValidNum(String number) {
        String NUMBER_PATTERN;
        NUMBER_PATTERN = "/[\\d]+(\\.[\\d]+)?/";

        Pattern pattern = Pattern.compile(NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
*/


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ebay_search, menu);
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
}
