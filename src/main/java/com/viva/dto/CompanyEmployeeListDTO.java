package com.viva.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyEmployeeListDTO {
    public int employeeId;
    public String employeeName;
    public String employeeContactNo;
    public int companyId;
    public String admin;
}
