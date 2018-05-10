package com.greatmancode.legendarybotapi.item;

import com.greatmancode.legendarybotapi.utils.BattleNetAPIInterceptor;
import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ItemHelper {
    private static final OkHttpClient clientBattleNet = new OkHttpClient.Builder()
            .addInterceptor(new BattleNetAPIInterceptor())
            .connectionPool(new ConnectionPool(300, 1, TimeUnit.SECONDS))
            .build();

    public static Item getItem(String region, long id) {
        Item item = ItemBackend.getItem(id);

        if (item == null) {
            item = getItemWeb(region, id,false);
        }
        return item;
    }

    private static Item getItemWeb(String region, long id, boolean alreadyTried) {
        Item item = null;
        HttpUrl url = new HttpUrl.Builder().scheme("https")
                .host(region + ".api.battle.net")
                .addPathSegments("/wow/item/" + id)
                .build();
        Request webRequest = new Request.Builder().url(url).build();
        try {
            Response responseBattleNet = clientBattleNet.newCall(webRequest).execute();
            if (responseBattleNet.code() == 200) {
                String itemRequest = responseBattleNet.body().string();
                JSONObject itemObject = new JSONObject(itemRequest);
                JSONObject finalObjectJSON = new JSONObject();
                finalObjectJSON.put("id", id);
                finalObjectJSON.put("name", itemObject.getString("name"));
                finalObjectJSON.put("itemSpells",itemObject.getJSONArray("itemSpells"));
                finalObjectJSON.put("quality", itemObject.getInt("quality"));
                finalObjectJSON.put("icon", itemObject.getString("icon"));
                finalObjectJSON.put("itemSubClass", itemObject.getInt("itemSubClass"));
                finalObjectJSON.put("inventoryType", itemObject.getInt("inventoryType"));
                if (!itemObject.getString("description").equalsIgnoreCase("")) {
                    finalObjectJSON.put("description", itemObject.getString("description"));
                }

                item = new ItemImpl();
                item.setid(id);
                item.setJson(finalObjectJSON.toString());
                ItemBackend.saveItem(item);
            }

        } catch (IOException e) {
            e.printStackTrace();
            //It timeouted, try again
            if (!alreadyTried) {
                getItemWeb(region,id,true);
            }
            UncaughtExceptionHandler.getHandler().sendException(e, "region:" + region, "id:" + id);
        }
        return item;
    }
}
