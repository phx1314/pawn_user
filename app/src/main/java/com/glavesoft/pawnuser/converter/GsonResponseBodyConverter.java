package com.glavesoft.pawnuser.converter;

import com.glavesoft.okGo.Convert;
import com.glavesoft.pawnuser.base.Base64;
import com.glavesoft.pawnuser.base.RSAUtils;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.apache.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static kotlin.text.Charsets.UTF_8;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String jsonString="{}";
        try {
              jsonString = new String(RSAUtils.decryptByPublicKey(Base64.decode(value.string()), RSAUtils.PUBLIC_KEY), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
//            return adapter.read(jsonReader);
            return  adapter.fromJson(jsonString);
//            return Convert.fromJson(jsonString, null);
        } finally {
            value.close();
        }
    }
}
