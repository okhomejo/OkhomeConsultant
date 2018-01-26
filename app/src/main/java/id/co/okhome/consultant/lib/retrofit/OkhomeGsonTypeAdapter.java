package id.co.okhome.consultant.lib.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import id.co.okhome.consultant.lib.retrofit.restmodel.ErrorModel;


/**
 * Created by josongmin on 2016-02-17.
 */
public class OkhomeGsonTypeAdapter implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new MyTypeAdapter<T>(delegate, elementAdapter) {
        }.nullSafe();
    }

    public class MyTypeAdapter<T> extends TypeAdapter<T>{


        TypeAdapter<T> delegate;
        TypeAdapter<JsonElement> elementAdapter;

        public MyTypeAdapter(TypeAdapter<T> delegate, TypeAdapter<JsonElement> elementAdapter){
            this.delegate = delegate;
            this.elementAdapter = elementAdapter;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            delegate.write(out, value);
        }

        @Override
        public T read(JsonReader in) throws IOException {

            JsonElement jsonElement = elementAdapter.read(in);
            if (jsonElement.isJsonObject()){
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject.has("code") && jsonObject.get("code").getAsInt() < 0){

                    String code = jsonObject.get("code").getAsString();
                    String obj = jsonObject.get("obj").getAsString();
                    String size = jsonObject.get("size").getAsString();
                    String message = jsonObject.get("message").getAsString();

                    return (T) new ErrorModel(code, message, obj);

                }

                else if(jsonObject.has("obj") && !jsonObject.get("obj").isJsonNull()){
                    jsonElement = jsonObject.get("obj");
                }
            }

            return delegate.fromJsonTree(jsonElement);
        }
    }


}

