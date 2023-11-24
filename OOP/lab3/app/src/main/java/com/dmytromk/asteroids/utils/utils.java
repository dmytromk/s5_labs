package com.dmytromk.asteroids.utils;

public class utils {
    public static float positiveMod(float value, float mod)
    {
        return ((value % mod + mod) % mod);
    }

}
