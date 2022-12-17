package kurskovik;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;



public class cipher {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private Map<String, PublicKey> basecip = new HashMap<>();

    public cipher(){
        generateKeyPair();


    }
    private void generateKeyPair(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048); // ключ длинной 2048 бит
            KeyPair pair = keyGen.generateKeyPair(); // генерируем ключи
            privateKey = pair.getPrivate(); // извлекаем приватный ключик
            publicKey = pair.getPublic(); // извлекаем публичный клчючик
            FileWriter pubwriter = new FileWriter("cipher.txt");


        } catch (Exception ex){
            throw  new RuntimeException(ex);
        }

    }
    public PublicKey getPublicKey(){
        return publicKey;
    } // возвращаем наш ключ публичный

    public void addMesseage (String name, PublicKey publicKey){
        basecip.put(name,publicKey); //прислали ключ и кладем этот ключ в базу
    }

    public String sendMessage(String recipient, String messeage){
        PublicKey publicKey = basecip.get(recipient); // по имени адреса мы извлекаем публичный ключ
        if (publicKey == null)
            throw new RuntimeException("неизвестный получатель");
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);

            byte [] msgBytes = messeage.getBytes(StandardCharsets.UTF_8); // на вход передаем массив с данными
            byte [] encypted = cipher.doFinal(msgBytes); // на выходе массив с зашифрованным сообщением

            return  Base64.getEncoder().encodeToString(encypted); //конвертируем массив в текстовую строку

        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public  String receiveMesseage (String cipherText){
        byte [] encrypted = Base64.getDecoder().decode(cipherText);
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte [] decrypted = cipher.doFinal(encrypted);

            return  new String (decrypted, StandardCharsets.UTF_8);

        } catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }





}
