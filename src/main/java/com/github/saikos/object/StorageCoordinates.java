package com.github.saikos.object;

/**
 * Each object in the application environment has its own coordinates.
 */
public interface StorageCoordinates {
    String getStoreName();
    String getId();
    String encodeCoordinates();

    static String encode(String storeName, String id) {
        return storeName + ":" + id;
    }

    static StorageCoordinates decode(String coordinates) {
        String[] parts = coordinates.split(":");
        if(parts.length != 2) {
            throw new IllegalArgumentException("Invalid storage coordinates: " + coordinates);
        }
        return of(parts[0], parts[1]);
    }

    static StorageCoordinates of(String storeName, String id) {
        return new StorageCoordinates() {
            @Override
            public String getStoreName() {
                return storeName;
            }
            @Override
            public String getId() {
                return id;
            }
            @Override
            public String encodeCoordinates() {
                return encode(storeName, id);
            }
        };
    }
}
