package vn.edu.greenwich.myapplication.model;

public class Expense {
    private Integer id;
    private String type_of_cost;
    private double cost;
    private String additional_Comment;
    private String timeOfCost;
    private String dateOfCost;
    private String totalCost;
    private long staffId;



    public Expense() {
    }

    public Expense(Integer id, String type_of_cost, double cost, String additional_Comment, String timeOfCost, String dateOfCost, String totalCost, long staffId) {
        this.id = id;
        this.type_of_cost = type_of_cost;
        this.cost = cost;
        this.additional_Comment = additional_Comment;
        this.timeOfCost = timeOfCost;
        this.dateOfCost = dateOfCost;
        this.totalCost = totalCost;
        this.staffId = staffId;
    }



    public long getStaffId() {
        return staffId;
    }

    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType_of_cost() {
        return type_of_cost;
    }

    public void setType_of_cost(String type_of_cost) {
        this.type_of_cost = type_of_cost;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getAdditional_Comment() {
        return additional_Comment;
    }

    public void setAdditional_Comment(String additional_Comment) {
        this.additional_Comment = additional_Comment;
    }

    public String getTimeOfCost() {
        return timeOfCost;
    }

    public void setTimeOfCost(String timeOfCost) {
        this.timeOfCost = timeOfCost;
    }

    public String getDateOfCost() {
        return dateOfCost;
    }

    public void setDateOfCost(String dateOfCost) {
        this.dateOfCost = dateOfCost;
    }

}
