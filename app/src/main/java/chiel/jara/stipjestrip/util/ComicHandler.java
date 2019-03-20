package chiel.jara.stipjestrip.util;

import android.os.Handler;
import android.os.Message;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chiel.jara.stipjestrip.model.Comic;
import chiel.jara.stipjestrip.model.ComicDatasource;

public class ComicHandler extends Handler {

    private ComicAdapter myComicAdapter;

    public ComicHandler(ComicAdapter myComicAdapter) {
        this.myComicAdapter = myComicAdapter;
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
                String name;
                if (fields.has("personnage_s")){
                    name=fields.getString("personnage_s");
                }else {name="No title";}

                String author;
                if (fields.has("auteur_s")){
                    author=fields.getString("auteur_s");
                }else {author="No author";}

                String year;
                if (fields.has("annee")){
                    year=fields.getString("annee");
                }else {year="No year";}

                String imageName;
                if (fields.has("filename")){
                    imageName=fields.getString("filename");
                }else {imageName="noimage.png";}

                String coordinateLAT;
                if (fields.has("coordonnees_geographiques")){
                    JSONArray coordinates = new JSONArray();
                    coordinates=fields.getJSONArray("coordonnees_geographiques");
                    coordinateLAT=coordinates.get(1).toString();
                }else {coordinateLAT="not found";}
                String coordinateLONG;
                if (fields.has("coordonnees_geographiques")){
                    JSONArray coordinates = new JSONArray();
                    coordinates=fields.getJSONArray("coordonnees_geographiques");
                    coordinateLONG=coordinates.get(0).toString();
                }else {coordinateLONG="not found";}
                double currentLONG = Double.valueOf(coordinateLONG);
                double currentLAT = Double.valueOf(coordinateLAT);

                //ToDo HOE imagename gebruiken om effectief al afbeelding te laten zien?

                Comic currentComic = new Comic(name, author, year, imageName, currentLONG,currentLAT);
                ComicDatasource.getInstance().addComic(currentComic);
                index++;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        myComicAdapter.setItems(ComicDatasource.getInstance().getComics());
        myComicAdapter.notifyDataSetChanged();
    }
}
