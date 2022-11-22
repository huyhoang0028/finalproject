package vn.edu.greenwich.myapplication.model;



public class Staff {
    private Integer id;
    private String name;
    private String business_trip_name;
    private String destination;
    private String date;
    private String riskAssessment;
    private String description;

    public Staff(String name, String business_trip_name, String destination,
                 String date, String riskAssessment, String description) {
        this.name = name;
        this.business_trip_name = business_trip_name;
        this.destination = destination;
        this.date = date;
        this.riskAssessment = riskAssessment;
        this.description = description;
    }

    public Staff() {
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusiness_trip_name() {
        return business_trip_name;
    }

    public void setBusiness_trip_name(String business_trip_name) {
        this.business_trip_name = business_trip_name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(String riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
