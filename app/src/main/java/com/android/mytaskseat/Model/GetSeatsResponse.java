
package com.android.mytaskseat.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSeatsResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("seat_map")
    @Expose
    private List<SeatMap> seatMap = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SeatMap> getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(List<SeatMap> seatMap) {
        this.seatMap = seatMap;
    }

}
