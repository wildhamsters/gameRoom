package org.wildhamsters.gameroom.configuration;

import java.util.List;

import org.wildhamsters.gameroom.fleet.ShipPosition;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Can't fix that for now")
public record PositionsDTO(List<ShipPosition> positions) {
}
