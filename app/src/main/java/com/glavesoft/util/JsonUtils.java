package com.glavesoft.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
* 文 件 名 : JsonMethed
* 创 建 人：gejian
* 日     期：2013-11-1
* 修 改 人：gejian
* 日    期：2013-11-1
* 描    述：Json数据处理类
*/
public class JsonUtils {

	/**
	 * 获取Json元素
	 * @param jsonString
	 * @return
	 */
	public static JsonElement getJsonElement(String jsonString){
		try{
			JsonElement jsonElement = new JsonParser().parse(jsonString);
			if(jsonElement != null && !jsonElement.isJsonNull()){
				return jsonElement;
			}
		}catch (JsonParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取JsonArray
	 * @param jsonElement
	 * @return
	 */
	public static JsonArray getJsonArray(JsonElement jsonElement){
		if(jsonElement != null &&!jsonElement.isJsonNull()&&jsonElement.isJsonArray()&& jsonElement.getAsJsonArray().size()!=0){
			return jsonElement.getAsJsonArray();
		}
		return null;
	}

	/**
	 * 获取JsonObject
	 * @param jsonElement
	 * @return
	 */
	public static JsonObject getJsonObject(JsonElement jsonElement){
		if(jsonElement != null && !jsonElement.isJsonNull()&&jsonElement.isJsonObject()){
			return jsonElement.getAsJsonObject();
		}
		return null;
	}

	/**
	 * 获取JsonString
	 * @param jsonElement
	 * @return
	 */
	public static String getJsonString(JsonElement jsonElement){
		if(jsonElement != null && !jsonElement.isJsonNull()&&jsonElement.isJsonPrimitive()){
			return jsonElement.getAsString();
		}
		return ""; //默认""
	}

	/**
	 * 获取JsonInt
	 * @param jsonElement
	 * @return
	 */
	public static Integer getJsonInt(JsonElement jsonElement){
		if(jsonElement != null && !jsonElement.isJsonNull()&&jsonElement.isJsonPrimitive()){
			boolean isInt = true;
			try{
				int num = jsonElement.getAsInt();
				return num;
			}catch (JsonParseException e) {
				isInt = false;

			}catch (Exception e) {
			}
			if(!isInt){
				try{
					String str = jsonElement.getAsString();
					if(str != null && !str.equals("null") && !str.equals("") ){
						return Integer.valueOf(str);
					}
				}catch (JsonParseException e) {
				}catch (Exception e) {
				}
			}
		}
		return 0; //默认==0
	}

	/**
	 * 获取JsonLong
	 * @param jsonElement
	 * @return
	 */
	public static long getJsonLong(JsonElement jsonElement){
		if(jsonElement != null && !jsonElement.isJsonNull()&&jsonElement.isJsonPrimitive()){
			boolean isLong = true;
			try{
				long num = jsonElement.getAsLong();
				return num;
			}catch (JsonParseException e) {
				isLong = false;
			}catch (Exception e) {
			}
			if(!isLong){
				try{
					String str = jsonElement.getAsString();
					if(str != null || !str.equals("null") || !str.equals("") ){
						return Long.valueOf(str);
					}
				}catch (JsonParseException e) {
				}catch (Exception e) {
				}
			}
		}
		return 0; //默认==0
	}

	/**
	 * 获取JsonBoolean
	 * @param jsonElement
	 * @return
	 */
	public static Boolean getJsonBoolean(JsonElement jsonElement){
		if(jsonElement != null && !jsonElement.isJsonNull()&&jsonElement.isJsonPrimitive()){
			return jsonElement.getAsBoolean();
		}
		return false;  //默认false
	}

	/**
	 * 获取JsonDouble
	 * @param jsonElement
	 * @return
	 */
	public static double getJsonDouble(JsonElement jsonElement){
		if(jsonElement != null && !jsonElement.isJsonNull()&&jsonElement.isJsonPrimitive()){
			boolean isdouble = true;
			try{
				double num = jsonElement.getAsDouble();
				return num;
			}catch (JsonParseException e) {
				isdouble = false;
			}catch (Exception e) {
			}
			if(!isdouble){
				try{
					String str = jsonElement.getAsString();
					if(str != null || !str.equals("null") || !str.equals("") ){
						return Double.parseDouble(str);
					}
				}catch (JsonParseException e) {
				}catch (Exception e) {
				}
			}
		}
		return 0; //默认==0
	}

	/**
	 * 获取JsonFloat
	 * @param jsonElement
	 * @return
	 */
	public static float getJsonFloat(JsonElement jsonElement){
		if(jsonElement != null && !jsonElement.isJsonNull()&&jsonElement.isJsonPrimitive()){
			boolean isfloat = true;
			try{
				float num = jsonElement.getAsFloat();
				return num;
			}catch (JsonParseException e) {
				isfloat = false;
			}catch (Exception e) {
			}
			if(!isfloat){
				try{
					String str = jsonElement.getAsString();
					if(str != null || !str.equals("null") || !str.equals("") ){
						return Float.parseFloat(str);
					}
				}catch (JsonParseException e) {
				}catch (Exception e) {
				}
			}
		}
		return 0; //默认==0
	}
}
