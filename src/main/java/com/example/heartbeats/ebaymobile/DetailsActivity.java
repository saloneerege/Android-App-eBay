package com.example.heartbeats.ebaymobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;


public class DetailsActivity extends ActionBarActivity {
    public JSONObject item;
    public JSONObject basicInfo;
    public JSONObject sellerInfo;
    public JSONObject shippingInfo;
    public String img,title,price,shipping,item_url,loc,top_rated,userName,feed,posfeed,rating,store,handling,ship_loc,ship_type,returns,exp_shipping,one_shipping;
    public JSONObject item_list;
    public TextView title_name,buyname,buyval,condname,condval,catname,catval,username,userval,feed_name,feed_val,pos_name,pos_val,rating_name,storename,storeval,shiploc_name,shiploc_val,shiptype_name,shiptype_val,handling_name,handling_val;
    public TextView expship_name,oneship_name,returns_name;
    public ImageView rating_val,expship_val,oneship_val,returns_val;
    public TextView price_cost;
    public TextView location_name;
    public ImageView fb_img;
    public ImageView top_img;
    double shipping_cost;
    public ImageView image;
    public String buy,cond,category;
    public Button basic_btn,seller_btn,shipping_btn,buy_btn;
    public TableLayout basicinfo,shippinginfo,sellerinfo;
    ShareDialog fb_share;
    CallbackManager callbackManager;
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        fb_share = new ShareDialog(this);
        fb_share.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
// successful so call the share async task
                if(result.getPostId() != null) {
                    Toast.makeText(getBaseContext(),
                            "Posted Successfully POST ID : !! " +result.getPostId(),
                            Toast.LENGTH_SHORT).show();
                }
               else if(result.getPostId() == null) {
                    Toast.makeText(getBaseContext(),
                            "Not Posted ",
                            Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(),
                        "Not Posted ",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {

            }

        });
        basicinfo=(TableLayout)findViewById(R.id.basicInfo);
        sellerinfo=(TableLayout)findViewById(R.id.SellerInfo);
        shippinginfo=(TableLayout)findViewById(R.id.shippingInfo);

        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("Json_Object"));
            //item= jsonObj.getJSONObject("item");
            basicInfo = jsonObj.getJSONObject("basicInfo");
            sellerInfo=jsonObj.getJSONObject("sellerInfo");
            shippingInfo=jsonObj.getJSONObject("shippingInfo");
          //  Log.v("sellerInfo",sellerInfo.toString());
            img = basicInfo.getString("galleryURL");
            title = basicInfo.getString("title");
            price = basicInfo.getString("convertedCurrentPrice");
            item_url = basicInfo.getString("viewItemURL");
            shipping = basicInfo.getString("shippingServiceCost");
            top_rated=basicInfo.getString("topRatedListing");
            category=basicInfo.getString("categoryName");
            buy = basicInfo.getString("listingType");
            cond= basicInfo.getString("conditionDisplayName");

            userName=sellerInfo.getString("sellerUserName");
            feed=sellerInfo.getString("feedbackScore");
            posfeed=sellerInfo.getString("positiveFeedbackPercent");
            rating=sellerInfo.getString("feedbackRatingStar");
            store=sellerInfo.getString("sellerStoreName");

            handling=shippingInfo.getString("handlingTime");
            ship_loc=shippingInfo.getString("shipToLocations");
            ship_type=shippingInfo.getString("shippingType");
            returns=shippingInfo.getString("returnsAccepted");
            exp_shipping=shippingInfo.getString("expeditedShipping");
            one_shipping=shippingInfo.getString("oneDayShippingAvailable");



            basic_btn= (Button)findViewById(R.id.basic_button);
            seller_btn= (Button)findViewById(R.id.seller_button);
            shipping_btn = (Button)findViewById(R.id.shipping_button);
            basic_btn= (Button)findViewById(R.id.basic_button);
            basic_btn.setBackgroundResource(R.drawable.aquabutton);
            seller_btn.setBackgroundResource(R.drawable.greybutton);
            shipping_btn.setBackgroundResource(R.drawable.greybutton);
            buy_btn =(Button)findViewById(R.id.buy_nowbtn);
            buy_btn.setBackgroundResource(R.drawable.roundbutton);


            image = (ImageView) findViewById(R.id.image);
            new DownloadImageTask(image).execute(img);

           buy_btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item_url));
                    startActivity(myIntent);

                }
            });
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
            loc = basicInfo.getString("location");

            title_name = (TextView) findViewById(R.id.title_image);
            title_name.setText(title);
            price_cost = (TextView) findViewById(R.id.title_price);
            price_cost.setText(price);
            location_name = (TextView) findViewById(R.id.Locations);
            location_name.setText(loc);
            top_img=(ImageView)findViewById(R.id.top_rated);
            if(top_rated.equals("true"))
            {
              top_img.setImageResource(R.drawable.toprated);
            }
            fb_img = (ImageView)findViewById(R.id.fb_img);
            fb_img.setImageResource(R.drawable.fb);

            fb_img.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View view) {
                                              if (ShareDialog.canShow(ShareLinkContent.class)) {
                                                  ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                                          .setImageUrl(Uri.parse(img))
                                                          .setContentTitle(title)
                                                          .setContentDescription(price)
                                                          .setContentDescription(loc)
                                                          .setContentUrl(Uri.parse(item_url))
                                                  .build();

                                                  fb_share.show(linkContent,ShareDialog.Mode.FEED);
                                              }
                                          }
                                      });



            buyval = (TextView)findViewById(R.id.buy_val);
            buyval.setText(buy);
            buyname = (TextView)findViewById(R.id.buy_name);
            buyname.setText("Buying Format");
            condname = (TextView)findViewById(R.id.condition_name);
            condname.setText("Condition");
            condval = (TextView)findViewById(R.id.condition_val);
            condval.setText(cond);
            catname = (TextView)findViewById(R.id.cat_name);
            catname.setText("Category Name");
            catval = (TextView)findViewById(R.id.cat_value);
            catval.setText(category);

            username = (TextView)findViewById(R.id.user_name);
            username.setText("User Name");
            userval = (TextView)findViewById(R.id.user_value);
            userval.setText(userName);
            feed_name = (TextView)findViewById(R.id.feedback_name);
            feed_name.setText("Feedback Score");
            feed_val = (TextView)findViewById(R.id.feedback_val);
            feed_val.setText(feed);
            pos_name = (TextView)findViewById(R.id.posfeedback_name);
            pos_name.setText("Positive Feedback");
            pos_val = (TextView)findViewById(R.id.posfeedback_val);
            pos_val.setText(posfeed + "%");
            rating_name = (TextView)findViewById(R.id.topratedSeller_name);
            rating_name.setText("Top Rated");
            rating_val = (ImageView)findViewById(R.id.topratedSeller_val);
            if(rating.equals("true"))
            {
               rating_val.setImageResource(R.drawable.tick);
            }
            else
                rating_val.setImageResource(R.drawable.cross);

            storename = (TextView)findViewById(R.id.store_name);
            storename.setText("Store");
            storeval = (TextView)findViewById(R.id.store_val);
            if(store.trim().length()== 0)
               storeval.setText("NA");
            else
             storeval.setText(store);




            shiploc_name=(TextView)findViewById(R.id.shippingloc_name);
            shiploc_name.setText("Shipping Locations");

            shiploc_val=(TextView)findViewById(R.id.shippingloc_val);
            shiploc_val.setText(ship_loc);

            shiptype_name=(TextView)findViewById(R.id.shippingtype_name);
            shiptype_name.setText("Shipping Type");

            shiptype_val=(TextView)findViewById(R.id.shippingtype_value);
            shiptype_val.setText(ship_type);

            handling_name=(TextView)findViewById(R.id.handling_name);
            handling_name.setText("Handling Time");

            handling_val=(TextView)findViewById(R.id.handling_val);
            handling_val.setText(handling + "day(s)");

            expship_name = (TextView)findViewById(R.id.expshipping_name);
            expship_name.setText("Expedited Shipping");

            expship_val = (ImageView)findViewById(R.id.expshipping_val);

            if(exp_shipping.equals("true"))
             expship_val.setImageResource(R.drawable.tick);
            else
                expship_val.setImageResource(R.drawable.cross);

            oneship_name = (TextView)findViewById(R.id.oneshipping_name);
            oneship_name.setText("One Day Shipping");

            oneship_val = (ImageView)findViewById(R.id.oneshipping_val);

            if(one_shipping.equals("true"))
                oneship_val.setImageResource(R.drawable.tick);
            else
                oneship_val.setImageResource(R.drawable.cross);

            returns_name = (TextView)findViewById(R.id.returns_name);
            returns_name.setText("Returns Accepted");

            returns_val = (ImageView)findViewById(R.id.returns_val);

            if(returns.equals("true"))
                returns_val.setImageResource(R.drawable.tick);
            else
                returns_val.setImageResource(R.drawable.cross);




            //returns,exp_shipping,one_shipping





            findViewById(R.id.basic_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                basic_btn.setBackgroundResource(R.drawable.aquabutton);
                seller_btn.setBackgroundResource(R.drawable.greybutton);
                shipping_btn.setBackgroundResource(R.drawable.greybutton);
                basicinfo.setVisibility(View.VISIBLE);
                sellerinfo.setVisibility(View.INVISIBLE);
                shippinginfo.setVisibility(View.INVISIBLE);

            }
               });

         findViewById(R.id.shipping_button).setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    basic_btn.setBackgroundResource(R.drawable.greybutton);
                    seller_btn.setBackgroundResource(R.drawable.greybutton);
                    shipping_btn.setBackgroundResource(R.drawable.aquabutton);
                    basicinfo.setVisibility(View.INVISIBLE);
                    sellerinfo.setVisibility(View.INVISIBLE);
                    shippinginfo.setVisibility(View.VISIBLE);

                }
            });
            findViewById(R.id.seller_button).setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    basic_btn.setBackgroundResource(R.drawable.greybutton);
                    seller_btn.setBackgroundResource(R.drawable.aquabutton);
                    shipping_btn.setBackgroundResource(R.drawable.greybutton);
                    basicinfo.setVisibility(View.INVISIBLE);
                    sellerinfo.setVisibility(View.VISIBLE);
                    shippinginfo.setVisibility(View.INVISIBLE);


                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
