package org.example.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nameShopWB")
    private String nameShopWB;
    @Column(name = "tokenStandartWB")
    private String tokenStandartWB;

    @Column(name = "nameShopOzon")
    private String nameShopOzon;
    @Column(name = "tokenClientOzon")
    private String tokenClientOzon;
    @Column(name = "tokenStatisticOzon")
    private String tokenStatisticOzon;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Documents> documents;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String nameShopWB, String tokenStandartWB, String nameShopOzon, String tokenClientOzon, String tokenStatisticOzon, String email, String password) {
        this.nameShopWB = nameShopWB;
        this.tokenStandartWB = tokenStandartWB;
        this.nameShopOzon = nameShopOzon;
        this.tokenClientOzon = tokenClientOzon;
        this.tokenStatisticOzon = tokenStatisticOzon;
        this.email = email;
        this.password = password;
    }

    public List<Documents> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Documents> documents) {
        this.documents = documents;
    }

    public String getNameShopWB() {
        return nameShopWB;
    }

    public void setNameShopWB(String nameShopWB) {
        this.nameShopWB = nameShopWB;
    }

    public String getTokenStandartWB() {
        return tokenStandartWB;
    }

    public void setTokenStandartWB(String tokenStandartWB) {
        this.tokenStandartWB = tokenStandartWB;
    }

    public String getNameShopOzon() {
        return nameShopOzon;
    }

    public void setNameShopOzon(String nameShopOzon) {
        this.nameShopOzon = nameShopOzon;
    }

    public String getTokenClientOzon() {
        return tokenClientOzon;
    }

    public void setTokenClientOzon(String tokenClientOzon) {
        this.tokenClientOzon = tokenClientOzon;
    }

    public String getTokenStatisticOzon() {
        return tokenStatisticOzon;
    }

    public void setTokenStatisticOzon(String tokenStatisticOzon) {
        this.tokenStatisticOzon = tokenStatisticOzon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
