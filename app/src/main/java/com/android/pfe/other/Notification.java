package com.android.pfe.other;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SADA INFO on 26/04/2018.
 */

public class Notification implements Serializable {
    private boolean State;

    public Notification(){

    }


    public boolean isState() {
        return State;
    }

    public void setState(boolean state) {
        State = state;
    }


}
