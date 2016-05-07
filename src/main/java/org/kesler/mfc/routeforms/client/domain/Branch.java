package org.kesler.mfc.routeforms.client.domain;


/**
 * Филиал
 */
public class Branch extends AbstractEntity{

    private String name;
    private String series;
    private Integer routeFormCounter;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSeries() { return series; }
    public void setSeries(String series) { this.series = series; }

    public Integer getRouteFormCounter() { return routeFormCounter; }
    public void setRouteFormCounter(Integer routeFormCounter) { this.routeFormCounter = routeFormCounter; }

    @Override
    public String toString() {
        return name;
    }
}
