package com.market.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AladinAPI {

    private static final String TTBKEY = "ttbrlatjgus12282156001";

    // 1) QueryType으로 가져오기 (베스트셀러, 신간, 추천 등)
    public static String getItemList(String queryType, int max) {

        String urlStr =
            "https://www.aladin.co.kr/ttb/api/ItemList.aspx?"
            + "ttbkey=" + TTBKEY
            + "&QueryType=" + queryType
            + "&MaxResults=" + max
            + "&start=1"
            + "&SearchTarget=Book"
            + "&output=js"
            + "&Version=20131101";

        return httpGet(urlStr);
    }

    // 2) 키워드로 검색하여 도서 가져오기 (IT, 경제 등)
    public static String searchItem(String keyword, int max) {

        String urlStr =
            "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?"
            + "ttbkey=" + TTBKEY
            + "&Query=" + keyword
            + "&QueryType=Keyword"
            + "&MaxResults=" + max
            + "&start=1"
            + "&SearchTarget=Book"
            + "&output=js"
            + "&Version=20131101";

        return httpGet(urlStr);
    }

    // 공통 HTTP GET 처리
    private static String httpGet(String urlStr) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"))) {

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }
}
