package chiel.jara.stipjestrip.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import chiel.jara.stipjestrip.model.Comic;
import chiel.jara.stipjestrip.model.ComicDatabase;

/**
 * Created By Chiel&Jara 03/2019
 */

public class ComicHandler extends Handler {

    private Context context;

    public ComicHandler( Context context) {
        this.context = context;
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
            while (index<aantalComics){
                JSONObject currentRecord = records.getJSONObject(index);//=huidige rij
                JSONObject fields = currentRecord.getJSONObject("fields");//=wat zit er in de rij?
                String name, author, year, imageName, imageID, coordinateLAT, coordinateLONG;
                if (fields.has("personnage_s")){
                    name=fields.getString("personnage_s");
                }else {name="No title";}

                if (fields.has("auteur_s")){
                    author=fields.getString("auteur_s");
                }else {author="No author";}

                if (fields.has("annee")){
                    year=fields.getString("annee");
                }else {year="No year";}

                JSONObject photo = fields.getJSONObject("photo");
                if (photo.has("filename")){
                    imageName=photo.getString("filename");
                }else {imageName="noimage.png";}

                if (photo.has("id")){
                    imageID=photo.getString("id");
                }else{imageID="noID";}

                if (fields.has("coordonnees_geographiques")){
                    JSONArray coordinates = new JSONArray();
                    coordinates=fields.getJSONArray("coordonnees_geographiques");
                    coordinateLAT=coordinates.get(0).toString();
                }else {coordinateLAT="not found";}

                if (fields.has("coordonnees_geographiques")){
                    JSONArray coordinates = new JSONArray();
                    coordinates=fields.getJSONArray("coordonnees_geographiques");
                    coordinateLONG=coordinates.get(1).toString();
                }else {coordinateLONG="not found";}
                double currentLONG = Double.valueOf(coordinateLONG);
                double currentLAT = Double.valueOf(coordinateLAT);

                Comic currentComic = new Comic(name, author, year, imageName, imageID, currentLONG, currentLAT);

                //AFBEELDING URL
                String URL = "https://bruxellesdata.opendatasoft.com/explore/dataset/comic-book-route/files/"+currentComic.getImgID()+"/300/";
                currentComic.setURLimg(URL);
                //downloadTASK voor afbeelding
                DownloadImageTask task = new DownloadImageTask(currentComic.getImgID(), context);
                task.execute(currentComic.getURLimg());
                currentComic.setImgID(task.get());

                boolean comicExist = false;
                for (Comic comic : ComicDatabase.getInstance(context).getMethodsComic().getAllComics()) {
                    if (comic.getImgID().equals(currentComic.getImgID())) {
                        comicExist = true;
                        break;
                    }
                }
                if (!comicExist) {
                    ComicDatabase.getInstance(context).getMethodsComic().addComic(currentComic);
                }

                index++;
            }
        }catch (JSONException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    //afb downloaden
    private static class DownloadImageTask extends AsyncTask<String, Void, String>{

        private String name;
        private WeakReference<Context> contextReference;

        DownloadImageTask(String name, Context context) {
            this.name = name;
            contextReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                InputStream inputStream = new URL(strings[0]).openStream();    // Download Image from URL
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();

                FileOutputStream foStream = contextReference.get().openFileOutput(name+".jpg", Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foStream);
                foStream.close();

                return name+".jpg";

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
