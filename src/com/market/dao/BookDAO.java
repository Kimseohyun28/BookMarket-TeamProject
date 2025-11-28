package com.market.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import java.util.ArrayList;

import com.market.bookitem.Book;

public class BookDAO {

    // ============================
    // 1. 전체 조회 (기본 정렬)
    // ============================
    public ArrayList<Book> findAll() {
        return findAll(1);
    }

    // 전체 조회 + 정렬
    public ArrayList<Book> findAll(int sortType) {

        ArrayList<Book> list = new ArrayList<>();

        String sql = "SELECT book_id, name, unit_price, author, description, category, release_date FROM book";
        sql += getOrderBy(sortType);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String bookId = rs.getString("book_id");
                String name = rs.getString("name");
                int unitPrice = rs.getInt("unit_price");
                String author = rs.getString("author");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String releaseDate = rs.getString("release_date");

                Book book = new Book(bookId, name, unitPrice, author, description, category, releaseDate);
                list.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ============================
    // 2. INSERT (중복이면 건너뛰기)
    // ============================
    public int insert(Book book) {
        String sql = "INSERT INTO book "
                   + "(book_id, name, unit_price, author, description, category, release_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getBookId());
            pstmt.setString(2, book.getName());
            pstmt.setInt(3, book.getUnitPrice());
            pstmt.setString(4, book.getAuthor());
            pstmt.setString(5, book.getDescription());
            pstmt.setString(6, book.getCategory());
            pstmt.setString(7, book.getReleaseDate());

            return pstmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("이미 존재하는 도서라 건너뜀: " + book.getBookId());
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ============================
    // 3. 제목 검색
    // ============================
    public ArrayList<Book> findByTitle(String keyword) {
        return findByTitle(keyword, 1);
    }

    public ArrayList<Book> findByTitle(String keyword, int sortType) {
        ArrayList<Book> list = new ArrayList<>();

        String sql = "SELECT book_id, name, unit_price, author, description, category, release_date "
                   + "FROM book WHERE name LIKE ?";
        sql += getOrderBy(sortType);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Book(
                        rs.getString("book_id"),
                        rs.getString("name"),
                        rs.getInt("unit_price"),
                        rs.getString("author"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getString("release_date")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ============================
    // 4. 저자 검색
    // ============================
    public ArrayList<Book> findByAuthor(String keyword) {
        return findByAuthor(keyword, 1);
    }

    public ArrayList<Book> findByAuthor(String keyword, int sortType) {
        ArrayList<Book> list = new ArrayList<>();

        String sql = "SELECT book_id, name, unit_price, author, description, category, release_date "
                   + "FROM book WHERE author LIKE ?";
        sql += getOrderBy(sortType);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Book(
                        rs.getString("book_id"),
                        rs.getString("name"),
                        rs.getInt("unit_price"),
                        rs.getString("author"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getString("release_date")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ============================
    // 5. 카테고리 검색
    // ============================
    public ArrayList<Book> findByCategory(String keyword) {
        return findByCategory(keyword, 1);
    }

    public ArrayList<Book> findByCategory(String keyword, int sortType) {
        ArrayList<Book> list = new ArrayList<>();

        String sql = "SELECT book_id, name, unit_price, author, description, category, release_date "
                   + "FROM book WHERE category LIKE ?";
        sql += getOrderBy(sortType);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Book(
                        rs.getString("book_id"),
                        rs.getString("name"),
                        rs.getInt("unit_price"),
                        rs.getString("author"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getString("release_date")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ============================
    // 6. 정렬 문자열 생성
    // ============================
    private String getOrderBy(int sortType) {
        switch (sortType) {
            case 2:  return " ORDER BY unit_price ASC";
            case 3:  return " ORDER BY unit_price DESC";
            case 4:  return " ORDER BY release_date DESC";
            default: return " ORDER BY book_id ASC";
        }
    }

    // ============================
    // 7. 도서 1권 조회 (findById)
    // ============================
    public Book findById(String bookId) {

        String sql = "SELECT book_id, name, unit_price, author, description, category, release_date "
                   + "FROM book WHERE book_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getString("book_id"),
                        rs.getString("name"),
                        rs.getInt("unit_price"),
                        rs.getString("author"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getString("release_date")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ============================
    // 8. 도서 수정
    // ============================
    public int update(Book book) {

        String sql = "UPDATE book SET "
                   + "name = ?, unit_price = ?, author = ?, description = ?, "
                   + "category = ?, release_date = ? WHERE book_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getName());
            pstmt.setInt(2, book.getUnitPrice());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getDescription());
            pstmt.setString(5, book.getCategory());
            pstmt.setString(6, book.getReleaseDate());
            pstmt.setString(7, book.getBookId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ============================
    // 9. 도서 삭제
    // ============================
    public int delete(String bookId) {

        String sql = "DELETE FROM book WHERE book_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookId);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
