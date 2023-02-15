package com.example.newdweller;

class AcidModel {
    String acidNames;
    String acidDesc;
    int image;
    String acidBDesc;
    String acidCost;


    public AcidModel(String acidNames, String acidDesc, int image,
                     String acidBDesc,String acidCost) {
        this.acidNames = acidNames;
        this.acidDesc = acidDesc;
        this.image = image;
        this.acidBDesc = acidBDesc;
        this.acidCost = acidCost;
    }

    public String getAcidNames() {
        return acidNames;
    }

    public String getAcidDesc() {
        return acidDesc;
    }

    public int getImage() {
        return image;
    }
    public String getAcidBDesc() {
        return acidBDesc;
    }
    public String getAcidCost() {
        return acidCost;
    }

}
