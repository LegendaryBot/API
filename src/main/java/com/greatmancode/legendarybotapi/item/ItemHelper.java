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
        System.out.println("THE ID " + id);
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
                item = new ItemImpl();
                item.setid(id);
                item.setJson(itemRequest);
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
