package com.expo.entity;

public class Plant {
    public String Name;  // 碰碰香
    public String LatinName;  // Plectranthus hadiensis var. tomentosus
    public String AliasName;
    public String Family;  // 唇形科
    public String Genus;  // 马刺花属
    public String Score;  // 92.74
    public String ImageUrl;

    public Plant() {
    }

    public Plant(String name, String latinName, String aliasName, String family, String genus, String score, String imageUrl) {
        Name = name;
        LatinName = latinName;
        AliasName = aliasName;
        Family = family;
        Genus = genus;
        Score = score;
        ImageUrl = imageUrl;
    }
}
