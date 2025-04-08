package br.com.univali.controleserivices.config.utils;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashFileUtils {
        public static String createHash(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

                StringBuilder sb = new StringBuilder();
                for (byte aByte : bytes) {
                    sb.append(String.format("%02x", aByte)); // Formata como hexadecimal
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Erro ao gerar hash", e);
            }
        }
}

