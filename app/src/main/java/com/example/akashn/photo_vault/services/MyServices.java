package com.example.akashn.photo_vault.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import com.example.akashn.photo_vault.Classes.MyImage;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MyServices extends IntentService {

    public MyServices(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent



    }



}
