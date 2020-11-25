
package com.android.mytaskseat.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeatMap {

    @SerializedName("SeatRow")
    @Expose
    private String seatRow;

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

}
