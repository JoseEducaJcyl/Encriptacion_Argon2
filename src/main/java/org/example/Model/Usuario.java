package org.example.Model;

import java.time.LocalDateTime;

public class Usuario {
    private long id;
    private String email;
    private String passwordHash;
    private LocalDateTime creadoEn;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor para crear un usuario nuevo
    public Usuario(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    // Constructor completo
    public Usuario(long id, String email, String passwordHash, LocalDateTime creadoEn) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.creadoEn = creadoEn;
    }

    // Getters y setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", creadoEn=" + creadoEn +
                '}';
    }
}
