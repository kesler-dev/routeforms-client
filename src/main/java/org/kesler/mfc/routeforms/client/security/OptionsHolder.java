package org.kesler.mfc.routeforms.client.security;

import org.kesler.mfc.routeforms.client.domain.ApplicationOptions;
import org.springframework.stereotype.Component;

/**
 * Created by alex on 28.06.15.
 */
@Component
public class OptionsHolder {

    private ApplicationOptions options;
    private String serverUrl;

    public ApplicationOptions getOptions() { return options; }
    public void setOptions(ApplicationOptions options) { this.options = options; }

    public String getServerUrl() { return serverUrl; }
    public void setServerUrl(String serverUrl) { this.serverUrl = serverUrl; }

}
