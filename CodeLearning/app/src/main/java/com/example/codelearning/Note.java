package com.example.codelearning;

public class Note {
    public String category;
    public String name;
    public String content;
    public Note(String category,String name, String content){
        this.category = category;
        this.name = name;
        this.content = content;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) {this.category = category; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}

