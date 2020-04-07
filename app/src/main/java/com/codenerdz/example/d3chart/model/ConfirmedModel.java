package com.codenerdz.example.d3chart.model;

import java.io.Serializable;

public class ConfirmedModel implements Serializable {
    private static final long serialVersionUID = 4990064484446119372L;

    public String date;
    public String count;

    public ConfirmedModel(String date, String count)
    {
        this.date = date;
        this.count = count;
    }
}
