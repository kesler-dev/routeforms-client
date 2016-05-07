package org.kesler.mfc.routeforms.client.security;


import org.kesler.mfc.routeforms.client.domain.Employee;
import org.springframework.stereotype.Component;

/**
 * Класс для сохранения текущего пользователя
 */
@Component
public class LoginHolder {

    private Employee currentEmployee;
    private boolean admin = false;

    public Employee getCurrentEmployee() { return currentEmployee; }
    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
        if (currentEmployee!=null) admin = currentEmployee.isAdmin()==null?false:currentEmployee.isAdmin();
        else admin = false;
    }


    public boolean isAdmin() { return admin; }
}
