package com.google.zxing.client.android.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.github.barcodeeye.CardPresenter;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ProductParsedResult;

/**
 * Handles generic products which are not books.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ProductResultHandler extends ResultHandler<ProductParsedResult> {

    public static final HashMap<String, String> PRODUCT_SEARCH_ENDPOINTS = new HashMap<String, String>();

    static {
        PRODUCT_SEARCH_ENDPOINTS.put("Google", "https://www.google.com/search?hl=en&tbm=shop&q={CODE}");
        PRODUCT_SEARCH_ENDPOINTS.put("Amazon", "http://www.amazon.com/s/?field-keywords={CODE}");
        PRODUCT_SEARCH_ENDPOINTS.put("eBay", "http://www.ebay.com/sch/i.html?_nkw={CODE}");
    }

    public ProductResultHandler(Context context, ProductParsedResult parsedResult, Result result) {//, Uri photoUri) {
        super(context, parsedResult, result);//, photoUri);
    }

    @Override
    public List<CardPresenter> getCardResults() {
        List<CardPresenter> cardPresenters = new ArrayList<CardPresenter>();

        ParsedResult parsedResult = getParsedResult();
        String codeValue = parsedResult.getDisplayResult();

        for (String key : PRODUCT_SEARCH_ENDPOINTS.keySet()) {
            CardPresenter cardPresenter = new CardPresenter();
            cardPresenter.setText("Lookup on " + key).setFooter(codeValue);

            String url = PRODUCT_SEARCH_ENDPOINTS.get(key);
            url = url.replace("{CODE}", codeValue);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            cardPresenter.setPendingIntent(createPendingIntent(getContext(), intent));

            cardPresenters.add(cardPresenter);
        }

        return cardPresenters;
    }
}
