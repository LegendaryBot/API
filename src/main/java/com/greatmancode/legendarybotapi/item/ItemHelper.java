package com.greatmancode.legendarybotapi.item;

import com.greatmancode.legendarybotapi.utils.BattleNetAPIInterceptor;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ItemHelper {
    private static final OkHttpClient clientBattleNet = new OkHttpClient.Builder()
            .addInterceptor(new BattleNetAPIInterceptor())
            .connectionPool(new ConnectionPool(300, 1, TimeUnit.SECONDS))
            .build();

    public static Item getItem(String region, int id) {
        Item item = ItemBackend.getItem(id);

        if (item == null) {
            HttpUrl url = new HttpUrl.Builder().scheme("https")
                    .host(region + ".api.battle.net")
                    .addPathSegments("/wow/item/" + id)
                    .build();
            Request webRequest = new Request.Builder().url(url).build();
            try {
                Response responseBattleNet = clientBattleNet.newCall(webRequest).execute();
                String itemRequest = responseBattleNet.body().string();
                item = new ItemImpl();
                item.setid(id);
                item.setJson(itemRequest);
                ItemBackend.saveItem(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return item;
    }
}
