package com.example.akashn.photo_vault;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.akashn.photo_vault.Adapter.Gallery_Adapter;
import com.example.akashn.photo_vault.Classes.MyImage;
import com.example.akashn.photo_vault.services.MyServices;
import com.nileshp.multiphotopicker.photopicker.activity.PickImageActivity;


import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Gallery extends AppCompatActivity {

    private File[] listFile;
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private ProgressBar progressBar;
    private ArrayList<MyImage> images;
    private Gallery_Adapter mAdapter;
    static final int OPEN_MEDIA_PICKER = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 1;
    Encryption encrypt;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<String> pathList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        try {
            encrypt=new Encryption();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
       // progressBar= (ProgressBar) findViewById(R.id.progress);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new Gallery_Adapter(getApplicationContext(), images);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        //starting service
     ////   Intent mServiceIntent = new Intent(Gallery.this,MyServices.class);
      //  startService(mServiceIntent);


//fetch images
        try {
            fetchImages();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }


         recyclerView.addOnItemTouchListener(new Gallery_Adapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new Gallery_Adapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);

                newFragment.show(ft,"slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));







        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 //startActivity(new Intent(Gallery.this,MainActivity.class));

                getImages();



            }
        });



        //referesh recycler view
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });





        //Request permission dialog
        if (ContextCompat.checkSelfPermission(Gallery.this
                ,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Gallery.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Gallery.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the

                // result of the request.
            }
        }

    }



    private void getImages() {

        Intent mIntent = new Intent(this, PickImageActivity.class);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 60);
        mIntent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 3);
        startActivityForResult(mIntent, PickImageActivity.PICKER_REQUEST_CODE);
    }


    private void fetchImages()  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        //pDialog.setMessage("Decrypting...");


        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        if(!myDir.isDirectory())
            myDir.mkdirs();

        if (myDir.isDirectory()) {
            listFile = myDir.listFiles();


            // Encryption encryptifile= null;

            // encryptfile = new Encryption();

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                MyImage image=new MyImage();
                image.setName(listFile[i].getName());

                //   image.setBytes(encryptfile.decrypt(listFile[i]));
                image.setLarge(listFile[i].getAbsolutePath());

               /* image.setSmall(listFile[i].getAbsolutePath());
                image.setMedium(listFile[i].getAbsolutePath());
                image.setLarge(listFile[i].getAbsolutePath());*/
                // image.setTimestamp(object.getString("timestamp"));

                images.add(image);


            }

            mAdapter.notifyDataSetChanged();
        }



        pDialog.hide();


    }





    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }




    void refreshItems() {
        // Load items
        // ...
        mAdapter.notifyDataSetChanged();
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (resultCode == -1 && requestCode == PickImageActivity.PICKER_REQUEST_CODE) {
            this.pathList = intent.getExtras().getStringArrayList(PickImageActivity.KEY_DATA_RESULT);
            if (this.pathList != null && !this.pathList.isEmpty()) {
                StringBuilder sb=new StringBuilder("");
                for(int i=0;i<pathList.size();i++) {
                    sb.append("Photo"+(i+1)+":"+pathList.get(i));
                    sb.append("\n");

                    //encryption
                    try {
                        encrypt.encrypt(Uri.parse(pathList.get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                }
                //tvResult.setText(sb.toString()); // here this is textview for sample use...
            }
        }
    }


}
