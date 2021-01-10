package com.android.mytaskseat.view.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mytaskseat.Model.GetSeatsResponse;
import com.android.mytaskseat.Model.SeatMap;
import com.android.mytaskseat.Network.ApiClient;
import com.android.mytaskseat.Network.ApiInterface;
import com.android.mytaskseat.R;
import com.android.mytaskseat.view.Adapter.SeatsAdapter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tozny.crypto.android.AesCbcWithIntegrity.generateKeyFromPassword;

public class MainActivity extends AppCompatActivity {

    private int mNumber;
    private List<String> mKeys = new ArrayList<>();
    private String mKeySha256, mKeyValue, mEncryptKey, mMdEnryptKey, mKeysValues, mDecyptKey;
    private static final String ALGORITHM = "AES";
    private List<SeatMap> mSeatMap = new ArrayList<>();
    private List<String> mAllSeats = new ArrayList<>();
    List<String> mSeats = new ArrayList<String>();
    int seatSize = 100;
    int seatGaping = 10;
    TableLayout layout;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mKeySha256 = sha256("1234");

        mMdEnryptKey = md5(mKeySha256);

        mNumber = CreateRandomNo();

        switch (mNumber) {
            case 1:
                mKeysValues = "MD5";
                mKeyValue = md5(mMdEnryptKey);
                break;

            case 2:
                mKeysValues = "SHA1";
                mKeyValue = sha1(mMdEnryptKey);
                break;

            case 3:
                mKeysValues = "SHA256";
                mKeyValue = sha256(mMdEnryptKey);
                break;

            case 4:
                mKeysValues = "SHA512";
                mKeyValue = sha512(mMdEnryptKey);
                break;

        }


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Map<String, String> params = new HashMap<String, String>();
        params.put("auth_key", String.valueOf(mNumber));
        params.put("password", "1234");

        Call<GetSeatsResponse> call = apiInterface.getSeats(mKeyValue, params);
        call.enqueue(new Callback<GetSeatsResponse>() {
            @Override
            public void onResponse(Call<GetSeatsResponse> call, Response<GetSeatsResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        onSeatsResultSuccess(response.body());
                    } else {
                        onSeatsResultFailed(response.message());
                    }
                } else {
                    onSeatsResultFailed(response.message());
                }
            }

            @Override
            public void onFailure(Call<GetSeatsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void onSeatsResultFailed(String message) {
        Toast.makeText(this, "Error!! Invalid key!! Please try again!!", Toast.LENGTH_SHORT).show();
    }

    private void onSeatsResultSuccess(GetSeatsResponse body) {
        if (body.getSeatMap() != null) {
            mSeatMap = body.getSeatMap();

            mAllSeats.add(mSeatMap.get(0).getSeatRow1());
            mAllSeats.add(mSeatMap.get(1).getSeatRow2());
            mAllSeats.add(mSeatMap.get(2).getSeatRow3());
            mAllSeats.add(mSeatMap.get(3).getSeatRow4());
            mAllSeats.add(mSeatMap.get(4).getSeatRow5());            mAllSeats.add(mSeatMap.get(5).getSeatRow6());
            mAllSeats.add(mSeatMap.get(6).getSeatRow7());
            mAllSeats.add(mSeatMap.get(7).getSeatRow8());
            mAllSeats.add(mSeatMap.get(8).getSeatRow9());
            mAllSeats.add(mSeatMap.get(9).getSeatRow10());
            mAllSeats.add(mSeatMap.get(10).getSeatRow11());
            mAllSeats.add(mSeatMap.get(11).getSeatRow12());
            mAllSeats.add(mSeatMap.get(12).getSeatRow13());


            for (int i = 0; i < mAllSeats.size(); i++) {
                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                layout.addView(row, i);
                String mSeatStrings = mAllSeats.get(i);
                mSeats = Arrays.asList(mSeatStrings.split(","));
                for (int j = 0; j < mSeats.size(); j++) {
                    if (!mSeats.get(j).equals("0")) {
                        String[] parts = mSeats.get(j).split("-");
                        TextView tv = new TextView(this);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(seatSize, seatSize);
                        tv.setLayoutParams(params);
                        tv.setPadding(0, 0, 0, 2 * seatGaping);
                        tv.setGravity(Gravity.CENTER);
                        tv.setBackgroundResource(R.drawable.ic_seats_book);
                        tv.setTextColor(Color.BLACK);
                        tv.setText(parts[3]);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                        row.addView(tv, j);
                    } else {
                        TextView tv = new TextView(this);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(seatSize, seatSize);
                        tv.setLayoutParams(params);
                        tv.setPadding(0, 0, 0, 2 * seatGaping);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextColor(Color.WHITE);
                        tv.setText("");
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                        row.addView(tv, j);
                    }
                }
            }

        }
    }

    private String sha512( String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
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

    private String sha256( String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
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

    private String sha1( String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
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

    public String md5(String mKeyValue) {
        MessageDigest mdEnc = null;
        String generatedPassword = null;

        try {
            mdEnc = MessageDigest.getInstance("MD5");
            byte[] bytes = mdEnc.digest(mKeyValue.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private int CreateRandomNo() {
        Random rn = new Random();
        int answer = rn.nextInt(4) + 1;
        return answer;
    }

    private void initViews() {
        layout = findViewById(R.id.layoutSeat);
    }
}