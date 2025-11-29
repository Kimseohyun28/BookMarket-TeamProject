package com.market.main;

import com.google.gson.*;
import com.market.api.AladinAPI;
import com.market.dao.BookDAO;
import com.market.bookitem.Book;
import java.util.ArrayList;

public class AladinTest {

    public static void main(String[] args) {

        System.out.println("==== 알라딘 도서 자동 수집 시작 ====");

        // 여러 분야 자동 수집
        collect("Bestseller", "베스트셀러");
        collect("ItemNewAll", "신간");
        collect("ItemEditorChoice", "추천");

        // 키워드 검색 기반 수집 (IT, 경제)
        collectKeyword("IT", "IT");
        collectKeyword("경제", "경제");

        System.out.println("==== 도서 수집 완료 ====");
    }


    // QueryType 기반 수집
    private static void collect(String queryType, String categoryName) {

        System.out.println("\n[" + categoryName + " 수집]");
        String json = AladinAPI.getItemList(queryType, 30);

        saveToDB(json, categoryName);
    }


    // 키워드 기반 수집
    private static void collectKeyword(String keyword, String categoryName) {

        System.out.println("\n[" + categoryName + " 도서 수집]");
        String json = AladinAPI.searchItem(keyword, 30);

        saveToDB(json, categoryName);
    }


    // JSON → Book 객체 → DB 저장
    private static void saveToDB(String json, String category) {

        if (json == null) {
            System.out.println("API 오류로 스킵");
            return;
        }

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray items = root.getAsJsonArray("item");

        if (items == null) return;

        BookDAO dao = new BookDAO();

        for (JsonElement el : items) {

            JsonObject obj = el.getAsJsonObject();

            String isbn = obj.get("isbn").getAsString();
            String title = obj.get("title").getAsString();
            String author = obj.get("author").getAsString();
            String desc = obj.get("description").getAsString();
            String pub = obj.get("pubDate").getAsString();
            int price = obj.get("priceStandard").getAsInt();

            Book book = new Book(isbn, title, price, author, desc, category, pub);

            dao.insert(book);
        }

    }
}

