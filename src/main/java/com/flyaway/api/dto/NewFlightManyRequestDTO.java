package com.flyaway.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class NewFlightManyRequestDTO {
    private List<NewFlightRequestDTO> inputs;
}