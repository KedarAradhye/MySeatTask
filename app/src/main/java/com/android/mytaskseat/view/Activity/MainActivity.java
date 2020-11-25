package com.android.mytaskseat.view.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mytaskseat.Model.GetSeatsResponse;
import com.android.mytaskseat.Model.RequestModel;
import com.android.mytaskseat.Network.ApiClient;
import com.android.mytaskseat.Network.ApiInterface;
import com.android.mytaskseat.R;
import com.android.mytaskseat.view.Adapter.SeatsAdapter;
import com.scottyab.aescrypt.AESCrypt;
import com.tozny.crypto.android.AesCbcWithIntegrity;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tozny.crypto.android.AesCbcWithIntegrity.generateKeyFromPassword;
import static com.tozny.crypto.android.AesCbcWithIntegrity.generateSalt;
import static com.tozny.crypto.android.AesCbcWithIntegrity.saltString;

public class MainActivity extends AppCompatActivity {

    private int mNumber;
    private List<String> mKeys = new ArrayList<>();
    private String mKeySha256,mKeyValue, mEncryptKey, mMdEnryptKey,mKeysValues;
    private static final String ALGORITHM = "AES";
    private RecyclerView mSeatsRecyclerView;
    private GridLayoutManager mLayoutManager;
    private SeatsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setViews();

        mKeySha256 = sha256( "1234");

        mMdEnryptKey = md5("sha256",mKeySha256);

        mNumber = CreateRandomNo();

        switch (mNumber) {
            case 1:
                mKeysValues = "MD5";
                mKeyValue = md55( "1234");
                break;

            case 2:
                mKeysValues = "SHA1";
                mKeyValue = sha1( "1234");
                break;

            case 3:
                mKeysValues = "SHA256";
                mKeyValue = sha256( "1234");
                break;

            case 4:
                mKeysValues = "SHA512";
                mKeyValue = sha512( "1234");
                break;
        }

        try {
            mEncryptKey = AESCrypt.encrypt(mMdEnryptKey,String.valueOf(mNumber));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        RequestModel requestModel = new RequestModel();
        requestModel.setAuthKey(mNumber);
        requestModel.setPassword(1234);
        Call<GetSeatsResponse> call = apiInterface.getSeats(mEncryptKey, requestModel);
        call.enqueue(new Callback<GetSeatsResponse>() {
            @Override
            public void onResponse(Call<GetSeatsResponse> call, Response<GetSeatsResponse> response) {
                if(response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        onSeatsResultSuccess(response.body());
                    } else {
                        onSeatsResultFailed(response.message());
                    }
                }else{
                    onSeatsResultFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<GetSeatsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void setViews() {
        mLayoutManager = (new GridLayoutManager(this, 7));
        mSeatsRecyclerView.setLayoutManager(mLayoutManager);
        mSeatsRecyclerView.setNestedScrollingEnabled(false);
        mSeatsRecyclerView.setItemAnimator(null);
        mAdapter = new SeatsAdapter(this);
        mSeatsRecyclerView.setAdapter(mAdapter);
    }

    private void onSeatsResultFailed(String message) {
        Toast.makeText(this, "Error!! Invalid key!! Please try again!!", Toast.LENGTH_SHORT).show();
    }

    private void onSeatsResultSuccess(GetSeatsResponse body) {
        if (body.getSeatMap() != null) {
            mAdapter.setSeats(body.getSeatMap());
        }
    }

    private String sha512(/*String sha512,*/ String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
//            md.update(sha512.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private String sha256(/*String sha256,*/ String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            md.update(sha256.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private String sha1(/*String sha1,*/ String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
//            md.update(sha1.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public String md55(/*String mKeyValue,*/ String password) {
        MessageDigest mdEnc = null;
        String generatedPassword = null;

        try {
            mdEnc = MessageDigest.getInstance("MD5");
            byte[] bytes = mdEnc.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
//        mdEnc.update(mKeyValue.getBytes(), 0, mKeyValue.length());

//        mdEnc.update(password.getBytes(), 0, password.length());
//        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
//        while (md5.length() < 32) {
//            md5 = "0" + md5;
//        }
        return generatedPassword;
    }

    public String md5(String mKeyValue, String password) {
        MessageDigest mdEnc = null;
        String generatedPassword = null;

        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(mKeyValue.getBytes(), 0, mKeyValue.length());
        byte[] bytes = mdEnc.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        generatedPassword = sb.toString();
//        mdEnc.update(password.getBytes(), 0, password.length());
//        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
//        while (md5.length() < 32) {
//            md5 = "0" + md5;
//        }
        return generatedPassword;
    }

    private int CreateRandomNo() {
        Random rn = new Random();
        int answer = rn.nextInt(4) + 1;
        return answer;
    }

    private void initViews() {
        mSeatsRecyclerView = findViewById(R.id.rv_seats);
    }
}