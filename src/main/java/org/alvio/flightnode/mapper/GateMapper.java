package org.alvio.flightnode.mapper;

import org.alvio.flightnode.dto.GateDTO;
import org.alvio.flightnode.dto.GateSummaryDTO;
import org.alvio.flightnode.rest.gate.Gate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GateMapper {

    public static GateDTO toDTO(Gate gate) {
        return new GateDTO(gate.getId(), gate.getName(), gate.getAirport().getId());
    }

    public static GateSummaryDTO toSummary(Gate gate) {
        return new GateSummaryDTO(gate.getId(), gate.getName());
    }

    public static List<GateDTO> toDTOList(List<Gate> gates) {
        return gates.stream()
                .sorted(Comparator.comparing(Gate::getName, String.CASE_INSENSITIVE_ORDER))
                .map(GateMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<GateSummaryDTO> toSummaryList(List<Gate> gates) {
        return gates.stream()
                .sorted(Comparator.comparing(Gate::getName, String.CASE_INSENSITIVE_ORDER))
                .map(GateMapper::toSummary)
                .collect(Collectors.toList());
    }


}
