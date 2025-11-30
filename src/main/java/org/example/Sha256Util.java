package org.example;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


public final class Sha256Util {

    private Sha256Util() { /* 유틸성 클래스, 인스턴스화 방지 */ }

    public static String hash(String input) {
        Objects.requireNonNull(input, "input must not be null");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256은 표준 알고리즘이므로 여기로 오지 않지만 예외 처리 유지
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * 평문과 기존 해시(16진수 문자열)를 비교하여 일치하면 true 반환.
     * 해시값은 복호화가 불가능하므로 직접 비교(비교 결과로 일치 여부 판단).
     * @param input 평문
     * @param expectedHexHash 기존에 저장된 해시 (16진수 문자열)
     * @return 일치하면 true
     */
    public static boolean matches(String input, String expectedHexHash) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(expectedHexHash, "expectedHexHash must not be null");
        String computed = hash(input);
        return constantTimeEquals(computed, expectedHexHash);
    }

    /* ----------------- 내부 헬퍼들 ----------------- */

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 타이밍 공격을 조금 방지하기 위한 상수 시간 비교.
     */
    private static boolean constantTimeEquals(String a, String b) {
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
