package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootDto {
    public String tableNumber;
    public String articleNumber;
    public String salesOrganisation;
    public String conditionRecordNumber;
    public String validFrom;
    public String validTo;
    public String conditionType;
    public String createdTimestamp;
    public Object updatedTimestamp;
    public int ttl;
    public String eventType;
}
