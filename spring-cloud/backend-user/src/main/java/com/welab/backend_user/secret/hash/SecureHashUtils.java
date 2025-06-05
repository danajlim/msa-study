package com.welab.backend_user.secret.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//비밀번호 등의 문자열을 해싱(hash) 하거나, 입력한 원문이 해시된 값과 일치하는지 비교하는 기능
public class SecureHashUtils {

    //전달받은 message를 해시 알고리즘으로 암호화
    public static String hash(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(message.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    //로그인 시 입력 비밀번호 검증에 사용 - 원본 문자열 다시 해싱하고 저장된 hashedMessage 와 비교
    public static boolean matches(String message, String hashedMessage) {
        String hashed = hash(message);
        return hashed.equals(hashedMessage);
    }
}