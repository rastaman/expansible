package io.expansible.model;


import com.wordnik.swagger.annotations.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

import javax.persistence.*;



@Entity
@Table(name = "person")
@ApiModel(description = "")
public class Person  {
  
  private Long id = null;
  private String firstName = null;
  private String lastName = null;
  private String email = null;
  private String picture = null;
  private String promoCode = null;

  
  /**
   * Identifier of the user.
   **/
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "Identifier of the user.")
  @JsonProperty("id")
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  
  /**
   * First name of the user.
   **/
  @ApiModelProperty(value = "First name of the user.")
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  
  /**
   * Last name of the user.
   **/
  @ApiModelProperty(value = "Last name of the user.")
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  
  /**
   * Email address of the user
   **/
  @ApiModelProperty(value = "Email address of the user")
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  
  /**
   * Image URL of the user.
   **/
  @ApiModelProperty(value = "Image URL of the user.")
  @JsonProperty("picture")
  public String getPicture() {
    return picture;
  }
  public void setPicture(String picture) {
    this.picture = picture;
  }

  
  /**
   * Promo code of the user.
   **/
  @ApiModelProperty(value = "Promo code of the user.")
  @JsonProperty("promoCode")
  public String getPromoCode() {
    return promoCode;
  }
  public void setPromoCode(String promoCode) {
    this.promoCode = promoCode;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Person {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  firstName: ").append(firstName).append("\n");
    sb.append("  lastName: ").append(lastName).append("\n");
    sb.append("  email: ").append(email).append("\n");
    sb.append("  picture: ").append(picture).append("\n");
    sb.append("  promoCode: ").append(promoCode).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
