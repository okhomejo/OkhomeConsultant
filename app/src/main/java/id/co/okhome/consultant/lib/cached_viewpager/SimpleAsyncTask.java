package id.co.okhome.consultant.lib.cached_viewpager;

import android.os.AsyncTask;

/**
 * Created by jo on 2018-04-18.
 */

public abstract class SimpleAsyncTask<T> extends AsyncTask{

    @Override
    protected Object doInBackground(Object[] objects) {
        return doBackground();
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        onFinish((T)o);
    }

    public void start(){
        execute("");
    }

    abstract public T doBackground();
    abstract public void onFinish(T result);
}
