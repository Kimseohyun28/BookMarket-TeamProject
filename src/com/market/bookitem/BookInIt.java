package com.market.bookitem;

import com.market.dao.BookDAO;
import java.util.ArrayList;

public class BookInIt {
    private static ArrayList<Book> mBookList;
    private static int mTotalBook = 0;

    // 프로그램 시작 시 한 번만 호출해서 DB에서 책 목록 불러오기
    public static void init() {
        BookDAO dao = new BookDAO();
        mBookList = dao.findAll();     // DB에서 전체 도서 목록 조회
        mTotalBook = mBookList.size(); // 총 도서 개수
    }

    public static ArrayList<Book> getmBookList() {
        return mBookList;
    }

    public static void setmBookList(ArrayList<Book> mBookList) {
        BookInIt.mBookList = mBookList;
        BookInIt.mTotalBook = (mBookList != null) ? mBookList.size() : 0;
    }

    public static int getmTotalBook() {
        return mTotalBook;
    }

    public static void setmTotalBook(int mTotalBook) {
        BookInIt.mTotalBook = mTotalBook;
    }
}
