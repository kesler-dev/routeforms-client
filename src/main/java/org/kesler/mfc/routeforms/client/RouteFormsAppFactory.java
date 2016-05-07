package org.kesler.mfc.routeforms.client;

import javafx.fxml.FXMLLoader;


import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.kesler.mfc.routeforms.client.gui.about.AboutController;
import org.kesler.mfc.routeforms.client.gui.auto.AutoController;
import org.kesler.mfc.routeforms.client.gui.auto.AutoListController;
import org.kesler.mfc.routeforms.client.gui.branch.BranchController;
import org.kesler.mfc.routeforms.client.gui.branch.BranchListController;
import org.kesler.mfc.routeforms.client.gui.driver.DriverController;
import org.kesler.mfc.routeforms.client.gui.driver.DriverListController;
import org.kesler.mfc.routeforms.client.gui.employee.EmployeeController;
import org.kesler.mfc.routeforms.client.gui.employee.EmployeeListController;
import org.kesler.mfc.routeforms.client.gui.login.LoginController;
import org.kesler.mfc.routeforms.client.gui.main.MainController;
import org.kesler.mfc.routeforms.client.gui.norm.NormController;
import org.kesler.mfc.routeforms.client.gui.norm.NormListController;
import org.kesler.mfc.routeforms.client.gui.options.ApplicationOptionsController;
import org.kesler.mfc.routeforms.client.gui.options.ConnectOptionsController;
import org.kesler.mfc.routeforms.client.gui.report.BranchReportController;
import org.kesler.mfc.routeforms.client.gui.routeform.RouteFormController;
import org.kesler.mfc.routeforms.client.gui.routeform.RouteFormsController;
import org.kesler.mfc.routeforms.client.gui.stat.DayStatController;
import org.kesler.mfc.routeforms.client.security.OptionsHolder;
import org.kesler.mfc.routeforms.client.security.LoginHolder;
import org.kesler.mfc.routeforms.client.service.RouteFormsService;
import org.kesler.mfc.routeforms.client.service.RouteFormsServiceRestImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * A Spring configuration class that provides factory methods for all the main components that make up the JavaFX client
 * application. This is used by the main App class on startup to load beans in a way that allows them to be autowired
 * into each other appropriately, along with all the other benefits of using Spring.
 * <p/>
 * Unless otherwise marked, all beans provided by this factory are singletons. So if you use an ApplicationContext for
 * loading this factory (as is done in the main App of this project), then each call to get a bean will return the same
 * instance. For example, the RestTemplate created below may get used in multiple services but only one instance will
 * ever be created and shared.
 * <p/>
 * This class is a direct replacement for the normal Spring XML file so in client side JavaFX we don't need this the
 * XML configuration file at all, just this.
 * <p/>
 * For more information on Spring configuration see:
 * http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/beans.html#beans-java
 */
@Configuration
@PropertySource(value="file:config/RouteForms.properties")
@ComponentScan({"org.kesler.mfc.routeforms.client.export"})
public class RouteFormsAppFactory {

    /**
     * Spring will wire this up to the properties file defined by the @PropertySource definition on this class. This
     * allows us to get access to our configuration properties in a fairly clean way.
     */
    @Autowired
    private Environment env;

    /**
     * Factory method for creating a RestTemplate, which is a Spring helper class that simplifies making Rest calls onto
     * a remote server. See http://blog.springsource.com/2009/03/27/rest-in-spring-3-resttemplate/ for more information
     * on RestTemplates.
     *
     * @return a RestTemplate ready for use in the
     */
    @Bean
    public RestTemplate restTemplate() {
        /// HttpComponents HttpClient  supports gzip encoding - using it
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                                            .setConnectTimeout(5000)
                                            .build())
                .build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    @Bean
    public MainController mainController() {
        MainController mainController = loadController("/fxml/Main.fxml");
        return mainController;
    }

    @Bean
    public LoginController loginController() {
       LoginController loginController = loadController("/fxml/Login.fxml");
        return loginController;
    }

    @Bean
    public EmployeeListController employeeListController() {
        EmployeeListController employeeListController = loadController("/fxml/EmployeeList.fxml");
        return employeeListController;
    }

