package org.example.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "documents")
public class Documents {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "serviceName")
    private String serviceName;
    @Column(name = "name")
    private String name;
    @Column(name = "category")
    private String category;
    @Column(name = "extensions")
    private String extensions;
    @Column(name = "creationTime")
    private String creationTime;
    @Column(name = "viewed")
    private String viewed;
    @Column(name = "download")
    private String download;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    public Documents(String serviceName, String name, String category, String extensions, String creationTime, String viewed, String download, User owner) {
        this.serviceName = serviceName;
        this.name = name;
        this.category = category;
        this.extensions = extensions;
        this.creationTime = creationTime;
        this.viewed = viewed;
        this.download = download;
        this.owner = owner;
    }

    public Documents() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getViewed() {
        return viewed;
    }

    public void setViewed(String viewed) {
        this.viewed = viewed;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
