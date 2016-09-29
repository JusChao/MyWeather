package com.example.cyc.weatherinf.module;

import java.util.List;

/**
 * Created by cyc on 2016/9/18.
 */
public class City {

    /**
     * city_info : [{"city":"南子岛","cnty":"中国","id":"CN101310230","lat":"11.26","lon":"114.20","prov":"海南"}]
     * status : ok
     */

    private String status;
    /**
     * city : 南子岛
     * cnty : 中国
     * id : CN101310230
     * lat : 11.26
     * lon : 114.20
     * prov : 海南
     */

    private List<CityInfoBean> city_info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CityInfoBean> getCity_info() {
        return city_info;
    }

    public void setCity_info(List<CityInfoBean> city_info) {
        this.city_info = city_info;
    }

    public static class CityInfoBean {
        private String city;
        private String id;
        private String prov;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProv() {
            return prov;
        }

        public void setProv(String prov) {
            this.prov = prov;
        }
    }
}