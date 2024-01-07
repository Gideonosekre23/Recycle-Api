package com.Ossolutions.RecycleApi.Model;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "recycling_center")
public class RecyclingCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long centerId;

    @Column(nullable = false)
    private String centerName;

    @Column(nullable = false)
    private String location;

    @ElementCollection
    @CollectionTable(name = "accepted_materials", joinColumns = @JoinColumn(name = "center_id"))
    @Column(name = "material")
    private List<String> acceptedMaterials;

    @ElementCollection
    @CollectionTable(name = "material_prices", joinColumns = @JoinColumn(name = "center_id"))
    @MapKeyColumn(name = "material")
    @Column(name = "price")
    private List<Double> materialPrices;

    @Column(nullable = false)
    private String operationHours;

    public RecyclingCenter(String centerName, String location, List<String> acceptedMaterials, List<Double> materialPrices, String operationHours) {
        this.centerName = centerName;
        this.location = location;
        this.acceptedMaterials = acceptedMaterials;
        this.materialPrices = materialPrices;
        this.operationHours = operationHours;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getAcceptedMaterials() {
        return acceptedMaterials;
    }

    public void setAcceptedMaterials(List<String> acceptedMaterials) {
        this.acceptedMaterials = acceptedMaterials;
    }

    public List<Double> getMaterialPrices() {
        return materialPrices;
    }

    public void setMaterialPrices(List<Double> materialPrices) {
        this.materialPrices = materialPrices;
    }

    public String getOperationHours() {
        return operationHours;
    }

    public void setOperationHours(String operationHours) {
        this.operationHours = operationHours;
    }

    @Override
    public String toString() {
        return "RecyclingCenter{" +
                "centerId=" + centerId +
                ", centerName='" + centerName + '\'' +
                ", location='" + location + '\'' +
                ", acceptedMaterials=" + acceptedMaterials +
                ", materialPrices=" + materialPrices +
                ", operationHours='" + operationHours + '\'' +
                '}';
    }
}
