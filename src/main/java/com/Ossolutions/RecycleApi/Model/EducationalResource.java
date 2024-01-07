package com.Ossolutions.RecycleApi.Model;

import javax.persistence.*;


@Entity
@Table(name = "educational_resources")
public class EducationalResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String type; // Article, Video, Infographic, Tip, etc.
    private String content;
    // Other fields like author, date, etc.


    public EducationalResource(String title, String type, String content) {
        this.title = title;
        this.type = type;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
