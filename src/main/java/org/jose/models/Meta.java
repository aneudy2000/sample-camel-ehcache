package org.jose.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta {

    private int count;

    @JsonProperty("total-count")
    private int totalCount;

    @JsonProperty("total-pages")
    private int totalPages;

    private DataItem labels;

    private DataItem dataTypes;

    private DataItem dataFormats;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public DataItem getLabels() {
        return labels;
    }

    public void setLabels(DataItem labels) {
        this.labels = labels;
    }

    public DataItem getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(DataItem dataTypes) {
        this.dataTypes = dataTypes;
    }

    public DataItem getDataFormats() {
        return dataFormats;
    }

    public void setDataFormats(DataItem dataFormats) {
        this.dataFormats = dataFormats;
    }
}
