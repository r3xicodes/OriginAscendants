package org.originsascendants.originAscendants.origins.base;

import org.originsascendants.originAscendants.player.PlayerState;

/**
 * Factory for creating Origin instances via reflection.
 * Looks for origin classes in their respective subpackages under origins.
 */
public class OriginFactory {
    public static Origin createOrigin(String typeName, PlayerState state) {
        if (typeName == null) throw new IllegalArgumentException("Type name cannot be null");
        String t = typeName.trim();
        if (t.isEmpty()) throw new IllegalArgumentException("Type name cannot be empty");

        // Normalize to simple class name form: "phantom" -> "Phantom", "sculkborn" -> "Sculkborn"
        String className = Character.toUpperCase(t.charAt(0)) + t.substring(1).toLowerCase();
        String packageName = t.toLowerCase();
        String fqcn = "org.originsascendants.originAscendants.origins." + packageName + "." + className;

        try {
            Class<?> cls = Class.forName(fqcn);
            // Find constructor that accepts PlayerState
            var ctor = cls.getConstructor(PlayerState.class);
            Object obj = ctor.newInstance(state);
            return (Origin) obj;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Type \"" + typeName + "\" doesn't exist.");
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Failed to instantiate origin type \"" + typeName + "\": " + e.getMessage(), e);
        }
    }
}
