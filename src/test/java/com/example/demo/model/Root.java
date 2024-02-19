package com.example.demo.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"_rid", "_self", "_etag", "_attachments", "_ts"})
public class Root{
    public String id;
    public String usage;
    public String conditionTable;
    public String tableNumber;
    public String application;
    public String variableKey;
    public String articleNumber;
    public String salesOrganisation;
    public String distributionChannel;
    public String priceList;
    public String salesDocumentCurrency;
    public String purchasingOrganisation;
    public String baseMerchandiseCategory;
    public String articleType;
    public String salesUnit;
    public String serial;
    public String site;
    public String pricingDocumentNumber;
    public String conditionRecordNumber;
    public String validFrom;
    public String validTo;
    public String conditionType;
    public String scaleType;
    public String calculationTypeForCondition;
    public String amount;
    public String conditionCurrency;
    public String rateUnit;
    public String conditionPricingUnit;
    public String conditionUnit;
    public String numeratorForConversion;
    public String denominatorForConversion;
    public String baseUnitOfMeasure;
    public String vendorNumber;
    public String customerNumber;
    public String createdTimestamp;
    public Object updatedTimestamp;
    public int ttl;
    public String eventType;
    public String _rid;
    public String _self;
    public String _etag;
    public String _attachments;
    public int _ts;
}
