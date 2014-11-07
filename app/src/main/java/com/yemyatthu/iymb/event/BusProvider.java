package com.yemyatthu.iymb.event;

import com.squareup.otto.Bus;

/**
 * Created by yemyatthu on 11/7/14.
 */
public class BusProvider {
  private static final Bus BUS = new Bus();

  public BusProvider() {
  }

  public static Bus getInstance() {
    return BUS;
  }
}
