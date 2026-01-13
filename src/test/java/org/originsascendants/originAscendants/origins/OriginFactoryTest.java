package org.originsascendants.originAscendants.origins;

import org.junit.jupiter.api.Test;
import org.originsascendants.originAscendants.player.PlayerState;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OriginFactoryTest {
    @Test
    void createOrigin_caseInsensitive() {
        PlayerState state = new PlayerState(UUID.randomUUID());
        Origin o = OriginFactory.createOrigin("phantom", state);
        assertNotNull(o);

        Origin o2 = OriginFactory.createOrigin("ScUlKbOrN", state);
        assertNotNull(o2);
    }

    @Test
    void createOrigin_unknown_throws() {
        PlayerState state = new PlayerState(UUID.randomUUID());
        assertThrows(IllegalArgumentException.class, () -> OriginFactory.createOrigin("phanotm", state));
    }
}