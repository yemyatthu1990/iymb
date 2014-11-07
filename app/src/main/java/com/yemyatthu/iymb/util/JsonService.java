package com.yemyatthu.iymb.util;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yemyatthu.iymb.model.Fact;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by YMT on 10/14/2014.
 */

public class JsonService {

  public static String convertToJson(List<Fact> facts) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    return gson.toJson(facts);
  }

  public static List<Fact> convertToJava(String jsonString) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();
    Type type = new TypeToken<List<Fact>>() {
    }.getType();
    return gson.fromJson(jsonString, type);
  }

  //Write Json to Local Storage
  public static void saveData(Context context, String jsonString, String fileName) {
    OutputStream outputStream = null;
    try {
      outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      outputStream.write(jsonString.getBytes());
      outputStream.close();
    } catch (FileNotFoundException exception) {
      Log.d("FileNotFound", "File not found Error");
    } catch (IOException ioException) {
      Log.d("IOError", "IO Error");
    }
  }

  //Load Json from Local Storage
  public static String loadData(Context context, String fileName) {
    StringBuilder builder = null;
    InputStream inputStream = null;
    try {
      inputStream = context.openFileInput(fileName);
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      builder = new StringBuilder();
      String line = null;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
      reader.close();
    } catch (FileNotFoundException fileNotFoundExcepiton) {
      Log.d("FileNotFound", "No File Found");
    } catch (IOException ioException) {
      Log.d("IO Exception", "IO Exception");
    }
    assert builder != null;
    return builder.toString();
  }
}
