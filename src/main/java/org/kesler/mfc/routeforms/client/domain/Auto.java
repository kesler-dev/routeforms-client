package org.kesler.mfc.routeforms.client.domain;


/**
 * Автомобиль
 */
public class Auto extends AbstractEntity {

    protected Branch branch;
    protected String model;
    protected Type type;
    protected String regNum;
    protected Norm norm;
    protected FuelType fuelType;

    public Branch getBranch() { return branch; }

    public void setBranch(Branch branch) { this.branch = branch; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public String getRegNum() { return regNum; }
    public void setRegNum(String regNum) { this.regNum = regNum; }

    public Norm getNorm() { return norm; }
    public void setNorm(Norm norm) { this.norm = norm; }

    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }

    @Override
    public String toString() {
        return model + " " + regNum +
                (branch==null?"":" - "+branch.getName());
    }

    public enum Type {
        TRUCK("Грузовой"),
        CAR("Легковой");

        private String desc;

        Type(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return desc;
        }
    }

}
