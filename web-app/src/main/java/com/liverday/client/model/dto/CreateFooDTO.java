package com.liverday.client.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFooDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Bar is required")
    private String bar;
}
