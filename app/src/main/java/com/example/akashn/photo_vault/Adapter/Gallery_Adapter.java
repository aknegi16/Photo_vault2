package com.example.akashn.photo_vault.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.akashn.photo_vault.Classes.MyImage;
import com.example.akashn.photo_vault.Encryption;
import com.example.akashn.photo_vault.R;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by akashn on 27-08-2017.
 */

public class Gallery_Adapter extends RecyclerView.Adapter<Gallery_Adapter.MyViewHolder> {

    private List<MyImage> images;
    private Context mcontext;

    public Gallery_Adapter(Context mcontext, ArrayList<MyImage> images) {
        this.images = images;
        this.mcontext = mcontext;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder
    {public ImageView thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            thumbnail= itemView.findViewById(R.id.thumbnail);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        final int pos=position;


        Thread thread=new Thread() {
            ///making encryption object
     @Override
           public void run() {

               MyImage image = images.get(pos);
         Encryption encryption = null;
         try {
             encryption = new Encryption();
         } catch (NoSuchPaddingException e) {
             e.printStackTrace();
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         } catch (InvalidKeyException e) {
             e.printStackTrace();
         }



               try {
                   Glide.with(mcontext).load(encryption.decrypt(new File(image.getLarge())))
                           .asBitmap()
                           .thumbnail(0.1f)

                           // .diskCacheStrategy(DiskCacheStrategy.ALL)
                           .into(holder.thumbnail);
               } catch (NoSuchPaddingException e) {
                   e.printStackTrace();
               } catch (NoSuchAlgorithmException e) {
                   e.printStackTrace();
               } catch (InvalidKeyException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               } catch (BadPaddingException e) {
                   e.printStackTrace();
               } catch (IllegalBlockSizeException e) {
                   e.printStackTrace();
               }

           }
            // }
        /*catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }*/
      /*  Glide.with(mcontext).load(image.getBytes())
                    .asBitmap()
                    .thumbnail(0.5f)

                   // .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.thumbnail);*/

        };
    }
    @Override
    public int getItemCount() {
        return images.size();
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private Gallery_Adapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Gallery_Adapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

}}
