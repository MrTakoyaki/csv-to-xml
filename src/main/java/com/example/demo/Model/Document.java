package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "Document")
public class Document {

    @JacksonXmlProperty(localName = "h1")
    private String hOne;

    @JacksonXmlProperty(localName = "descriptions")
    private String descriptions;

    @JacksonXmlProperty(localName = "Definition")
    private String definition;

    @JacksonXmlProperty(localName = "DataSet")
    private List<DataSet> dataSet;

    public Document(){

    }

    @Override
    public String toString() {
        return "Document{" +
                "hOne='" + hOne + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", definition='" + definition + '\'' +
                ", dataSet=" + dataSet +
                '}';
    }

    public String gethOne() {
        return hOne;
    }

    public void sethOne(String hOne) {
        this.hOne = hOne;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<DataSet> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<DataSet> dataSet) {
        this.dataSet = dataSet;
    }

    public Document(String hOne, String descriptions, String definition, List<DataSet> dataSet) {
        this.hOne = hOne;
        this.descriptions = descriptions;
        this.definition = definition;
        this.dataSet = dataSet;
    }
}
