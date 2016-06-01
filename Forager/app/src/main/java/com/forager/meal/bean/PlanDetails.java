package com.forager.meal.bean;

/**
 * Created by Akbar on 9/2/2015.
 */

public class PlanDetails {

    private String id;

    private String type;

//    private String product_id;

    private String image;

    private String meal_plan_logo;

//    private String price;

    private String description;

    private String tips_guidelines;

    private String daily_supplements;

    private String plan_images;

//    private String purchased_status;

    private String active_status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

 /*   public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
*/
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMeal_plan_logo() {
        return meal_plan_logo;
    }

    public void setMeal_plan_logo(String meal_plan_logo) {
        this.meal_plan_logo = meal_plan_logo;
    }

  /*  public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTips_guidelines() {
        return tips_guidelines;
    }

    public void setTips_guidelines(String tips_guidelines) {
        this.tips_guidelines = tips_guidelines;
    }

    public String getDaily_supplements() {
        return daily_supplements;
    }

    public void setDaily_supplements(String daily_supplements) {
        this.daily_supplements = daily_supplements;
    }

    public String getPlan_images() {
        return plan_images;
    }

    public void setPlan_images(String plan_images) {
        this.plan_images = plan_images;
    }

   /* public String getPurchased_status() {
        return purchased_status;
    }

    public void setPurchased_status(String purchased_status) {
        this.purchased_status = purchased_status;
    }
*/
    public String getActive_status() {
        return active_status;
    }

    public void setActive_status(String active_status) {
        this.active_status = active_status;
    }


}
