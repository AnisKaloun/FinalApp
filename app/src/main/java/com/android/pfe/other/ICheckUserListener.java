package com.android.pfe.other;

import java.util.Map;

/**
 * Created by SADA INFO on 19/04/2018.
 */

interface ICheckUserListener {
  void onSuccess (Map value);
  void onError(Exception e);
}
