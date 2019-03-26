package chiel.jara.stipjestrip.util.bar_util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chiel.jara.stipjestrip.model.bar_model.Bar;
import chiel.jara.stipjestrip.model.bar_model.BarDatabase;

public class BarHandler extends Handler {

    private Context context;

    public BarHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String data = (String) msg.obj;

        try {
            JSONArray bars = new JSONArray(data);
            int aantalBars = bars.length();
            int index = 0;
            while (index < aantalBars) {
                JSONObject records = bars.getJSONObject(index);
                String name, street, houseNumber, postalCode, cityName, phone, website, description;

                if (!records.getString("postal_code").equals("1000")){
                    index++;
                    continue;
                }

                if (records.has("Name")) {
                    name = records.getString("Name");
                    Log.i("TEST", "" + name);
                } else {
                    name = "No name";
                }

                if (records.has("Street")) {
                    street = records.getString("Street");
                } else {
                    street = "No Adress";
                }

                if (records.has("House_number")) {
                    houseNumber = records.getString("House_number");
                } else {
                    houseNumber = "No house number";
                }

                if (records.has("postal_code")) {
                    postalCode = records.getString("postal_code");
                } else {
                    postalCode = "No postal code";
                }

                if (records.has("city_name")) {
                    cityName = records.getString("city_name");
                } else {
                    cityName = "No city name";
                }

                if (records.has("phone")) {
                    phone = records.getString("phone");
                } else {
                    phone = "No phone number";
                }

                if (records.has("Website")) {
                    website = records.getString("Website");
                } else {
                    website = "No website";
                }

                if (records.has("description_en")) {
                    description = records.getString("description_en");
                } else {
                    description = "No desciption available";
                }


                Bar currentBar = new Bar(name, street, houseNumber, postalCode, cityName, phone, website, description);

                boolean barExist = false;
                for (Bar bar : BarDatabase.getInstance(context).getMethodsBar().getAllBars()) {
                    if (bar.getName().equals(currentBar.getName())) {
                        barExist = true;
                        break;
                    }
                }
                if (!barExist) {
                    BarDatabase.getInstance(context).getMethodsBar().addBar(currentBar);
                }

                index++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
