package id.co.okhome.consultant.exception;

import java.util.List;

/**
 * Created by josongmin on 2016-08-27.
 */
public class OkhomeException extends Exception {
    public Integer code, size = 0;
    public String message;
    public Object obj;

    public OkhomeException(String message) {
        init(0, message, null);
    }

    public OkhomeException(Integer code, String message) {
        init(code, message, null);
    }

    public OkhomeException(Integer code, String message, Object obj) {
        init(code, message, obj);
    }

    private void init(Integer code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;

        if(obj == null)
            return;

        if(obj instanceof List){
            this.size = ((List) obj).size();
        }else{
            this.size = 1;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**check exception*/
    public final static void chkException(boolean expression, String err) throws OkhomeException{
        if(expression){
            throw new OkhomeException(-100, err);
        }

    }

}
