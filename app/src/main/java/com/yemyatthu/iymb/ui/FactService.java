package com.yemyatthu.iymb.ui;

import com.yemyatthu.iymb.model.Fact;
import java.util.List;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by yemyatthu on 11/7/14.
 */
public interface FactService {
  @GET("/") void getFact(Callback<List<Fact>> factCallBack);
}
