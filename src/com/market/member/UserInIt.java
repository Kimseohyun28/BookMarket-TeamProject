package com.market.member;

public class UserInIt {

    private static User mUser;
    private static String sessionId; // 세션 ID 보관

    public static void setmUser(User mUser) {
        UserInIt.mUser = mUser;
    }

    // name + phone 입력될 때 User 생성 + sessionId 생성도 같이 해야 함
    public static void init(String name, String phone) {
        mUser = new User(name, phone);

        // 전화번호에서 숫자만 추출 (010-1234-5678 → 01012345678)
        String digits = phone.replaceAll("\\D", "");

        // 세션 ID 생성 규칙: 이름_전화번호
        sessionId = name + "_" + digits;
    }

    public static User getmUser() {
        return mUser;
    }

    // 세션 ID getter
    public static String getSessionId() {
        return sessionId;
    }
}
