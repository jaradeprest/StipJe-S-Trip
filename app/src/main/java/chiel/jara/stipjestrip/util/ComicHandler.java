package chiel.jara.stipjestrip.util;

import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Target;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import chiel.jara.stipjestrip.model.Comic;
import chiel.jara.stipjestrip.model.ComicDatabase;

import static android.content.Context.MODE_PRIVATE;

public class ComicHandler extends Handler {

    private Context context;
    private ComicAdapter myComicAdapter;

    public ComicHandler(ComicAdapter myComicAdapter, Context context) {
        this.myComicAdapter = myComicAdapter;
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

                Comic currentComic = new Comic(name, author, year, imageName, imageID, currentLONG,currentLAT);

                //AFBEELDING INLADEN
                String URL = "https://bruxellesdata.opendatasoft.com/explore/dataset/comic-book-route/files/"+currentComic.getImgID()+"/300/";
                currentComic.setURLimg(URL);

                //TODO AFBEELDING OPSLAAN
                /*Intent intent = new Intent();
                File photoFile;
                File storage = context.getFilesDir();
                photoFile = new File(storage+"/"+currentComic.getImageName()+"jpg");
                Uri fotoURI = FileProvider.getUriForFile(context, "chiel.jara.stipjestrip.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);*/


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
        }

        myComicAdapter.setComics(ComicDatabase.getInstance(context).getMethodsComic().getAllComics());
        myComicAdapter.notifyDataSetChanged();
    }
}
