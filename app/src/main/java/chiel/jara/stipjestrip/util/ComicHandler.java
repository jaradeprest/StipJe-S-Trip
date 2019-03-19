package chiel.jara.stipjestrip.util;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

public class ComicHandler extends Handler {

    private ComicHandler myComicHandler;

    public ComicHandler(ComicHandler myComicHandler) {
        this.myComicHandler = myComicHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String data = (String) msg.obj;

        try {
            JSONObject rootObject = new JSONObject(data);
            JSONArray records = rootObject.getJSONArray("records");
            int aantalComics = records.length();
            int index = 0;
            //NOG NIET KLAAR
        }
    }
}