    @Bean
    public EmployeeController employeeController() {
        EmployeeController employeeController = loadController("/fxml/Employee.fxml");
        return employeeController;
    }

    @Bean
    public BranchListController branchListController() {
        BranchListController branchListController = loadController("/fxml/BranchList.fxml");
        return branchListController;
    }

    @Bean
    public BranchController branchController() {
        BranchController branchController = loadController("/fxml/Branch.fxml");
        return branchController;
    }

    @Bean
    public DriverListController driverListController() {
        DriverListController driverListController = loadController("/fxml/DriverList.fxml");
        return driverListController;
    }

    @Bean
    public DriverController driverController() {
        DriverController driverController = loadController("/fxml/Driver.fxml");
        return driverController;
    }

    @Bean
    public AutoListController autoListController() {
        AutoListController autoListController = loadController("/fxml/AutoList.fxml");
        return autoListController;
    }

    @Bean
    public AutoController autoController() {
        AutoController autoController = loadController("/fxml/Auto.fxml");
        return autoController;
    }

    @Bean
    public NormListController normListController() {
        NormListController normListController = loadController("/fxml/NormList.fxml");
        return normListController;
    }

    @Bean
    public NormController normController() {
        NormController normController = loadController("/fxml/Norm.fxml");
        return normController;
    }


    @Bean
    public RouteFormsController routeFormsController() {
        RouteFormsController routeFormsController = loadController("/fxml/RouteForms.fxml");
        return routeFormsController;
    }

    @Bean
    public RouteFormController routeFormController() {
        RouteFormController routeFormController = loadController("/fxml/RouteForm.fxml");
        return routeFormController;
    }

    @Bean
    public BranchReportController branchReportController() {
        BranchReportController branchReportController = loadController("/fxml/BranchReport.fxml");
        return branchReportController;
    }

    @Bean
    public DayStatController dayStatController() {
        DayStatController dayStatController = loadController("/fxml/DayStat.fxml");
        return dayStatController;
    }



    @Bean
    public ConnectOptionsController connectOptionsController() {
        ConnectOptionsController connectOptionsController = loadController("/fxml/ConnectOptions.fxml");
        return connectOptionsController;
    }
   
    @Bean
    public ApplicationOptionsController applicationOptionsController() {
        ApplicationOptionsController applicationOptionsController = loadController("/fxml/ApplicationOptions.fxml");
        return applicationOptionsController;
    }

    @Bean
    public AboutController aboutController() {
        AboutController aboutController = loadController("/fxml/About.fxml");
        return aboutController;
    }


    /**
     * Вспомогательный класс для сохранения текущего пользователя
     * можем его использовать везде где надо
     */

    @Bean
    public LoginHolder loginHolder() {
        return new LoginHolder();
    }

    /**
     * Вспомогательный класс для хранения настроек приложения,
     * чтобы не загружать каждый раз из БД и можно было встраивать
     */

    @Bean
    public OptionsHolder applicationOptionsHolder() {
        OptionsHolder optionsHolder = new OptionsHolder();
        optionsHolder.setServerUrl(env.getProperty("server.url"));
        return optionsHolder;
    }


    @Bean
    public RouteFormsService routeFormsService() {
        return new RouteFormsServiceRestImpl();
    }



    /**
     * Convenience method for loading Controllers from FXML. FXML can be a little impure in its inter-dependencies
     * between client and server (it is quite biased to things being view driven and tightly couples the view into its
     * controller, etc). We make things a little cleaner by interacting mostly with the Controller and only accessing
     * the View via it. This load method hooks reverses the focus of the FXMLLoader to make things Controller based.
     *
     * @param fxmlFile the file to load the FXML from, this should be relative to the classpath.
     * @param <T> the type of Controller to return is inferred by whatever you assign the result of this method to.
     * @return the Controller loaded from the FXML specified which should have its view loaded and attached.
     */
    private <T> T loadController(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.load();
            return (T) loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to load FXML file '%s'", fxmlFile), e);
        }
    }
}
