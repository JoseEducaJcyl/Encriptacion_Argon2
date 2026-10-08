package org.example.Security;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordService {
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;

    private static final int MEMORY_KB = 65536;
    private static final int ITERATIONS = 3;
    private static final int PARALLELISM = 2;

    private final SecureRandom secureRandom =
            new SecureRandom();

    public String hashPassword(String password) {

        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        byte[] hash = generateHash(
                password,
                salt,
                MEMORY_KB,
                ITERATIONS,
                PARALLELISM
        );

        Base64.Encoder encoder =
                Base64.getEncoder().withoutPadding();

        return "$argon2id$"
                + "m=" + MEMORY_KB
                + ",t=" + ITERATIONS
                + ",p=" + PARALLELISM
                + "$"
                + encoder.encodeToString(salt)
                + "$"
                + encoder.encodeToString(hash);
    }

    public boolean verifyPassword(
            String password,
            String storedHash) {

        String[] parts = storedHash.split("\\$");

        if (parts.length != 5) {
            return false;
        }

        if (!"argon2id".equals(parts[1])) {
            return false;
        }

        String[] parameters =
                parts[2].split(",");

        int memory = Integer.parseInt(
                parameters[0].substring(2)
        );

        int iterations = Integer.parseInt(
                parameters[1].substring(2)
        );

        int parallelism = Integer.parseInt(
                parameters[2].substring(2)
        );

        Base64.Decoder decoder =
                Base64.getDecoder();

        byte[] salt =
                decoder.decode(parts[3]);

        byte[] expectedHash =
                decoder.decode(parts[4]);

        byte[] actualHash = generateHash(
                password,
                salt,
                memory,
                iterations,
                parallelism
        );

        return MessageDigest.isEqual(
                actualHash,
                expectedHash
        );
    }

    private byte[] generateHash(
            String password,
            byte[] salt,
            int memory,
            int iterations,
            int parallelism) {

        Argon2Parameters parameters =
                new Argon2Parameters.Builder(
                        Argon2Parameters.ARGON2_id
                )
                        .withSalt(salt)
                        .withMemoryAsKB(memory)
                        .withIterations(iterations)
                        .withParallelism(parallelism)
                        .build();

        Argon2BytesGenerator generator =
                new Argon2BytesGenerator();

        generator.init(parameters);

        byte[] hash = new byte[HASH_LENGTH];

        generator.generateBytes(
                password.toCharArray(),
                hash
        );

        return hash;
    }

}
