package org.kesler.mfc.routeforms.client.domain;



/**
 * Сотрудник
 */
public class Employee extends AbstractEntity {

    protected String fio;
    protected String password;
    protected Boolean admin;
    protected Boolean active;
    protected Branch branch;

    public String getFio() { return fio; }
    public void setFio(String fio) { this.fio = fio; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Boolean isAdmin() { return admin; }
    public void setAdmin(Boolean admin) { this.admin = admin; }

    public Boolean isActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }

    @Override
    public String toString() {
        return fio;
    }
}
