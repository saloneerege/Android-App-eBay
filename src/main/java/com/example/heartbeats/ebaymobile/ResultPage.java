package com.example.heartbeats.ebaymobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class ResultPage extends ActionBarActivity {

    public JSONObject json;
    public TextView display_result;
    public ListView lv;
    private Context context;
    JSONObject item_list;
    public String img,title,price,shipping,image_url;
    public ImageView image;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();
        display_result =  (TextView) findViewById(R.id.result);
        Intent intent = getIntent();
        String message = intent.getStringExtra("Keyword_Message");
        display_result.setText("Results for ' " + message + "'");
      String res =  intent.getStringExtra("result");
       Log.v("result-now",res);
        JSONObject item;

        JSONObject basicInfo;

        double shipping_cost;
        try {
            json = new JSONObject(res);
            item_list = json.getJSONObject("item1");
            for (int i = 0; i < 5; i++) {
                item = (JSONObject) item_list.getJSONObject("item" + i);
                basicInfo = item.getJSONObject("basicInfo");
                img = basicInfo.getString("galleryURL");
                title = basicInfo.getString("title");
                image_url=basicInfo.getString("viewItemURL");
                price = basicInfo.getString("convertedCurrentPrice");
                shipping = basicInfo.getString("shippingServiceCost");
                if(shipping.trim().length()== 0){
                    shipping_cost=0.0;

                }
                else
                    shipping_cost = Double.parseDouble(shipping);
                if(shipping_cost > 0.0)
                    price = "Price : $" + price + "(+" + shipping + "Shipping)";
                else {
                    price = "Price : $" + price + "(FREE Shipping)";
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("title_new", title);
                map.put("price_new", price);
                map.put("shipping_new", shipping);
                map.put("image_new",img);
                map.put("image_url",image_url);

                jsonlist.add(map);
            }
        }catch (JSONException e) {

            e.printStackTrace();
        }

                ListAdapter adapter = new SimpleAdapter(this, jsonlist, R.layout.displayitems, new String[]{"title_new", "price_new","image_new"}, new int[]{R.id.title, R.id.price , R.id.image}){

                   public View getView(final int position,View convertView, final ViewGroup parent){

                        LayoutInflater inflater = ((Activity)ResultPage.this).getLayoutInflater();
                        convertView = inflater.inflate(R.layout.displayitems,parent,false);

                        TextView title = (TextView)convertView.findViewById(R.id.title);
                        TextView price = (TextView)convertView.findViewById(R.id.price);
                       image = (ImageView)convertView.findViewById(R.id.imageView);

                      title.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                            Intent i = new Intent(ResultPage.this, DetailsActivity.class);
                            try {
                                i.putExtra("Json_Object", item_list.getJSONObject("item"+ position).toString());
                               // Log.v("NEW",item_list.getJSONObject("item"+ position).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(i);

                        }
                    });

                       image.setOnClickListener(new View.OnClickListener() {
                           public void onClick(View view) {

                               Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(image_url));
                               startActivity(myIntent);

                           }
                       });

                       HashMap<String,String> mapresult = (HashMap)this.getItem(position);
                       title.setText(mapresult.get("title_new"));
                       price.setText(mapresult.get("price_new"));


                       new DownloadImageTask(image).execute(mapresult.get("image_new"));
                       return convertView;
                    }



                };
                lv = (ListView) findViewById(R.id.listView);
                lv.setAdapter(adapter);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_page, menu);
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
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }


}
