package data;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    private final SimpleIntegerProperty userid;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty firstname;
    private final SimpleStringProperty lastname;
    private final SimpleStringProperty email;
    private final SimpleStringProperty planType;
    private final SimpleStringProperty paymentMethod;
    private final SimpleStringProperty created;
    private final SimpleStringProperty subscriptionAmount;
    

    public User(Integer userid, String uname, String pword, String fname, String lname, String email, String created, String plan, String payment, String subscriptionAmount) {
                this.userid = new SimpleIntegerProperty(userid);
                this.username = new SimpleStringProperty(uname);
                this.password = new SimpleStringProperty(pword);
                this.firstname = new SimpleStringProperty(fname);
                this.lastname = new SimpleStringProperty(lname);
                this.email = new SimpleStringProperty(email);
                this.planType = new SimpleStringProperty(plan);
                this.paymentMethod = new SimpleStringProperty(payment);
                this.created = new SimpleStringProperty(created);
                this.subscriptionAmount = new SimpleStringProperty(subscriptionAmount);
        }
            public Integer getUserID() {
                return userid.get();
            }
        
            public String getUsername() {
                return username.get();
            }
        
            public String getPassword() {
                return password.get();
            }
        
            public String getFirstName() {
                return firstname.get();
            }
        
            public String getLastName() {
                return lastname.get();
            }
        
            public String getEmail() {
                return email.get();
            }
        
            public String getPlanType() {
                return planType.get();
            }

            public String getPaymentMethod() {
                return paymentMethod.get();
            }
            public String getCreated(){
                return created.get();
            }

            public String getSubscriptionAmount() {
                return subscriptionAmount.get();
            }
        }
