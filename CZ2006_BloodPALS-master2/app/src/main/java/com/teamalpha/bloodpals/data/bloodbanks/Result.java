package com.teamalpha.bloodpals.data.bloodbanks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("resource_id")
    @Expose
    private String resourceId;

    @SerializedName("fields")
    @Expose
    private List<Field> fields;

    @SerializedName("records")
    @Expose
    private List<Record> records;

    @SerializedName("_links")
    @Expose
    private Links links;

    @SerializedName("total")
    @Expose
    private Integer total;

    @SerializedName("q")
    @Expose
    private String query;

    @SerializedName("limit")
    @Expose
    private Integer limit;

    public Result(String resourceId, List<Field> fields, List<Record> records, Links links, Integer total, String query, Integer limit) {
        this.resourceId = resourceId;
        this.fields = fields;
        this.records = records;
        this.links = links;
        this.total = total;
        this.query = query;
        this.limit = limit;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

}
