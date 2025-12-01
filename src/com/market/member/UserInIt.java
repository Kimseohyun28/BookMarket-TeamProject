package com.market.member;

public class UserInIt {

    private static User mUser;
    private static String sessionId;  // 장바구니용 세션 ID
    private static int userId;        // DB users.user_id 저장용

    public static void setmUser(User mUser) {
        UserInIt.mUser = mUser;
    }

    // 손님 정보 입력 시 호출 (GuestWindow)
    public static void init(String name, String phone) {
        mUser = new User(name, phone);

        // 🔹 sessionId 생성 (장바구니 DB 저장/복원용)
        String digits = phone.replaceAll("\\D", ""); 
        sessionId = name + "_" + digits;

        // 🔹 DB에 아직 없는 상태로 초기화 (-1)
        userId = -1;
    }

    public static User getmUser() {
        return mUser;
    }

    // ================================
    // 🔹 장바구니 DB용: 세션 ID
    // ================================
    public static String getSessionId() {
        return sessionId;
    }

    // ================================
    // 🔹 주문 내역 조회용: userId(DB)
    // ================================
    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int id) {
        userId = id;
    }
}
