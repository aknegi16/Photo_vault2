package com.example.akashn.photo_vault;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by akashn on 17-08-2017.
 */

public class Encryption  {

    Uri uri;
    CipherInputStream cipherIn;
    Cipher cipher;
    KeyGenerator keyGen;
    Key key;
    SecretKeySpec sks; //key fixed string



    public Encryption() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {



       // this.context=context;
        this.uri=uri;
        cipher=Cipher.getInstance("AES");
       //  keyGen=KeyGenerator.getInstance("AES");

         sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
       // key=keyGen.generateKey();



    }

    public void encrypt(Uri uri) throws IOException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE,sks);
        //cipherIn= new CipherInputStream(new FileInputStream(new File(uri.getPath())),cipher);
        File inputfile=new File(uri.getPath());
          FileInputStream fis=new FileInputStream(inputfile);

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        File file = new File (myDir, uri.getLastPathSegment());

        FileOutputStream fos=new FileOutputStream(file);
        //int i;
        byte[] bytesArray=new byte[(int)inputfile.length()];
        fis.read(bytesArray);
        fos.write(cipher.doFinal(bytesArray));
        /*while((i=cipherIn.read())!=-1)
        {
            fos.write(i);

        }*/

    }

    public byte[] decrypt(File file) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {


        cipher.init(Cipher.DECRYPT_MODE,sks);
        //String root = Environment.getInternalStorageDirectory().toString();
        //File myDir = new File(root + "/req_images");
        //String root = Environment.getExternalStorageDirectory().toString();
        //File myDir = new File(root + "/saved_ima ges/encrypted");
       // File file = new File(myDir, filename);
        FileInputStream cin1=new FileInputStream(file);



       // File myDir2=new File(root+"/saved_images");
       // File file2 = new File(myDir2, filename);
       // FileOutputStream fos1=new FileOutputStream(file2);
       // int i1;

        //converting encrypted file to bytes and returning it.
        byte[] bytesArray=new byte[(int)file.length()];
        cin1.read(bytesArray);
        return cipher.doFinal(bytesArray);

        /*while((i1=cin1.read())!=-1){
            //fos1.write(i1);
        }*/

       // return bytesArray;
        }


    /*public void savetoSharedPreference(SharedPreferences sharedPreferences)
    {


        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(uri.getLastPathSegment(),key.toString());

    }*/



}
